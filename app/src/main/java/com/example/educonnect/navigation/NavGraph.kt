package com.example.educonnect.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.educonnect.auth.LoginScreen
import com.example.educonnect.auth.RegisterScreen
import com.example.educonnect.auth.SplashScreen
import com.example.educonnect.auth.ForgotPasswordScreen
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.home.HomeScreen
import com.example.educonnect.ui.profile.ProfileScreen
import com.example.educonnect.ui.chat.ChatScreen
import com.example.educonnect.ui.boards.BoardsScreen
import com.example.educonnect.ui.profile.EditProfileScreen
import java.net.URLDecoder

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
                authViewModel = authViewModel
            )
        }

        // SATU ROUTE CHAT dengan PARAMETER OPTIONAL
        composable(
            route = "chat?sharedTitle={sharedTitle}&sharedDescription={sharedDescription}&sharedTime={sharedTime}",
            arguments = listOf(
                navArgument("sharedTitle") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("sharedDescription") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("sharedTime") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val sharedTitle = backStackEntry.arguments?.getString("sharedTitle") ?: ""
            val sharedDescription = backStackEntry.arguments?.getString("sharedDescription") ?: ""
            val sharedTime = backStackEntry.arguments?.getString("sharedTime") ?: ""

            ChatScreen(
                navController = navController,
                username = "Sumbul",
                sharedTitle = sharedTitle,
                sharedDescription = sharedDescription,
                sharedTime = sharedTime
            )
        }

        composable("boards") {
            BoardsScreen(
                navController = navController
            )
        }

        composable("edit_profile") {
            EditProfileScreen(
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel,
                onLogout = {
                    authViewModel.logout {
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                }
            )
        }
    }
}