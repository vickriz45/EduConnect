package com.example.educonnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.educonnect.data.local.AppDatabase
import com.example.educonnect.data.repository.AuthRepository
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.theme.EduConnectTheme
import com.example.educonnect.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "educonnect-db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val repository = AuthRepository(db.userDAO())
        val authViewModel = AuthViewModel(repository)

        enableEdgeToEdge()
        setContent {
            EduConnectTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}