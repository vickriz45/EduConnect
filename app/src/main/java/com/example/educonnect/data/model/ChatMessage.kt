package com.example.educonnect.data.model

data class ChatMessage(
    val senderName: String,
    val message: String,
    val time: String,
    val isMine: Boolean,
    val hasAttachment: Boolean = false,
    val fileName: String? = null
)
