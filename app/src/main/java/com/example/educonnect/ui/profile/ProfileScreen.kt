package com.example.educonnect.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.educonnect.components.EduBottomNavigation
import com.example.educonnect.ui.auth.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onLogoutSuccess: () -> Unit
) {
    // State nama dan detail mahasiswa (Nanti di-load dinamis dari Firebase Firestore)
    var studentName by remember { mutableStateOf("Muhammad Sumbul") }
    var studentRole by remember { mutableStateOf("Software Engineer | Semester 4") }
    var aboutMeText by remember {
        mutableStateOf(
            "Sangat tertarik dan antusias di bidang desain UI/UX serta pengembangan full-stack. " +
                    "Saat ini memimpin proyek web untuk Tech Society. Terbuka untuk berkolaborasi dalam " +
                    "pengembangan alat pendidikan sumber terbuka (open-source)"
        )
    }

    // Ambil data nama asli dari Firebase saat halaman dibuka
    LaunchedEffect(Unit) {
        authViewModel.getCurrentUser { name ->
            if (name.isNotEmpty() && name != "Mahasiswa") {
                studentName = name
            }
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* 1. HEADER (EduConnect + Mini Logo Placeholders) */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.School,
                        contentDescription = null,
                        tint = Color(0xFF1A1A40),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "EduConnect",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF1A1A40)
                    )
                }

                // Tempat Logo PNM dan TRPL bersebelahan
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(24.dp).background(Color.LightGray, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(40.dp, 18.dp).background(Color.LightGray))
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            /* 2. FOTO PROFIL (Lingkaran Besar dengan Border Ungu) */
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .border(4.dp, Color(0xFF9333EA), CircleShape) // Border ungu sesuai gambar figma
                    .padding(6.dp)
                    .background(Color(0xFFE9ECEF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto Profil",
                    tint = Color.Gray,
                    modifier = Modifier.size(90.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            /* 3. NAMA DAN DETAIL MAHASISWA */
            Text(
                text = studentName,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFF1A1A40),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = studentRole,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            /* 4. BUTTON EDIT PROFILE */
            Button(
                onClick = { navController.navigate("edit_profile") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9333EA)), // Warna ungu figma
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* 5. CARD ABOUT ME */
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "About Me",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = aboutMeText,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Dorong tombol keluar ke posisi paling bawah

            /* 6. BUTTON KELUAR (LOGOUT) */
            Button(
                onClick = {
                    // Panggil fungsi logout dari Firebase nanti melalui ViewModel
                    // authViewModel.logout()
                    onLogoutSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9333EA)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Keluar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}