package com.example.educonnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.auth.LoginScreen
import com.example.educonnect.ui.auth.RegisterScreen
import com.example.educonnect.ui.boards.BoardsScreen
import com.example.educonnect.ui.boards.CreateTaskScreen
import com.example.educonnect.ui.chat.ChatScreen
import com.example.educonnect.ui.home.HomeScreen
import com.example.educonnect.ui.profile.EditProfileScreen
import com.example.educonnect.ui.profile.ProfileScreen
import com.example.educonnect.ui.theme.EduConnectTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EduConnectTheme {
                val navController = rememberNavController()

                // Inisialisasi AuthViewModel satu kali di level utama (MainActivity)
                // agar state-nya bisa dipakai bersama oleh semua Screen
                val authViewModel: AuthViewModel = viewModel()

                // Cek status login Firebase saat ini
                // Jika user sudah login sebelumnya, langsung lempar ke "home"
                val currentUser = FirebaseAuth.getInstance().currentUser
                val startDestination = if (currentUser != null) "home" else "login"

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    // 1. RUTE LAYAR LOGIN
                    composable("login") {
                        LoginScreen(
                            viewModel = authViewModel,
                            onNavigateToRegister = {
                                navController.navigate("register")
                            },
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    // Tutup gerbang login agar tidak bisa ditekan tombol 'back' kembali ke login
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 2. RUTE LAYAR REGISTER
                    composable("register") {
                        RegisterScreen(
                            viewModel = authViewModel,
                            onNavigateToLogin = {
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 3. RUTE LAYAR HOME
                    composable("home") {
                        HomeScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            onNavigateToChat = { navController.navigate("chat") },
                            onNavigateToProfile = { navController.navigate("profile") }
                        )
                    }

                    //4. RUTE CHAT
                    composable("chat") {
                        ChatScreen(navController = navController)
                    }

                    // 5. RUTE BOARDS
                    composable("boards") {
                        BoardsScreen(
                            navController = navController,
                            onNavigateToCreateTask = {
                                navController.navigate("create_task")
                            }
                        )
                    }
                    composable("create_task") {
                        CreateTaskScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // 6. RUTE PROFILE
                    composable("profile") {
                        ProfileScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            onLogoutSuccess = {
                                // Ketika klik keluar, kembalikan user ke gerbang Login
                                // dan bersihkan seluruh tumpukan halaman agar aman
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 7. RUTE EDIT PROFILE
                    composable("edit_profile") {
                        EditProfileScreen(
                            authViewModel = authViewModel,
                            onNavigateBack = {
                                navController.popBackStack() // Perintah untuk kembali ke halaman sebelumnya (Profile)
                            }
                        )
                    }
                }
            }
        }
    }
}