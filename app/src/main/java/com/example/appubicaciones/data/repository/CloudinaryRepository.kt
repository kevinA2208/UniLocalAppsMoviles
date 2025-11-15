package com.example.appubicaciones.data.repository

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.Result
import kotlin.coroutines.resume

class CloudinaryRepository {

    suspend fun uploadImage(uri: Uri, context: Context, uploadPreset: String): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            val requestId = MediaManager.get()
                .upload(uri)
                .unsigned(uploadPreset)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {}
                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val url = resultData["secure_url"] as? String // O "url"
                        if (url != null) {
                            continuation.resume(Result.success(url))
                        } else {
                            continuation.resume(Result.failure(Exception("URL de Cloudinary no encontrada.")))
                        }
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        continuation.resume(Result.failure(Exception(error.description)))
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {}
                })
                .dispatch(context) // Necesitamos el Context para la subida

            // Manejar cancelación de la corrutina
            continuation.invokeOnCancellation {
                try {
                    MediaManager.get().cancelRequest(requestId)
                } catch (_: Exception) {}
            }
        }
    }

    // Sube una lista de imágenes en paralelo y devuelve la lista de URLs
    suspend fun uploadImages(uris: List<Uri>, context: Context, uploadPreset: String): Result<List<String>> {
        return try {
            coroutineScope {
                val uploadTasks = uris.map { uri ->
                    async { uploadImage(uri, context, uploadPreset) } // Lanza todas las subidas
                }

                val results = uploadTasks.awaitAll() // Espera a que todas terminen

                // Verifica si alguna falló
                val firstError = results.firstOrNull { it.isFailure }
                if (firstError != null) {
                    return@coroutineScope Result.failure(firstError.exceptionOrNull() ?: Exception("Error de subida desconocido"))
                }

                // Todas exitosas, extrae las URLs
                val urls = results.mapNotNull { it.getOrNull() }
                Result.success(urls)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}