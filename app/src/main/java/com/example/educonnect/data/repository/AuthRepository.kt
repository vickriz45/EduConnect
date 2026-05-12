package com.example.educonnect.data.repository

import com.example.educonnect.data.local.UserDAO
import com.example.educonnect.data.local.UserEntity

class AuthRepository(private val userDao: UserDAO) {
    //Fungsi yang dipanggil saat mengklik tombol DAFTAR
    suspend fun registerUser(user: UserEntity){
        //Simulasi dummy: Simpan ke lokal dulu
        userDao.insertUser(user)
    }
    suspend fun getSavedUser(): UserEntity? {
        return userDao.getUserProfile()
    }
}