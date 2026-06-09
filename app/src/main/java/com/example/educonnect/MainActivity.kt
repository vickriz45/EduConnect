package com.example.educonnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.educonnect.data.local.AppDatabase
import com.example.educonnect.data.repository.AuthRepository
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.theme.EduConnectTheme
import com.example.educonnect.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = AuthRepository(db.userDAO())

        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                    return AuthViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val authViewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            EduConnectTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}