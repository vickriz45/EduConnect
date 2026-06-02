package com.example.educonnect.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.educonnect.components.EduBottomNavigation
import com.example.educonnect.components.MenuCard
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.theme.GrayText
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel, // Injeksi AuthViewModel untuk ambil data Firebase
    onNavigateToChat: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    // State untuk menyimpan nama pengguna asli dari Firebase Firestore
    var currentUsername by remember { mutableStateOf("Memuat...") }

    // Ambil data nama asli saat HomeScreen pertama kali dibuka
    LaunchedEffect(Unit) {
        authViewModel.getCurrentUser { name ->
            currentUsername = name
        }
    }

    Scaffold(
        bottomBar = { EduBottomNavigation(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)) // Background abu-abu terang sesuai gambar
                .padding(padding)
                .padding(24.dp)
        ) {
            /* 1. HEADER (EduConnect + Logo PNM & TRPL) */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.School,
                        contentDescription = null,
                        tint = Color(0xFF1A1A40), // Sesuai warna gelap di gambar
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "EduConnect",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color(0xFF1A1A40)
                    )
                }

                // Tempat Logo PNM dan TRPL bersebelahan di pojok kanan
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Catatan: Ganti R.drawable.logo_pnm & logo_trpl sesuai dengan nama file aset gambar kamu
                    Box(modifier = Modifier.size(28.dp)) {
                        // Jika belum ada aset gambar, bisa pakai placeholder Box sementara:
                         Box(modifier = Modifier.fillMaxSize().background(Color.LightGray, CircleShape))
                        /* Image(
                            painter = painterResource(id = R.drawable.logo_pnm),
                            contentDescription = "Logo PNM"
                        ) */
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(45.dp, 20.dp)) {
                        /* Image(
                            painter = painterResource(id = R.drawable.logo_trpl),
                            contentDescription = "Logo TRPL"
                        ) */
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* 2. WELCOME TEXT (Dinamis dari Firebase) */
            Text(
                "Selamat Datang $currentUsername,",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFF1A1A40)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "di EduConnect. Ini adalah aplikasi antar mahasiswa Politeknik Negeri Madiun, khusus Program Studi Teknologi Rekayasa Perangkat Lunak.",
                fontSize = 14.sp,
                color = Color(0xFF6C757D), // Warna text muted
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            /* 3. MAIN BANNER (Card TRPL Software Engineering dengan wave effect halus) */
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0D36)) // Warna navy super gelap sesuai gambar
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier.size(70.dp),
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Default.Terminal, // Ikon code/terminal
                                contentDescription = null,
                                tint = Color(0xFF0D0D36),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "TRPL",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Text(
                        "SOFTWARE ENGINEERING",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* 4. MENU CARD (Diskusi & Akun Saya) */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Diskusi",
                    desc = "Tanya jawab seputar koding.",
                    icon = Icons.Default.ChatBubbleOutline,
                    onClick = onNavigateToChat
                )
                MenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Akun Saya",
                    desc = "Atur profil & preferensi belajar.",
                    icon = Icons.Default.PersonOutline,
                    onClick = onNavigateToProfile
                )
            }
        }
    }
}