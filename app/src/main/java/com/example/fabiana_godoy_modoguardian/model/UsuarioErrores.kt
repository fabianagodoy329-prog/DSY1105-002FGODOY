package com.example.fabiana_godoy_modoguardian.model

// Modelo para gestionar los mensajes de error en el formulario de registro integral.
// Contiene campos nulos que se pueblan al ejecutar la validación del formulario.
data class UsuarioErrores(
    val nombreError: String? = null,
    val correoError: String? = null,
    val claveError: String? = null,
    val direccionError: String? = null,
    val terminosError: String? = null,
    val fotoError: String? = null
) {
    // Evalúa si existe al menos un mensaje de error activo.
    val hasErrors: Boolean
        get() = nombreError != null ||
                correoError != null ||
                claveError != null ||
                direccionError != null ||
                terminosError != null ||
                fotoError != null
}
