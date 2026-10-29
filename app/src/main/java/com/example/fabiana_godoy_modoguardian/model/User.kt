package com.example.fabiana_godoy_modoguardian.model

// Enumeración que define los roles del sistema Modo Guardián.
// Permite la diferenciación de vistas y permisos según el rol asignado.
enum class Role(val displayName: String, val description: String) {
    ADMIN("Administrador", "Acceso total a la configuración y gestión de usuarios"),
    SUPERVISOR("Supervisor", "Monitoreo de rondas, alertas y reportes"),
    OPERADOR("Operador", "Registro de rondas de patrulla e incidentes")
}

// Modelo de datos que representa a un usuario dentro de la aplicación.
// @param email Correo electrónico institucional del usuario.
// @param nombre Nombre completo o identificador visible del usuario.
// @param role Rol del usuario dentro del sistema (ADMIN, SUPERVISOR, OPERADOR).
data class User(
    val email: String,
    val nombre: String,
    val role: Role
)
