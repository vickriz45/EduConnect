package com.example.educonnect.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey
    val nim: String = "",
    val fullName: String = "",
    val studentClass: String = "",
    val email: String = "",
    val prodi: String = "",
    val password: String = ""
)