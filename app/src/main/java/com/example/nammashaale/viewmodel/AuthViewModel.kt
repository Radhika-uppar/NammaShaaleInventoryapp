package com.example.nammashaale.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammashaale.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val currentUser: StateFlow<FirebaseUser?> = authRepository.currentUser
    
    var isLoading by mutableStateOf(false)
        private set
        
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = authRepository.login(email, password)
            isLoading = false
            result.onSuccess {
                onSuccess()
            }.onFailure {
                errorMessage = it.message ?: "Login failed"
            }
        }
    }

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = authRepository.register(email, password)
            isLoading = false
            result.onSuccess {
                onSuccess()
            }.onFailure {
                errorMessage = it.message ?: "Registration failed"
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun resetPassword(email: String, onEmailSent: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = authRepository.resetPassword(email)
            isLoading = false
            result.onSuccess {
                onEmailSent()
            }.onFailure {
                errorMessage = it.message ?: "Failed to send reset email"
            }
        }
    }
    
    fun clearError() {
        errorMessage = null
    }
}
