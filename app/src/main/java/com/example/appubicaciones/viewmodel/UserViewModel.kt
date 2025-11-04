package com.example.appubicaciones.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appubicaciones.data.model.User
import com.example.appubicaciones.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    // Cargar todos los usuarios
    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _users.value = repo.getAllUsers()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Actualizar usuario
    fun updateUser(user: User, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repo.updateUser(user)
            onResult(success)
            if (success) loadUsers()
        }
    }

    fun registerUser(user: User, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.registerUser(user, password)
            _isLoading.value = false

            if (result.isSuccess) {
                _isSuccess.value = true
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false

            val result = repo.loginUser(email, password)

            _isLoading.value = false
            if (result.isSuccess) {
                _isSuccess.value = true
                _users.value = listOf(result.getOrNull()!!)
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}
