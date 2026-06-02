package com.example.educonnect.ui.boards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.components.EduButton

@Composable
fun CreateTaskScreen(onNavigateBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                /* 1. HEADER BUATAN SENDIRI (Pengganti TopAppBar - 100% Bebas Merah) */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tulis Tugas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }

                /* 2. INPUT FIELD JUDUL TUGAS */
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Tugas (Contoh: Tugas 1 Informatika)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                /* 3. INPUT FIELD DESKRIPSI TUGAS */
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Tulis informasi tugas di sini...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                /* 4. TOMBOL TAMBAH GAMBAR */
                OutlinedButton(
                    onClick = { /* Logika buka galeri */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tambah Gambar", color = Color.DarkGray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                /* 5. TOMBOL TAMBAH DOKUMEN */
                OutlinedButton(
                    onClick = { /* Logika buka file picker */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Description, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tambah Dokumen", color = Color.DarkGray)
                }
            }

            /* 6. TOMBOL BAGIKAN TUGAS */
            EduButton(
                text = "Bagikan Tugas",
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        onNavigateBack()
                    }
                }
            )
        }
    }
}