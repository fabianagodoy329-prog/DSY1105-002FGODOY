package com.example.fabiana_godoy_modoguardian.model

import android.net.Uri

// Estado inmutable de la interfaz de usuario para el registro e inspección integral.
// Representa todos los campos necesarios para la Guía 14 de ROOM, Cámara y Animaciones.
data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val fotoUri: Uri? = null,
    val modoEspecial: Boolean = false
)
