package com.example.educonnect.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.educonnect.auth.LoginScreen
import com.example.educonnect.auth.RegisterScreen
import com.example.educonnect.ui.auth.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "login" //aplikasi mulai dari login
    ) {
        // Halaman login
        composable("Login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { navController.navigate("Home") }
            )
        }

        // Halaman register
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() } // Kembali ke login
            )
        }

        // Halaman home (Dummy)
        composable("home") {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Selamat datang di Beranda EduConnect !")
            }
        }
    }
}