package com.example.educonnect.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun isDeviceOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
data class ChatMessage(
    val name: String = "",
    val message: String = "",
    val time: String = "",
    val senderId: String = ""
)
@Composable
fun ChatScreen(
    navController: NavHostController,
    username: String = "",
    sharedTitle: String = "",
    sharedDescription: String = "",
    sharedTime: String = ""
) {
    var messageText by remember { mutableStateOf("") }
    var hasAutoFilled by remember { mutableStateOf(false) }

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var memberCount by remember { mutableStateOf(0) }

    val firestore = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUserId = auth.currentUser?.uid ?: ""

    val context = androidx.compose.ui.platform.LocalContext.current
    var isOnline by remember { mutableStateOf(isDeviceOnline(context)) }

    LaunchedEffect(Unit) {
        while(true) {
            isOnline = isDeviceOnline(context)
            kotlinx.coroutines.delay(3000) // Cek status internet setiap 3 detik sekali
        }
    }

    LaunchedEffect(Unit) {
        firestore.collection("group_chats")
            .orderBy("timestamp", Query.Direction.ASCENDING)
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PurpleMain.copy(alpha = 0.1f))
                    .clickable{navController.navigate("group_members")}
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

            if (!isOnline) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE57373)) // Warna merah soft/pudar agar estetik
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚠️ Mode Offline • Menunggu Jaringan...",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                reverseLayout = false
            ) {
                items(messages) { message ->
                    val isOwn = message.senderId == currentUserId
                    ChatBubble(message = message, isOwnMessage = isOwn)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

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
                            val currentTimeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                            val dataPesan = hashMapOf(
                                "name" to username,
                                "message" to messageText,
                                "time" to currentTimeStr,
                                "senderId" to currentUserId,
                                "timestamp" to System.currentTimeMillis()
                            )

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
                .widthIn(max = 280.dp)
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
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}