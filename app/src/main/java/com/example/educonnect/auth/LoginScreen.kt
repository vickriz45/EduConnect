package com.example.educonnect.auth

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.educonnect.ui.auth.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        // INPUT PASSWORD (Tambahkan ini agar sesuai desain Figma)
        EduTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            leadingIcon = Icons.Default.Lock, // Perlu import Icons.Default.Lock
            isPassword = true // Supaya teks bintang-bintang
        )

        // Tampilkan pesan error jika gagal
        viewModel.loginStatus?.let {
            Text(it, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))

        // 3. Login Button
        EduButton(
            text = "Login",
            onClick = {
                if(nim.isNotEmpty()) {
                    viewModel.login(nim, onLoginSuccess)
                }
            }
        )
        // 4. Footer
        TextButton(onClick = onNavigateToRegister) {
            Text("Belum punya akun? Daftar sekarang", color = TextDark)
        }
    }
}