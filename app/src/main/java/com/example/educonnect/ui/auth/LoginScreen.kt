package com.example.educonnect.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educonnect.components.EduButton
import com.example.educonnect.components.EduTextField
import com.example.educonnect.ui.theme.GrayText
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark
import com.example.educonnect.ui.theme.White

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var nim by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Logo App & Name
        Icon(
            Icons.Default.School,
            contentDescription = null,
            tint = PurpleMain,
            modifier = Modifier.size(80.dp)
        )
        Text(
            "EduConnect",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = TextDark
        )
        Text("Silahkan masuk untuk melanjutkan", color = GrayText)

        Spacer(modifier = Modifier.height(40.dp))

        // 2. Input Fields
        EduTextField(
            value = nim,
            onValueChange = { nim = it },
            label = "NIM",
            leadingIcon = Icons.Default.Badge,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        EduTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )

        // 3. Status Loading atau Pesan Error/Sukses dari Firebase
        viewModel.loginStatus?.let { status ->
            val textColor = if (status.contains("Berhasil")) Color(0xFF2E7D32) else Color.Red
            Text(
                text = status,
                color = textColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 4. Login Button dengan kondisi Loading
        if (viewModel.isLoading) {
            CircularProgressIndicator(color = PurpleMain)
        } else {
            EduButton(
                text = "Login",
                onClick = {
                    if (nim.isNotEmpty() && password.isNotEmpty()) {
                        // Di sini kita panggil fungsi login baru yang butuh NIM dan Password
                        viewModel.login(nim, password, onLoginSuccess)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Footer Navigasi ke Register
        TextButton(onClick = onNavigateToRegister) {
            Text("Belum punya akun? Daftar sekarang", color = TextDark)
        }
    }
}