package com.example.fabiana_godoy_modoguardian.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Data Access Object (DAO) para la gestión de usuarios en ROOM.
@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    fun getUsuariosFlow(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getPorId(id: Int): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity)

    @Delete
    suspend fun eliminar(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE correo LIKE '%' || :query || '%'")
    fun buscarPorCorreo(query: String): Flow<List<UsuarioEntity>>
}
