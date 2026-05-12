package com.example.educonnect.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educonnect.data.local.UserEntity
import com.example.educonnect.data.repository.AuthRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class AuthViewModel(private val repository : AuthRepository) : ViewModel() {
    //Fungsi untuk memproses pendaftaran
    fun register(
        nim: String,
        fullName: String,
        studentClass: String,
        email: String
    ) {
        viewModelScope.launch {
            val newUser = UserEntity(
                nim = nim,
                fullName = fullName,
                studentClass = studentClass,
                email = email,
                prodi = "TRPL"
            )
            repository.registerUser(newUser)
            //Di sini bisa tambahkan logika navigasi ke home setelah berhasil
        }
    }

    var loginStatus by mutableStateOf<String?>(null)
        private set
    fun login(nim: String, onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = repository.getSavedUser()
            if(user != null && user.nim == nim) {
                loginStatus = "Login Berhasil !"
                onLoginSuccess()
            } else {
                loginStatus
            }
        }
    }
}