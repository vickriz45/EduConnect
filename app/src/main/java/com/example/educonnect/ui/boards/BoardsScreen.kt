package com.example.educonnect.ui.boards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.net.URLEncoder

@Composable
fun BoardsScreen(
    navController: NavHostController
) {
    Scaffold(
        bottomBar = { EduBottomNavigation(navController = navController) }
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

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(boardList) { board ->
                    InteractiveBoardCard(
                        board = board,
                        onShareClick = { selectedBoard ->
                            val encodedTitle = java.net.URLEncoder.encode(selectedBoard.title, "UTF-8")
                            val encodedDescription = java.net.URLEncoder.encode(selectedBoard.description, "UTF-8")
                            val encodedTime = java.net.URLEncoder.encode(selectedBoard.time, "UTF-8")
                            navController.navigate("chat?sharedTitle=$encodedTitle&sharedDescription=$encodedDescription&sharedTime=$encodedTime")
                        }
                    )
                }
            }
        }
    }
}

data class InteractiveBoardModel(
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val imageColor: Color
)

val boardList = listOf(
    InteractiveBoardModel(
        1, "Campus News",
        "Disenalis PNN 2026 - HUT PNN akan merayakan dengan mengundang artis internasional",
        "5h ago", Icons.Default.School, Color(0xFF4CAF50)
    ),
    InteractiveBoardModel(
        2, "Academic Update",
        "Lulusan 2028 Dipastikan Wisuda - Mulai persiapan dari sekarang!",
        "Yesterday", Icons.Default.Description, Color(0xFF2196F3)
    ),
    InteractiveBoardModel(
        3, "Workshop",
        "Advanced Software Development - Bangun aplikasi dari nol hingga deployment",
        "2 days ago", Icons.Default.CalendarToday, Color(0xFFFF9800)
    ),
    InteractiveBoardModel(
        4, "Campus Life",
        "PMN Peduli - Penggalangan dana untuk korban bencana alam",
        "Yesterday", Icons.Default.People, Color(0xFF9C27B0)
    )
)

@Composable
fun InteractiveBoardCard(
    board: InteractiveBoardModel,
    onShareClick: (InteractiveBoardModel) -> Unit
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
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = board.imageColor.copy(alpha = 0.1f)
                ) {
                    Icon(
                        board.icon,
                        contentDescription = null,
                        tint = board.imageColor,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        board.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        board.time,
                        fontSize = 12.sp,
                        color = GrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isExpanded) board.description else board.description.take(100) + "...",
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { onShareClick(board) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        tint = GrayText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}