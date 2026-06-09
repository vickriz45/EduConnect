package com.example.educonnect.ui.boards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.School
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

data class InteractiveBoardModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val time: String = ""
)

@Composable
fun BoardsScreen(
    navController: NavHostController,
    username: String = "Mahasiswa PNM"
) {
    val firestore = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUserId = auth.currentUser?.uid ?: ""

    val boardsList = remember { mutableStateListOf<InteractiveBoardModel>() }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        firestore.collection("boards")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    boardsList.clear()
                    for (doc in snapshot.documents) {
                        val item = doc.toObject(InteractiveBoardModel::class.java)
                        if (item != null) {
                            boardsList.add(item.copy(id = doc.id))
                        }
                    }
                }
            }
    }

    Scaffold(
        bottomBar = { EduBottomNavigation(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = PurpleMain,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Pengumuman")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues)
        ) {
            Text(
                "Information Boards",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = TextDark,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                "Stay updated with the latest campus announcements",
                fontSize = 14.sp,
                color = GrayText,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (boardsList.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Belum ada pengumuman kampus.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(boardsList) { board ->
                        InteractiveBoardCard(
                            board = board,
                            onDeleteClick = { selectedBoard ->
                                firestore.collection("boards").document(selectedBoard.id).delete()
                            }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AddBoardDialog(
                onDismiss = { showDialog = false },
                onSave = { title, desc, deadline ->
                    val timestampSekarang = System.currentTimeMillis()

                    val dataTugas = hashMapOf(
                        "title" to title,
                        "description" to desc,
                        "time" to deadline,
                        "timestamp" to timestampSekarang
                    )

                    val currentTimeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                    val teksPesanGrup = "📢 *PENGUMUMAN BARU: $title*\n\n📝 Detail: $desc\n\n⏰ Waktu/Tenggat: $deadline"

                    val dataPesanChat = hashMapOf(
                        "name" to username,
                        "message" to teksPesanGrup,
                        "time" to currentTimeStr,
                        "senderId" to currentUserId,
                        "timestamp" to timestampSekarang
                    )

                    firestore.collection("boards").add(dataTugas)
                    firestore.collection("group_chats").add(dataPesanChat)

                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun InteractiveBoardCard(
    board: InteractiveBoardModel,
    onDeleteClick: (InteractiveBoardModel) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = PurpleMain.copy(alpha = 0.1f)
                ) {
                    Icon(
                        Icons.Default.School,
                        contentDescription = null,
                        tint = PurpleMain,
                        modifier = Modifier.fillMaxSize().padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(board.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Tenggat/Waktu: ${board.time}", fontSize = 12.sp, color = GrayText)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isExpanded) board.description else if (board.description.length > 100) board.description.take(100) + "..." else board.description,
                fontSize = 14.sp,
                color = GrayText,
                lineHeight = 20.sp
            )

            if (board.description.length > 100) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isExpanded) "Lebih sedikit" else "Baca selengkapnya",
                    fontSize = 12.sp,
                    color = PurpleMain,
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onDeleteClick(board) }, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Hapus",
                        tint = Color.Red.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddBoardDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Tambah Pengumuman Baru", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul Pengumuman") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Isi Deskripsi") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = deadline, onValueChange = { deadline = it }, label = { Text("Waktu / Deadline") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank() && desc.isNotBlank()) onSave(title, desc, deadline) },
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
            ) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) { Text("Batal", color = Color.Gray) }
        }
    )
}