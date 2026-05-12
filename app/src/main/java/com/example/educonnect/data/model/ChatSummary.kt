package com.example.educonnect.data.model

data class ChatSummary(
    val id: Int,
    val title: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val isGroup: Boolean = true
)
