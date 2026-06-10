package com.example.educonnect.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.educonnect.ui.auth.LoginScreen
import com.example.educonnect.ui.auth.RegisterScreen
import com.example.educonnect.ui.auth.SplashScreen
import com.example.educonnect.ui.auth.ForgotPasswordScreen
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.home.HomeScreen
import com.example.educonnect.ui.profile.ProfileScreen
import com.example.educonnect.ui.chat.ChatScreen
import com.example.educonnect.ui.boards.BoardsScreen
import com.example.educonnect.ui.chat.GroupMembersScreen
import com.example.educonnect.ui.profile.EditProfileScreen
import java.net.URLDecoder

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                authViewModel = authViewModel,
                onSplashComplete = {
                    val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

                    if (currentUser != null) {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
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
                authViewModel = authViewModel,
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

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
            val userProfileState = authViewModel.userProfile.collectAsState(initial = null)
            val currentUsername = userProfileState.value?.fullName ?: "Mahasiswa"

            ChatScreen(
                navController = navController,
                username = currentUsername,
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
        composable("group_members") {
            GroupMembersScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}