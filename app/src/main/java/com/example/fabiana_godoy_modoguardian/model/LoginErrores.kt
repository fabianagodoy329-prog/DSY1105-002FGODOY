package com.example.fabiana_godoy_modoguardian.model

// Modelo para gestionar el estado de los mensajes de error en el formulario de Login.
// Permite mostrar mensajes contextuales en el supportingText de los campos de texto.
data class LoginErrores(
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null
) {
    // Indica si el formulario tiene algún error de validación activo.
    val hasErrors: Boolean
        get() = emailError != null || passwordError != null || generalError != null
}
