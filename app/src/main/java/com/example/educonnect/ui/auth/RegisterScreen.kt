package com.example.educonnect.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable  // <-- Tambahkan ini
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.components.EduButton
import com.example.educonnect.components.EduTextField
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.theme.GrayText
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark
import com.example.educonnect.ui.theme.White
import com.example.educonnect.utils.NIMMapper

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var studentClass by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(nim) {
        studentClass = NIMMapper.getInfoFromNIM(nim)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.School,
            contentDescription = null,
            tint = PurpleMain,
            modifier = Modifier.padding(top = 40.dp)
        )
        Text(
            "EduConnect",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = TextDark
        )
        Text(
            "Bergabunglah dengan komunitas akademik EduConnect hari ini.",
            color = GrayText,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        EduTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Full Name",
            leadingIcon = Icons.Default.Person
        )

        EduTextField(
            value = nim,
            onValueChange = { nim = it },
            label = "NIM (Student ID)",
            leadingIcon = Icons.Default.Badge,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        EduTextField(
            value = studentClass,
            onValueChange = {},
            label = "Class",
            leadingIcon = Icons.Default.Groups,
            readOnly = true
        )

        EduTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            leadingIcon = Icons.Default.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        EduTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        EduButton(
            text = "Daftar →",
            onClick = {
                if (fullName.isNotEmpty()) {
                    viewModel.register(
                        nim = nim,
                        fullName = fullName,
                        studentClass = studentClass,
                        email = email,
                        password = password,
                        onRegisterSuccess = {
                            onNavigateToLogin()
                        }
                    )
                }
            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sudah punya akun? Login",
            color = PurpleMain,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onNavigateToLogin() }
        )
    }
}