package com.example.educonnect.data.repository

import com.example.educonnect.data.local.UserDAO
import com.example.educonnect.data.local.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val userDao: UserDAO,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val userProfile: Flow<UserEntity?> = userDao.observeUserProfile()

    // REGISTER: Firebase Auth -> Cloud Firestore -> Room Cache
    suspend fun registerUser(user: UserEntity, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            val uid = authResult.user?.uid

            if (uid != null) {
                val userFirestoreData = user.copy(password = "PROTECTED")
                firestore.collection("users").document(uid).set(userFirestoreData).await()
                userDao.insertUser(user)
                onSuccess()
            }
        } catch (e: Exception) {
            onFailure(e.localizedMessage ?: "Pendaftaran gagal")
        }
    }

    // LOGIN: Cari Email via NIM di Firestore -> Jalankan Firebase Auth -> Simpan ke Room Cache
    suspend fun loginUser(nim: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("nim", nim)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val document = snapshot.documents.first()
                val emailFromFirestore = document.getString("email") ?: ""
                val authResult = firebaseAuth.signInWithEmailAndPassword(emailFromFirestore, password).await()

                if (authResult.user != null) {
                    val userProfileRemote = document.toObject(UserEntity::class.java)
                    if (userProfileRemote != null) {
                        val userLocalCache = userProfileRemote.copy(password = password)
                        userDao.insertUser(userLocalCache)
                    }
                    onSuccess()
                }
            } else {
                onFailure("NIM tidak terdaftar di sistem")
            }
        } catch (e: Exception) {
            onFailure(e.localizedMessage ?: "NIM atau Password salah")
        }
    }

    suspend fun updateProfile(
        nim: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            val updates = mapOf(
                "nim" to nim,
                "email" to email
            )
            // Update di Firestore
            firestore.collection("users").document(nim).update(updates).await()
            onSuccess()
        } catch (e: Exception) {
            onFailure(e.localizedMessage ?: "Gagal memperbarui profil")
        }
    }

    suspend fun logoutUser(onComplete: () -> Unit) {
        firebaseAuth.signOut()
        userDao.clearData()
        onComplete()
    }
}