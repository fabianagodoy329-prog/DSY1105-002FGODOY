package com.example.fabiana_godoy_modoguardian.repository

import com.example.fabiana_godoy_modoguardian.model.Role
import com.example.fabiana_godoy_modoguardian.model.User
import kotlinx.coroutines.delay

// Repositorio encargado de la autenticación de usuarios.
// Administra las credenciales sintéticas de prueba obligatorias para el caso Modo Guardián:
// - admin@guardian.test / 123456 -> Rol ADMIN
// - supervisor@guardian.test / 123456 -> Rol SUPERVISOR
// - operador@guardian.test / 123456 -> Rol OPERADOR
class AuthRepository {

    // Lista de usuarios sintéticos de prueba obligatorios según requerimiento
    private val usuariosPrueba = listOf(
        User(
            email = "admin@guardian.test",
            nombre = "Administrador Modo Guardián",
            role = Role.ADMIN
        ),
        User(
            email = "supervisor@guardian.test",
            nombre = "Supervisor de Turno Central",
            role = Role.SUPERVISOR
        ),
        User(
            email = "operador@guardian.test",
            nombre = "Operador de Terreno Rondas",
            role = Role.OPERADOR
        )
    )

    // Contraseña por defecto solicitada para las credenciales de prueba
    private val passwordPrueba = "123456"

    // Realiza el proceso de login simulando una llamada asíncrona de red/local.
    // Incluye manejo de retardo de carga y control de excepciones.
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Simulación de latencia de red (1 segundo)
            delay(1000)

            // Limpieza de espacios en el email
            val cleanEmail = email.trim().lowercase()

            // Búsqueda del usuario correspondiente en la fuente de datos sintética
            val usuarioEncontrado = usuariosPrueba.find { it.email == cleanEmail }

            if (usuarioEncontrado != null && password == passwordPrueba) {
                Result.success(usuarioEncontrado)
            } else if (usuarioEncontrado == null) {
                Result.failure(Exception("El usuario no existe en el sistema."))
            } else {
                Result.failure(Exception("Contraseña incorrecta. Intente de nuevo."))
            }
        } catch (e: Exception) {
            // Captura defensiva de errores inesperados para garantizar estabilidad
            Result.failure(Exception("Error al conectar con el servidor: ${e.message}"))
        }
    }
}
