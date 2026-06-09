package com.example.educonnect.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.educonnect.components.EduBottomNavigation
import com.example.educonnect.ui.theme.GrayText
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark
import com.example.educonnect.ui.theme.White
import com.google.firebase.auth.FirebaseAuth // Import Auth
import com.google.firebase.firestore.FirebaseFirestore // Import Firestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val name: String = "",
    val message: String = "",
    val time: String = "",
    val senderId: String = "" // Tambahkan ini untuk melacak UID pengirim di Firebase
)
@Composable
fun ChatScreen(
    navController: NavHostController,
    username: String = "Mahasiswa PNM", // Default username jika dari auth belum ke-passing
    sharedTitle: String = "",
    sharedDescription: String = "",
    sharedTime: String = ""
) {
    var messageText by remember { mutableStateOf("") }
    var hasAutoFilled by remember { mutableStateOf(false) }

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var memberCount by remember { mutableStateOf(0) }

    // Inisialisasi Firebase Instansi
    val firestore = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUserId = auth.currentUser?.uid ?: ""

    // 1. MENDENGARKAN DATABASE FIRESTORE SECARA REAL-TIME
    LaunchedEffect(Unit) {
        firestore.collection("group_chats")
            .orderBy("timestamp", Query.Direction.ASCENDING) // Mengurutkan chat dari yang terlama ke terbaru
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                if (snapshot != null) {
                    messages.clear()
                    for (document in snapshot.documents) {
                        val msg = document.toObject(ChatMessage::class.java)
                        if (msg != null) {
                            messages.add(msg)
                        }
                    }
                }
            }
        firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    memberCount = snapshot.size()
                }
            }
    }

    // Auto fill message text ketika ada data kiriman share dari Boards Page
    LaunchedEffect(sharedTitle, sharedDescription, sharedTime) {
        if (sharedTitle.isNotEmpty() && sharedDescription.isNotEmpty() && !hasAutoFilled) {
            messageText = "📢 ${sharedTitle}\n\n${sharedDescription}\n\nWaktu: ${sharedTime}"
            hasAutoFilled = true
        }
    }

    Scaffold(
        bottomBar = { EduBottomNavigation(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues)
        ) {
            // Header Group Diskusi
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PurpleMain.copy(alpha = 0.1f))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(PurpleMain.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Group,
                        contentDescription = null,
                        tint = PurpleMain,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "TRPL - Kelas A",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextDark
                    )
                    Text(
                        "Grup Diskusi • $memberCount Anggota",
                        fontSize = 12.sp,
                        color = GrayText
                    )
                }
            }

            // Chat messages list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                reverseLayout = false
            ) {
                items(messages) { message ->
                    // Cek kepemilikan pesan berdasarkan UID Firebase Auth yang sedang login
                    val isOwn = message.senderId == currentUserId
                    ChatBubble(message = message, isOwnMessage = isOwn)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Input message area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    placeholder = { Text("Ketik pesan...", fontSize = 14.sp) },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleMain,
                        unfocusedBorderColor = GrayText.copy(alpha = 0.3f)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            // FORMAT WAKTU (Jam:Menit)
                            val currentTimeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                            // DATA DATA YANG DIKIRIM KE CLOUD DATABASE FIRESTORE
                            val dataPesan = hashMapOf(
                                "name" to username,
                                "message" to messageText,
                                "time" to currentTimeStr,
                                "senderId" to currentUserId,
                                "timestamp" to System.currentTimeMillis() // Dipakai untuk sorting data di query atas
                            )

                            // Tembak langsung ke dokumen Firestore
                            firestore.collection("group_chats").add(dataPesan)

                            messageText = ""
                            hasAutoFilled = false
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(PurpleMain, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage, isOwnMessage: Boolean) {
    val bubbleColor = if (isOwnMessage) PurpleMain else Color(0xFFE4E6EB)
    val textColor = if (isOwnMessage) Color.White else TextDark

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp) // Menggunakan widthIn agar balon mengecil jika chat pendek
                .background(bubbleColor, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            if (!isOwnMessage) {
                Text(
                    message.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = PurpleMain
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                message.message,
                fontSize = 14.sp,
                color = textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                message.time,
                fontSize = 10.sp,
                color = if (isOwnMessage) Color.White.copy(alpha = 0.7f) else GrayText,
                modifier = Modifier.align(Alignment.End) // Taruh keterangan waktu di pojok kanan bawah balon chat
            )
        }
    }
}