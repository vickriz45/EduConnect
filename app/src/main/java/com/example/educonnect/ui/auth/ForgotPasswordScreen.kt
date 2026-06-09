package com.example.educonnect.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.educonnect.components.EduButton
import com.example.educonnect.components.EduTextField
import com.example.educonnect.ui.theme.GrayText
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark
import com.example.educonnect.ui.theme.White

@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    val forgotStatus by authViewModel.forgotPasswordStatus.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose { authViewModel.resetForgotPasswordStatus() }
    }

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

        if (forgotStatus != null && forgotStatus != "success") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = forgotStatus ?: "",
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        EduButton(
            text = "Kirim Instruksi",
            onClick = {
                authViewModel.resetPassword(email = email.trim()) {
                    Toast.makeText(context, "Instruksi reset terkirim! Silakan cek kotak masuk Email Anda.", Toast.LENGTH_LONG).show()
                    onBackToLogin()
                }
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