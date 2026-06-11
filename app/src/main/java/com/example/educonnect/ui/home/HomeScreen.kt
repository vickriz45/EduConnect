package com.example.educonnect.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.educonnect.R
import com.example.educonnect.components.EduBottomNavigation
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.boards.InteractiveBoardModel
import com.example.educonnect.ui.theme.GrayText
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val userProfile by authViewModel.userProfile.collectAsStateWithLifecycle()
    val username = userProfile?.fullName ?: ""

    val firestore = remember { FirebaseFirestore.getInstance() }
    val latestAnnouncements = remember { mutableStateListOf<InteractiveBoardModel>() }

    LaunchedEffect(Unit) {
        firestore.collection("boards")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(3)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    latestAnnouncements.clear()
                    for (doc in snapshot.documents) {
                        val item = doc.toObject(InteractiveBoardModel::class.java)
                        if (item != null) {
                            latestAnnouncements.add(item.copy(id = doc.id))
                        }
                    }
                }
            }
    }

    Scaffold(
        bottomBar = { EduBottomNavigation(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.School,
                        contentDescription = null,
                        tint = PurpleMain,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "EduConnect",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextDark
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.logo_poltek),
                    contentDescription = "Logo POLTEKxTRPL",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Selamat Datang $username,",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextDark
            )
            Text(
                text = "di EduConnect. Aplikasi antar mahasiswa Politeknik Negeri Madiun",
                fontSize = 12.sp,
                color = GrayText,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A40))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.9f)
                    ) {
                        Icon(
                            Icons.Default.Terminal,
                            contentDescription = null,
                            tint = Color(0xFF1A1A40),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "TRPL",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "SOFTWARE ENGINEERING",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Pengumuman Terbaru",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (latestAnnouncements.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("Belum ada pengumuman kelas saat ini.", fontSize = 13.sp, color = GrayText)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    latestAnnouncements.forEach { announcement ->
                        HomeAnnouncementCard(announcement = announcement)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeAnnouncementCard(announcement: InteractiveBoardModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(10.dp),
                color = PurpleMain.copy(alpha = 0.1f)
            ) {
                Icon(
                    Icons.Default.Campaign,
                    contentDescription = null,
                    tint = PurpleMain,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = announcement.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = announcement.description,
                    fontSize = 12.sp,
                    color = GrayText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Waktu: ${announcement.time}",
                    fontSize = 10.sp,
                    color = PurpleMain,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}