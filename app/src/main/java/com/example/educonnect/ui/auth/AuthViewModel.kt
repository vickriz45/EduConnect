package com.example.educonnect.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

open class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    var loginStatus by mutableStateOf<String?>(null)
        private set

    var registerStatus by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        // Aktifkan Offline Persistence agar query NIM tetap jalan walau internet drop
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
            .build()
        firestore.firestoreSettings = settings
    }

    fun getCurrentUser(onResult: (String) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Mengambil real-time data dari cache lokal (offline-first) atau cloud remote
            firestore.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("fullName") ?: "Mahasiswa"
                        onResult(name)
                    } else {
                        onResult("Mahasiswa PNM")
                    }
                }
                .addOnFailureListener {
                    onResult("Mahasiswa PNM (Offline)")
                }
        } else {
            onResult("Sumbul")
        }
    }

    /**
     * LOGIN MENGGUNAKAN NIM
     * Menghubungkan NIM ke Email Pribadi yang terdaftar di Firestore, lalu melakukan Auth Login
     */
    fun login(nim: String, password: String, onLoginSuccess: () -> Unit) {
        if (nim.isBlank() || password.isBlank()) {
            loginStatus = "NIM dan Password tidak boleh kosong!"
            return
        }

        isLoading = true
        loginStatus = "Memeriksa NIM..."

        viewModelScope.launch {
            try {
                // 1. Cari dokumen di Firestore yang memiliki NIM tersebut
                val querySnapshot = firestore.collection("users")
                    .whereEqualTo("nim", nim.trim())
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    // 2. Ambil email pribadi dari dokumen yang ditemukan
                    val userDocument = querySnapshot.documents[0]
                    val registeredEmail = userDocument.getString("email")

                    if (registeredEmail != null) {
                        loginStatus = "Mencoba masuk..."

                        // 3. Lakukan login ke Firebase Auth menggunakan email pribadi tersebut
                        auth.signInWithEmailAndPassword(registeredEmail, password).await()

                        loginStatus = "Login Berhasil!"
                        isLoading = false
                        onLoginSuccess()
                    } else {
                        loginStatus = "Gagal: Data email tidak ditemukan."
                        isLoading = false
                    }
                } else {
                    loginStatus = "NIM tidak terdaftar!"
                    isLoading = false
                }
            } catch (e: Exception) {
                isLoading = false
                loginStatus = "Login Gagal: ${e.localizedMessage ?: "Periksa koneksi atau password Anda"}"
            }
        }
    }

    /**
     * REGISTER MENGGUNAKAN EMAIL PRIBADI + DATA NIM
     */
    fun register(
        nim: String,
        fullName: String,
        studentClass: String,
        emailPribadi: String,
        password: String,
        onRegisterSuccess: () -> Unit
    ) {
        if (nim.isBlank() || fullName.isBlank() || emailPribadi.isBlank() || password.isBlank()) {
            registerStatus = "Semua field wajib diisi!"
            return
        }

        isLoading = true
        registerStatus = "Mendaftarkan akun..."

        viewModelScope.launch {
            try {
                // 1. Cek dulu apakah NIM ini sudah pernah dipakai orang lain di Firestore
                val checkNIM = firestore.collection("users")
                    .whereEqualTo("nim", nim.trim())
                    .get()
                    .await()

                if (!checkNIM.isEmpty) {
                    registerStatus = "NIM sudah terdaftar dengan akun lain!"
                    isLoading = false
                    return@launch
                }

                // 2. Buat user di Firebase Authentication menggunakan email pribadi
                val authResult = auth.createUserWithEmailAndPassword(emailPribadi.trim(), password).await()
                val uid = authResult.user?.uid

                if (uid != null) {
                    // 3. Simpan data lengkap profil (termasuk NIM & Email Pribadi) ke Firestore
                    val userProfile = hashMapOf(
                        "uid" to uid,
                        "nim" to nim.trim(),
                        "fullName" to fullName.trim(),
                        "studentClass" to studentClass,
                        "email" to emailPribadi.trim(),
                        "prodi" to "TRPL"
                    )

                    firestore.collection("users").document(uid).set(userProfile).await()

                    registerStatus = "Pendaftaran Berhasil!"
                    isLoading = false
                    onRegisterSuccess()
                }
            } catch (e: Exception) {
                isLoading = false
                registerStatus = "Pendaftaran Gagal: ${e.localizedMessage}"
            }
        }
    }
}