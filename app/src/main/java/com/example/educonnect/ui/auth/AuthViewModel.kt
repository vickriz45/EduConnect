package com.example.educonnect.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educonnect.data.local.UserEntity
import com.example.educonnect.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginStatus = MutableStateFlow<String?>(null)
    val loginStatus: StateFlow<String?> = _loginStatus

    fun login(nim: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserProfile()
            if (user != null && user.nim == nim && user.password == password) {
                _loginStatus.value = null
                onSuccess()
            } else {
                _loginStatus.value = "NIM atau password salah"
            }
        }
    }

    fun register(
        nim: String,
        fullName: String,
        studentClass: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            val user = UserEntity(
                nim = nim,
                fullName = fullName,
                studentClass = studentClass,
                email = email,
                prodi = "TRPL",
                password = password
            )
            repository.insertUser(user)
        }
    }
}