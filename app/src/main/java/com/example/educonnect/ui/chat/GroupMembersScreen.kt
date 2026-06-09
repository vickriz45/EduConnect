package com.example.educonnect.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.educonnect.ui.theme.GrayText
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark
import com.google.firebase.firestore.FirebaseFirestore

data class UserMemberModel(
    val fullName: String = "",
    val email: String = "",
    val profilePictureUrl: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupMembersScreen(
    onNavigateBack: () -> Unit
) {
    val firestore = remember { FirebaseFirestore.getInstance() }
    val membersList = remember { mutableStateListOf<UserMemberModel>() }

    LaunchedEffect(Unit) {
        firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    membersList.clear()
                    for (doc in snapshot.documents) {
                        val member = doc.toObject(UserMemberModel::class.java)
                        if (member != null) {
                            membersList.add(member)
                        }
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Anggota Grup",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Text(
                text = "${membersList.size} Mahasiswa Tergabung",
                fontSize = 13.sp,
                color = GrayText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Medium
            )

            if (membersList.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PurpleMain)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(membersList) { member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(PurpleMain.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!member.profilePictureUrl.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = member.profilePictureUrl,
                                        contentDescription = "Foto ${member.fullName}",
                                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = PurpleMain,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = member.fullName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextDark
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = member.email,
                                    fontSize = 12.sp,
                                    color = GrayText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}