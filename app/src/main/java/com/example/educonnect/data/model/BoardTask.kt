package com.example.educonnect.data.model

data class BoardTask(
    val id: String,
    val authorName: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val imageUri: String? = null,
    val documentName: String? = null
)
