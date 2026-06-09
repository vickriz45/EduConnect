package com.example.educonnect.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.educonnect.components.EduBottomNavigation
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.theme.GrayText
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark
import com.example.educonnect.ui.theme.White

@Composable
fun ProfileScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    val userProfile by authViewModel.userProfile.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { EduBottomNavigation(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )
                // Tombol Edit yang berfungsi
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = PurpleMain,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.navigate("edit_profile") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier.size(90.dp).clip(CircleShape).background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = GrayText, modifier = Modifier.size(50.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = userProfile?.fullName ?: "Nama Mahasiswa",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextDark,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Row NIM
                    InfoRow(Icons.Default.School, "NIM", userProfile?.nim ?: "-")
                    // Row Email
                    InfoRow(Icons.Default.Email, "Email", userProfile?.email ?: "-")
                    // Row Class
                    InfoRow(Icons.Default.Person, "Class", userProfile?.studentClass ?: "-")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.1f), contentColor = Color.Red)
            ) {
                Text("Logout", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 10.dp)) {
        Icon(icon, contentDescription = null, tint = PurpleMain, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = GrayText)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextDark)
        }
    }
}