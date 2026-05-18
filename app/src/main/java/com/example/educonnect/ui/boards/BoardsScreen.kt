package com.example.educonnect.ui.boards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.educonnect.components.EduBottomNavigation
import com.example.educonnect.data.model.BoardTask

@Composable
fun BoardsScreen(
    navController: NavHostController,
    onNavigateToCreateTask: () -> Unit
) {
    // Data dummy untuk simulasi mading tugas kuliah
    val taskList = remember {
        listOf(
            BoardTask("1", "Vicky Stecu", "Tugas 1 (Informatika)", "Membuat rancangan Class Diagram dan Kebutuhan Fungsional sistem informasi berbasis mobile.", "2 hours ago", null, "Template_RPL.pdf"),
            BoardTask("2", "Alvina Kicaw", "Tugas Praktikum API", "Implementasi REST API menggunakan Laravel. Dokumentasi harus lengkap beserta endpoint-nya.", "5 hours ago", "mock_image_uri", "API_Specs.pdf")
        )
    }

    Scaffold(
        bottomBar = { EduBottomNavigation(navController = navController) },
        floatingActionButton = {
            // Tombol + di kanan bawah, posisinya otomatis diatur Scaffold tepat di atas navbar
            FloatingActionButton(
                onClick = onNavigateToCreateTask,
                containerColor = Color(0xFF8B5CF6), // Warna ungu ikon kirim
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Tugas")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Papan Tugas Kuliah",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFF1A1A40)
            )
            Text(
                text = "Informasi tugas bersama yang bisa di-pin ke ruang diskusi",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(taskList) { task ->
                    TaskCard(task = task)
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: BoardTask) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(task.authorName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(task.timeAgo, fontSize = 11.sp, color = Color.Gray)
                }
                // Tombol simulasi pin ke Chat Page
                IconButton(onClick = { /* Logika Pin ke Chat */ }) {
                    Icon(Icons.Default.PushPin, contentDescription = "Pin ke Chat", tint = Color(0xFF8B5CF6))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(task.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1A1A40))
            Spacer(modifier = Modifier.height(4.dp))
            Text(task.description, fontSize = 14.sp, color = Color.DarkGray)

            // Jika ada attachment gambar
            if (task.imageUri != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFFE9ECEF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gambar Tugas Disisipkan", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            // Jika ada attachment dokumen
            if (task.documentName != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFF1F3F5),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF7451EB))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(task.documentName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}