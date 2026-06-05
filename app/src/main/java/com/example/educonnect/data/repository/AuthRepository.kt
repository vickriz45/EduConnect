package com.example.educonnect.data.repository

import com.example.educonnect.data.local.UserDAO
import com.example.educonnect.data.local.UserEntity

class AuthRepository(
    private val userDao: UserDAO
) {
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserProfile(): UserEntity? {
        return userDao.getUserProfile()
    }

    suspend fun clearData() {
        userDao.clearData()
    }
}