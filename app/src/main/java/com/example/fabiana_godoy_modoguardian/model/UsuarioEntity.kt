package com.example.fabiana_godoy_modoguardian.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val correo: String,
    val direccion: String,
    val modoEspecial: Boolean,
    val fotoPath: String?,
    val fechaRegistro: Date = Date()
)