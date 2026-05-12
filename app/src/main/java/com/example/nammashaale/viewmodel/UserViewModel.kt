package com.example.nammashaale.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammashaale.data.repository.AuthRepository
import com.example.nammashaale.data.repository.UserProfile
import com.example.nammashaale.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    var fullName by mutableStateOf("")
    var schoolName by mutableStateOf("")
    var email by mutableStateOf("")
    
    var isDarkMode by mutableStateOf(false)
    var isNotificationsEnabled by mutableStateOf(true)
    var language by mutableStateOf("English (India)")
    var auditFrequency by mutableStateOf(0.5f) // Quarterly

    val categories = mutableStateListOf("Electronics", "Furniture", "Sports Equipment", "Lab Supplies")

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    fetchUserProfile(user.uid)
                } else {
                    // Reset profile when logged out
                    fullName = ""
                    schoolName = ""
                    email = ""
                }
            }
        }
    }

    private suspend fun fetchUserProfile(uid: String) {
        userRepository.getUserProfile(uid).onSuccess { profile ->
            if (profile != null) {
                fullName = profile.fullName
                schoolName = profile.schoolName
                email = profile.email
            }
        }
    }

    fun updateProfile(name: String, school: String, mail: String) {
        fullName = name
        schoolName = school
        email = mail
        
        val currentUser = authRepository.currentUser.value
        if (currentUser != null) {
            viewModelScope.launch {
                userRepository.saveUserProfile(
                    UserProfile(
                        uid = currentUser.uid,
                        fullName = name,
                        schoolName = school,
                        email = mail
                    )
                )
            }
        }
    }
    
    fun addCategory(category: String) {
        if (category.isNotBlank() && !categories.contains(category)) {
            categories.add(category)
        }
    }
}
