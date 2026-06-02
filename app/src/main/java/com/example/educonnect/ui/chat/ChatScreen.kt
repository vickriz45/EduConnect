package com.example.educonnect.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.educonnect.data.model.ChatMessage

@Composable
fun ChatScreen(navController: NavHostController) {
    // Data dummy simulasi percakapan mahasiswa TRPL PNM sesuai gambar Figma kamu
    val messages = remember {
        listOf(
            ChatMessage("Vicky Stecu", "cah, wes ngumpulne tugas RPL rung? aku rodok bingung ning bagian class diagram e", "09:45 AM", false),
            ChatMessage("Ripki", "wes pik, tinggal ndelok kebutuhan fungsional e. metu a nggarap bareng? rung bar ki infone gas ye ngko ning ndi ngono", "10:15 AM", true),
            ChatMessage("Alvina Kicaw Mania", "ayo ae lek aku, ki mang aku wes nggarap beberapa. ngko ewangi nggarap API ne @all", "10:20 AM", false, true, "API_Documentation_Ref.pdf"),
            ChatMessage("Vicky Stecu", "yowesno, ngko nde kwarcab e jam 2", "10:25 AM", false)
        )
    }

    Scaffold(
        topBar = { ChatTopBar() },
        bottomBar = { EduBottomNavigation(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(padding)
        ) {
            // 1. Divider Tanggal "Today"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(color = Color(0xFFE9ECEF), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Today",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // 2. Daftar Pesan (LazyColumn)
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Memperbaiki typo '1fr' yang bikin error baris 54 & 77
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(msg)
                }
            }

            // 3. Kolom Input Teks di paling bawah
            ChatInputBar()
        }
    }
}

@Composable
fun ChatTopBar() {
    Surface(shadowElevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = Color(0xFF1A1A40),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("EduConnect", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1A1A40))
                    Text("TEKNOLOGI REKAYASA PERANGKAT LUNAK", fontSize = 10.sp, color = Color.Gray)
                }
            }

            // Placeholder Logo Kampus di kanan atas
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(20.dp).background(Color.LightGray, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(30.dp, 15.dp).background(Color.LightGray))
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isMine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!msg.isMine) {
            // Avatar Teman (Lingkaran Abu-abu)
            Box(modifier = Modifier.size(35.dp).background(Color(0xFFE9ECEF), CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(horizontalAlignment = (if (msg.isMine) Alignment.End else Arrangement.Start) as Alignment.Horizontal) {
            if (!msg.isMine) {
                Text(
                    text = msg.senderName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }

            Surface(
                color = if (msg.isMine) Color(0xFF1A1A40) else Color(0xFFE9ECEF), // Menyesuaikan warna bubble Figma kamu
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (msg.isMine) 16.dp else 0.dp,
                    bottomEnd = if (msg.isMine) 0.dp else 16.dp
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    if (msg.hasAttachment) {
                        AttachmentView(msg.fileName ?: "")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(
                        text = msg.message,
                        color = if (msg.isMine) Color.White else Color.Black,
                        fontSize = 14.sp
                    )
                }
            }
            Text(
                text = if (msg.isMine) "Sent • ${msg.time}" else msg.time,
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun AttachmentView(fileName: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(0.75f),
        border = TabRowDefaults.run { androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDEE2E6)) }
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = Color(0xFF7451EB), // Warna ungu ikon dokumen figma kamu
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(fileName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text("1.2 MB", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ChatInputBar() {
    var textState by remember { mutableStateOf("") }

    Surface(shadowElevation = 8.dp, color = Color.White) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Menggunakan Icon Tambah (+) di pojok kiri input bar sesuai figma kamu
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.padding(end = 12.dp).size(28.dp)
            )

            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                placeholder = { Text("Type a message...", fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                // Perbaikan Material 3 untuk menggantikan 'outlinedTextFieldColors' yang usang
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF8F9FA),
                    unfocusedContainerColor = Color(0xFFF8F9FA),
                    focusedBorderColor = Color(0xFFDEE2E6),
                    unfocusedBorderColor = Color(0xFFDEE2E6)
                )
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Tombol kirim pesawat kertas warna ungu bulat
            Surface(
                modifier = Modifier.size(45.dp),
                shape = CircleShape,
                color = Color(0xFF8B5CF6) // Sesuai warna ungu tombol kirim figma kamu
            ) {
                IconButton(onClick = { /* Handle kirim pesan */ }) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}