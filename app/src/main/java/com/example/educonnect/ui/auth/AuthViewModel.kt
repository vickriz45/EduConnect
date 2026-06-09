package com.example.educonnect.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educonnect.data.local.UserEntity
import com.example.educonnect.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    val userProfile: StateFlow<UserEntity?> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _loginStatus = MutableStateFlow<String?>(null)
    val loginStatus: StateFlow<String?> = _loginStatus

    // Status untuk operasi update profile: null = idle, "success" = berhasil, pesan lain = error
    private val _updateStatus = MutableStateFlow<String?>(null)
    val updateStatus: StateFlow<String?> = _updateStatus

    fun login(nim: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loginStatus.value = null
            repository.loginUser(
                nim = nim,
                password = password,
                onSuccess = { onSuccess() },
                onFailure = { errorMessage -> _loginStatus.value = errorMessage }
            )
        }
    }

    fun register(
        nim: String,
        fullName: String,
        studentClass: String,
        email: String,
        password: String,
        onRegisterSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val newUser = UserEntity(
                nim = nim,
                fullName = fullName,
                studentClass = studentClass,
                email = email,
                prodi = "TRPL",
                password = password
            )
            repository.registerUser(
                user = newUser,
                onSuccess = { onRegisterSuccess() },
                onFailure = { errorMessage -> _loginStatus.value = errorMessage }
            )
        }
    }

    fun updateProfile(email: String) {
        viewModelScope.launch {
            _updateStatus.value = null
            repository.updateProfile(
                email = email,
                onSuccess = { _updateStatus.value = "success" },
                onFailure = { error -> _updateStatus.value = error }
            )
        }
    }

    fun resetUpdateStatus() {
        _updateStatus.value = null
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.logoutUser { onLogoutSuccess() }
        }
    }
}