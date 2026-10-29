package com.example.fabiana_godoy_modoguardian.model

// Estado inmutable de la interfaz de usuario para la pantalla de inicio de sesión.
// Sigue el patrón Unidirectional Data Flow (UDF) de Jetpack Compose.
// @param email Valor del campo de entrada de correo electrónico.
// @param password Valor del campo de entrada de contraseña.
// @param isLoading Indica si hay un proceso de autenticación en marcha.
// @param errores Contiene los mensajes de validación para los campos.
// @param currentUser Usuario autenticado tras un inicio de sesión exitoso.
// @param isLoggedIn Indica si la sesión está actualmente activa.
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errores: LoginErrores = LoginErrores(),
    val currentUser: User? = null,
    val isLoggedIn: Boolean = false
) {
    // Propiedad derivada que evalúa si el botón de login debe estar habilitado.
    // Requiere que email y contraseña no estén vacíos, que no haya carga activa y sin errores.
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading && !errores.hasErrors
}
