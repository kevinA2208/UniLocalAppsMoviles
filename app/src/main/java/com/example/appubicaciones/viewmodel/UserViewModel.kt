package com.example.appubicaciones.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appubicaciones.data.model.User
import com.example.appubicaciones.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repo = UserRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

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

    // Obtener el usuario actualmente autenticado
    fun loadCurrentUser() {
        viewModelScope.launch {
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                val user = repo.getUserById(firebaseUser.uid)
                _currentUser.value = user
            } else {
                _currentUser.value = null
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

    // Registrar usuario nuevo
    fun registerUser(user: User, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.registerUser(user, password)
            _isLoading.value = false

            if (result.isSuccess) {
                _isSuccess.value = true
                loadCurrentUser()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    // Iniciar sesión
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false

            val result = repo.loginUser(email, password)

            _isLoading.value = false
            if (result.isSuccess) {
                val user = result.getOrNull()
                _isSuccess.value = true
                _currentUser.value = user
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    // Cerrar sesión
    fun logoutUser() {
        auth.signOut()
        _currentUser.value = null
        _isSuccess.value = false
    }
}
