package com.example.educonnect.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.educonnect.components.EduButton
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.theme.PurpleMain

@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    // Ambil data user yang sedang login
    val userProfile by authViewModel.userProfile.collectAsStateWithLifecycle()

    // State input – diisi dengan data lama saat pertama kali load
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var aboutMe by remember { mutableStateOf("") }

    // Isi field dengan data yang sudah ada di Room/Firebase saat komponen pertama kali muncul
    LaunchedEffect(userProfile) {
        userProfile?.let { profile ->
            if (email.isEmpty()) email = profile.email
            // phoneNumber & aboutMe bisa ditambahkan ke UserEntity jika diperlukan
        }
    }

    val updateStatus by authViewModel.updateStatus.collectAsStateWithLifecycle()

    // Kalau update sukses, langsung kembali
    LaunchedEffect(updateStatus) {
        if (updateStatus == "success") {
            authViewModel.resetUpdateStatus()
            onNavigateBack()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // ── Header ──────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Edit Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }

                // ── Avatar + Badge Kamera ────────────────────────────────
                // FIX: Gunakan Box tunggal dengan ukuran tetap agar badge kamera
                // tidak overlap ke luar batas lingkaran.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(120.dp)) {
                        // Lingkaran avatar
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, PurpleMain, CircleShape)
                                .background(Color(0xFFE9ECEF))
                                .clickable { /* Logika buka galeri */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(70.dp)
                            )
                        }

                        // Badge kamera – posisi sudut kanan bawah
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(PurpleMain),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Ganti Foto",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // ── Input Email ──────────────────────────────────────────
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = email.isEmpty()
                )

                if (email.isEmpty()) {
                    Text(
                        text = "Email tidak boleh kosong",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tampilkan pesan error dari ViewModel jika ada
                if (updateStatus != null && updateStatus != "success") {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = updateStatus ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            // ── Tombol Simpan ────────────────────────────────────────────
            EduButton(
                text = "Simpan Perubahan",
                onClick = {
                    if (email.isNotEmpty()) {
                        authViewModel.updateProfile(email = email)
                    }
                }
            )
        }
    }
}