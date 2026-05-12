package com.example.educonnect.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.educonnect.components.EduBottomNavigation
import com.example.educonnect.components.MenuCard
import com.example.educonnect.ui.theme.GrayText
import com.example.educonnect.ui.theme.PurpleMain
import com.example.educonnect.ui.theme.TextDark

@Composable
fun HomeScreen(
    navController: NavHostController,
    username: String = "Sumbul",
    onNavigateToChat: () -> Unit,
    onNavigateToProfile: () ->Unit
){
    Scaffold(
        bottomBar = { EduBottomNavigation(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(padding)
                .padding(20.dp)
        ) {
            /*1. Header*/
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(Icons.Default.School, contentDescription = null, tint = PurpleMain)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "EduConnect",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextDark
                    )
                }
                // Placeholder untuk logo PNM dan TRPL
                Row {
                    Box(modifier = Modifier.size(30.dp).background(Color.LightGray, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.size(30.dp).background(Color.LightGray, CircleShape))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            /* 2. Welcome Txt*/
            Text(
                "Selamat Datang $username,",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextDark
            )
            Text(
                "di EduConnect. Ini adalah aplikasi antar mahasiswa Politeknik Negeri Madiun",
                fontSize = 14.sp,
                color = GrayText,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

//            3. MAIN BANNER (Card)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A40))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.9f)
                    ) {
                        Icon(
                            Icons.Default.Terminal, // Ikon mirip code/terminal
                            contentDescription = null,
                            tint = Color(0xFF1A1A40),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("TRPL", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("SOFTWARE ENGINEERING", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }
                }
            Spacer(modifier = Modifier.height(24.dp))

//            4. Menu Card
            Row(modifier = Modifier.fillMaxWidth()) {
                MenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Diskusi",
                    desc = "Tanya jawab seputar koding.",
                    icon = Icons.Default.ChatBubbleOutline,
                    onClick = onNavigateToChat
                )
                Spacer(modifier = Modifier.width(16.dp))
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