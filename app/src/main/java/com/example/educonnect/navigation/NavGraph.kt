package com.example.educonnect.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.educonnect.auth.LoginScreen
import com.example.educonnect.auth.RegisterScreen
import com.example.educonnect.auth.SplashScreen
import com.example.educonnect.auth.ForgotPasswordScreen
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.home.HomeScreen
import com.example.educonnect.ui.profile.ProfileScreen
import com.example.educonnect.ui.chat.ChatScreen
import com.example.educonnect.ui.boards.BoardsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToForgotPassword = { navController.navigate("forgot_password") },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                username = "Sumbul"
            )
        }

        composable("chat") {
            ChatScreen(
                navController = navController,
                username = "Sumbul"
            )
        }

        composable("boards") {
            BoardsScreen(
                navController = navController
            )
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}