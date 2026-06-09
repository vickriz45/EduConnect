package com.example.educonnect.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.components.EduButton
import com.example.educonnect.components.EduTextField
import com.example.educonnect.ui.theme.GrayText
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark
import com.example.educonnect.ui.theme.White

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = TextDark,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { onBackToLogin() }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Icon(
            Icons.Default.School,
            contentDescription = null,
            tint = PurpleMain,
            modifier = Modifier.size(64.dp)
        )
        Text(
            "Lupa Password?",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Masukkan email Anda untuk menerima instruksi pengaturan ulang kata sandi.",
            color = GrayText,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        EduTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            leadingIcon = Icons.Default.Email
        )

        Spacer(modifier = Modifier.height(32.dp))

        EduButton(
            text = "Kirim Instruksi",
            onClick = {
                onBackToLogin()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Kembali ke Login",
            color = PurpleMain,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onBackToLogin() }
        )
    }
}