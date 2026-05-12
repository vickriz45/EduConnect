package com.example.educonnect.auth

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
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.components.EduButton
import com.example.educonnect.components.EduTextField
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.utils.NIMMapper

@Composable
fun RegisterScreen (
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
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header Logo disini nanti

        Text("Daftar Akun Baru", fontWeight = FontWeight.Bold, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        //Pake component reusable
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
        //Field Class dibuat READ ONLY karena otomatis dari NIM
        EduTextField(
            value = studentClass,
            onValueChange = { }, //gak perlu change karena otomatis
            label = "Class",
            leadingIcon = Icons.Default.Groups,
            readOnly = true
        )
        EduTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(32.dp))
        //Button daftar
        EduButton(
            text = "Daftar ->",
            onClick = {
                if (fullName.isNotEmpty() && nim.isNotEmpty()) {
                    viewModel.register(nim, fullName, studentClass, email)
                    println("Data berhasil disimpan ke Room Database")
                }
            }
        )
    }
}