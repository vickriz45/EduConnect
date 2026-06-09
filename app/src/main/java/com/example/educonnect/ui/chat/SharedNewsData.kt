package com.example.educonnect.ui.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SharedNewsData(
    val title: String,
    val description: String,
    val time: String,
    val senderName: String = "System"
) : Parcelable