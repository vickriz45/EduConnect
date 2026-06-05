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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.components.EduButton
import com.example.educonnect.ui.auth.AuthViewModel

@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    // State tampungan input user
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") } // Awalnya kosong sesuai request-mu
    var aboutMe by remember { mutableStateOf("") }

    // Efek ketika halaman pertama kali dibuka, load data lama dari Firebase (jika ada)
    LaunchedEffect(Unit) {
        // Nanti di sini kita ambil email asli yang sedang login dari Firebase Auth
        // email = FirebaseAuth.getInstance().currentUser?.email ?: ""
    }

    Scaffold(
        // Menggunakan header buatan sendiri (Row) agar 100% aman dari error @OptIn
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                /* 1. HEADER NAVIGASI */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Edit Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }

                /* 2. AREA UBAH FOTO PROFIL (Lingkaran dengan Ikon Kamera Overlap) */
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .border(2.dp, Color(0xFF9333EA), CircleShape)
                            .padding(4.dp)
                            .background(Color(0xFFE9ECEF), CircleShape)
                            .clickable { /* Logika buka galeri untuk ganti foto */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(70.dp)
                        )

                        // Badge Ikon Kamera Kecil di sudut kanan bawah foto
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF9333EA), CircleShape)
                                .align(Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Ganti Foto",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                /* 3. INPUT FIELD EMAIL (WAJIB ISI) */
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                /* 4. INPUT FIELD NOMOR TELEPON (OPSIONAL - BISA KOSONG) */
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Nomor Telepon (Opsional)") },
                    placeholder = { Text("Contoh: 08123456xxx") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                /* 5. INPUT FIELD ABOUT ME */
                OutlinedTextField(
                    value = aboutMe,
                    onValueChange = { aboutMe = it },
                    label = { Text("About Me") },
                    placeholder = { Text("Ceritakan sedikit tentang dirimu atau keahlian kodingmu...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            /* 6. TOMBOL SIMPAN PERUBAHAN */
            EduButton(
                text = "Simpan Perubahan",
                onClick = {
                    // Validasi: Minimal Email tidak boleh kosong sebelum di-save ke Firebase
                    if (email.isNotEmpty()) {
                        // Logika updateProfile ke Firebase Firestore & Auth nanti di sini
                        onNavigateBack() // Kembali ke halaman profile setelah sukses
                    }
                }
            )
        }
    }
}