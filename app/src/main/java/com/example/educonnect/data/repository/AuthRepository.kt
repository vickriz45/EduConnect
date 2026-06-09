package com.example.educonnect.data.repository

import android.util.Log
import com.example.educonnect.data.local.UserDAO
import com.example.educonnect.data.local.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class AuthRepository(
    private val userDao: UserDAO,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val userProfile = userDao.observeUserProfile()

    private var cachedUser: UserEntity? = null
    private var cachedNim: String? = null

    suspend fun registerUser(user: UserEntity, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            Log.d("AuthRepo", "Memulai register: ${user.email}")

            val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            val uid = authResult.user?.uid ?: throw Exception("UID tidak ditemukan")
            Log.d("AuthRepo", "Firebase Auth success, UID: $uid")

            val userFirestoreData = user.copy(password = "PROTECTED")
            firestore.collection("users").document(uid).set(userFirestoreData).await()
            Log.d("AuthRepo", "Firestore save success")

            userDao.insertUser(user)
            Log.d("AuthRepo", "Room cache saved")

            Log.d("AuthRepo", "Register SUKSES")
            onSuccess()

        } catch (e: FirebaseAuthUserCollisionException) {
            Log.e("AuthRepo", "Email sudah terdaftar")
            onFailure("Email sudah digunakan")
        } catch (e: Exception) {
            Log.e("AuthRepo", "Register error: ${e.message}", e)
            onFailure(e.localizedMessage ?: "Pendaftaran gagal")
        }
    }

    suspend fun loginUser(nim: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val startTime = System.currentTimeMillis()
        Log.d("AuthRepo", "LOGIN DIMULAI untuk NIM: $nim")

        if (cachedNim == nim && cachedUser != null) {
            Log.d("AuthRepo", "Login pakai cache")
            if (cachedUser?.password == password) {
                Log.d("AuthRepo", "Cache password cocok, login instant")
                onSuccess()
                return
            } else {
                Log.d("AuthRepo", "Cache password tidak cocok, lanjut ke server")
            }
        }

        try {
            Log.d("AuthRepo", "Mencari user dengan NIM: $nim")

            val snapshot = withTimeout(15000) {
                firestore.collection("users")
                    .whereEqualTo("nim", nim)
                    .limit(1)
                    .get()
                    .await()
            }

            val queryTime = System.currentTimeMillis() - startTime
            Log.d("AuthRepo", "Query Firestore selesai dalam ${queryTime}ms")

            if (snapshot.isEmpty) {
                Log.e("AuthRepo", "NIM tidak ditemukan")
                onFailure("NIM tidak terdaftar di sistem")
                return
            }

            val document = snapshot.documents.first()
            val emailFromFirestore = document.getString("email")

            if (emailFromFirestore.isNullOrEmpty()) {
                Log.e("AuthRepo", "Email tidak ditemukan untuk NIM: $nim")
                onFailure("Data user tidak lengkap")
                return
            }

            Log.d("AuthRepo", "Email ditemukan: $emailFromFirestore")

            val authResult = withTimeout(10000) {
                firebaseAuth.signInWithEmailAndPassword(emailFromFirestore, password).await()
            }

            val authTime = System.currentTimeMillis() - startTime
            Log.d("AuthRepo", "Firebase Auth selesai dalam ${authTime}ms")

            if (authResult.user != null) {
                val userProfileRemote = document.toObject(UserEntity::class.java)

                if (userProfileRemote != null) {
                    cachedUser = userProfileRemote.copy(password = password)
                    cachedNim = nim
                    Log.d("AuthRepo", "Data disimpan ke cache")

                    try {
                        userDao.insertUser(userProfileRemote.copy(password = password))
                        Log.d("AuthRepo", "Data disimpan ke Room")
                    } catch (e: Exception) {
                        Log.w("AuthRepo", "Gagal simpan ke Room: ${e.message}")
                    }

                    Log.d("AuthRepo", "Login sukses untuk: ${userProfileRemote.fullName}")
                }

                val totalTime = System.currentTimeMillis() - startTime
                Log.d("AuthRepo", "TOTAL LOGIN: ${totalTime}ms")

                onSuccess()
            } else {
                Log.e("AuthRepo", "Autentikasi gagal - user null")
                onFailure("Autentikasi gagal")
            }

        } catch (e: TimeoutCancellationException) {
            Log.e("AuthRepo", "Timeout melebihi batas waktu 15 detik")
            Log.d("AuthRepo", "Total waktu sampai timeout: ${System.currentTimeMillis() - startTime}ms")

            try {
                Log.d("AuthRepo", "Mencoba fallback ke Room database")
                val localUser = userDao.getUserProfile()
                if (localUser?.nim == nim && localUser.password == password) {
                    Log.d("AuthRepo", "Login sukses dari Room offline mode")
                    cachedUser = localUser
                    cachedNim = nim
                    onSuccess()
                } else {
                    onFailure("Koneksi lambat dan tidak ada data offline")
                }
            } catch (fallbackError: Exception) {
                onFailure("Koneksi lambat, silakan coba lagi")
            }

        } catch (e: Exception) {
            Log.e("AuthRepo", "Login error: ${e.message}", e)
            Log.d("AuthRepo", "Total waktu sampai error: ${System.currentTimeMillis() - startTime}ms")

            try {
                Log.d("AuthRepo", "Mencoba fallback ke Room database")
                val localUser = userDao.getUserProfile()
                if (localUser?.nim == nim && localUser.password == password) {
                    Log.d("AuthRepo", "Login sukses dari Room offline mode")
                    cachedUser = localUser
                    cachedNim = nim
                    onSuccess()
                } else {
                    onFailure("NIM atau Password salah: ${e.localizedMessage}")
                }
            } catch (fallbackError: Exception) {
                onFailure("NIM atau Password salah: ${e.localizedMessage}")
            }
        }
    }

    suspend fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            Log.d("AuthRepo", "Meminta reset password untuk email: $email")
            firebaseAuth.sendPasswordResetEmail(email).await()
            Log.d("AuthRepo", "Email instruksi reset password berhasil dikirim")
            onSuccess()
        } catch (e: Exception) {
            Log.e("AuthRepo", "Gagal mengirim email reset: ${e.message}", e)
            onFailure(e.localizedMessage ?: "Gagal mengirim instruksi reset password")
        }
    }

    suspend fun updateProfile(
        email: String,
        imageUri: android.net.Uri? = null,
        context: android.content.Context,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            Log.d("AuthRepo", "Memulai proses update profile: email=$email")

            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                Log.e("AuthRepo", "Operasi dibatalkan: User tidak terautentikasi")
                onFailure("User tidak login")
                return
            }

            val updates = hashMapOf<String, Any>(
                "email" to email
            )

            if (imageUri != null) {
                Log.d("AuthRepo", "Mendeteksi file gambar baru, memulai upload ke Firebase Storage...")

                val storage = com.google.firebase.storage.FirebaseStorage.getInstance()
                val storageRef = storage.reference.child("profile_pictures/${currentUser.uid}.jpg")

                val uploadTask = storageRef.putFile(imageUri)
                uploadTask.await()
                Log.d("AuthRepo", "Upload ke Storage berhasil sepenuhnya!")

                val downloadUrl = storageRef.downloadUrl.await().toString()
                Log.d("AuthRepo", "Mendapatkan URL Publik: $downloadUrl")

                updates["profilePictureUrl"] = downloadUrl
            }

            firestore.collection("users").document(currentUser.uid).update(updates).await()
            Log.d("AuthRepo", "Sinkronisasi Cloud Firestore Sukses!")

            val currentUserData = userDao.getUserProfile()
            currentUserData?.let {
                val updatedUser = it.copy(
                    email = email,
                    profilePictureUrl = if (updates.containsKey("profilePictureUrl")) updates["profilePictureUrl"].toString() else it.profilePictureUrl
                )
                userDao.insertUser(updatedUser)
                Log.d("AuthRepo", "Sinkronisasi Room Cache Lokal Sukses!")
            }

            cachedUser = cachedUser?.copy(
                email = email,
                profilePictureUrl = if (updates.containsKey("profilePictureUrl")) updates["profilePictureUrl"].toString() else cachedUser?.profilePictureUrl ?: ""
            )

            Log.d("AuthRepo", "PROSES UPDATE PROFIL SELESAI DENGAN SUKSES")
            onSuccess()

        } catch (e: Exception) {
            Log.e("AuthRepo", "Terjadi kesalahan saat update profile: ${e.message}", e)
            onFailure(e.localizedMessage ?: "Gagal memperbarui profil")
        }
    }

    suspend fun logoutUser(onComplete: () -> Unit) {
        Log.d("AuthRepo", "Logout user")
        firebaseAuth.signOut()
        userDao.clearData()
        cachedUser = null
        cachedNim = null
        onComplete()
    }

    fun clearCache() {
        cachedUser = null
        cachedNim = null
        Log.d("AuthRepo", "Cache dibersihkan")
    }
}