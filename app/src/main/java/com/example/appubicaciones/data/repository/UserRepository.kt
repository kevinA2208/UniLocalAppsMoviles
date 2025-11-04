package com.example.appubicaciones.data.repository

import com.example.appubicaciones.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("users")

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /** Leer todos los usuarios **/
    suspend fun getAllUsers(): List<User> {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /** Obtener un usuario por ID **/
    suspend fun getUserById(id: String): User? {
        return try {
            val doc = collection.document(id).get().await()
            doc.toObject(User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** Actualizar usuario **/
    suspend fun updateUser(user: User): Boolean {
        return try {
            if (user.id.isNotBlank()) {
                collection.document(user.id).set(user).await()
                true
            } else false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Crear usuario (Auth + Firestore)
    suspend fun registerUser(user: User, password: String): Result<Unit> {
        return try {
            // Crear usuario en Firebase Authentication
            val authResult = auth.createUserWithEmailAndPassword(user.email, password).await()
            val firebaseUser = authResult.user ?: return Result.failure(Exception("Usuario no creado"))
            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID no encontrado"))

            val profileUpdates = userProfileChangeRequest {
                displayName = "${user.names} ${user.lastnames}"
            }
            firebaseUser.updateProfile(profileUpdates).await()

            // Guardar datos en Firestore
            firestore.collection("users")
                .document(uid)
                .set(user.copy(id = uid))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            // Iniciar sesión con Firebase Authentication
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID no encontrado"))

            // Obtener los datos del usuario desde Firestore
            val userDoc = firestore.collection("users").document(uid).get().await()
            val user = userDoc.toObject(User::class.java)
                ?: return Result.failure(Exception("Usuario no encontrado"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
