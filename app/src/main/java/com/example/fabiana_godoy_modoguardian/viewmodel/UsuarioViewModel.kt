package com.example.fabiana_godoy_modoguardian.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabiana_godoy_modoguardian.db.AppDatabase
import com.example.fabiana_godoy_modoguardian.db.UsuarioEntity
import com.example.fabiana_godoy_modoguardian.model.UsuarioErrores
import com.example.fabiana_godoy_modoguardian.model.UsuarioUiState
import com.example.fabiana_godoy_modoguardian.repository.PreferencesManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

// ViewModel Integral que gestiona la lógica de negocio, persistencia en ROOM, DataStore Preferences
// y validaciones reactivas según los requerimientos de la Guía 14.
class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val usuarioDao = db.usuarioDao()
    private val preferencesManager = PreferencesManager(application)

    // Estado principal del formulario
    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    // Estado de errores de validación
    private val _errores = MutableStateFlow(UsuarioErrores())
    val errores: StateFlow<UsuarioErrores> = _errores.asStateFlow()

    // Estado de carga inicial simulada (1500 ms)
    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // Mensaje temporal de éxito para animaciones y feedback de DataStore
    private val _mensajeExito = MutableStateFlow<String?>(null)
    val mensajeExito: StateFlow<String?> = _mensajeExito.asStateFlow()

    // Flow con el listado completo de usuarios guardados en ROOM para la pantalla offline
    val usuariosList: Flow<List<UsuarioEntity>> = usuarioDao.getUsuariosFlow()

    init {
        // Carga inicial simulada de 1.5 segundos
        viewModelScope.launch {
            delay(1500)
            _cargando.value = false
        }

        // Recolección continua de la preferencia guardada en DataStore
        viewModelScope.launch {
            preferencesManager.modoEspecialFlow.collect { modo ->
                _uiState.update { it.copy(modoEspecial = modo) }
            }
        }
    }

    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombre = nuevoNombre) }
        _errores.update { it.copy(nombreError = null) }
    }

    fun onCorreoChange(nuevoCorreo: String) {
        _uiState.update { it.copy(correo = nuevoCorreo) }
        _errores.update { it.copy(correoError = null) }
    }

    fun onClaveChange(nuevaClave: String) {
        _uiState.update { it.copy(clave = nuevaClave) }
        _errores.update { it.copy(claveError = null) }
    }

    fun onDireccionChange(nuevaDireccion: String) {
        _uiState.update { it.copy(direccion = nuevaDireccion) }
        _errores.update { it.copy(direccionError = null) }
    }

    fun onTerminosChange(acepta: Boolean) {
        _uiState.update { it.copy(aceptaTerminos = acepta) }
        _errores.update { it.copy(terminosError = null) }
    }

    fun actualizarFoto(uri: Uri?) {
        _uiState.update { it.copy(fotoUri = uri) }
        _errores.update { it.copy(fotoError = null) }
    }

    // Invierte el valor del Modo Especial, lo persiste en DataStore y emite feedback temporal de 2 segundos.
    fun toggleModoEspecial() {
        val nuevoModo = !_uiState.value.modoEspecial
        viewModelScope.launch {
            preferencesManager.guardarModoEspecial(nuevoModo)
            _uiState.update { it.copy(modoEspecial = nuevoModo) }
            _mensajeExito.value = "¡Modo Especial ${if (nuevoModo) "Activado" else "Desactivado"}!"
            delay(2000)
            _mensajeExito.value = null
        }
    }

    // Valida exhaustivamente todos los campos del formulario según la Guía 14.
    fun validarFormulario(): Boolean {
        val state = _uiState.value

        val nombreErr = if (state.nombre.trim().length < 3) "El nombre debe tener al menos 3 caracteres." else null
        val correoErr = if (!state.correo.contains("@") || !Patterns.EMAIL_ADDRESS.matcher(state.correo.trim()).matches()) "Ingrese un correo válido con '@'." else null
        val claveErr = if (state.clave.length < 6) "La clave debe contener al menos 6 caracteres." else null
        val direccionErr = if (state.direccion.trim().isEmpty()) "La dirección es obligatoria." else null
        val terminosErr = if (!state.aceptaTerminos) "Debe aceptar los términos y condiciones." else null
        val fotoErr = if (state.fotoUri == null) "Debe seleccionar o capturar una foto de perfil." else null

        val nuevosErrores = UsuarioErrores(
            nombreError = nombreErr,
            correoError = correoErr,
            claveError = claveErr,
            direccionError = direccionErr,
            terminosError = terminosErr,
            fotoError = fotoErr
        )

        _errores.value = nuevosErrores
        return !nuevosErrores.hasErrors
    }

    // Convierte el estado de UI actual en una entidad de ROOM y la inserta en la base de datos local.
    fun guardarEnRoom(onExito: () -> Unit) {
        if (!validarFormulario()) return

        val state = _uiState.value
        val entity = UsuarioEntity(
            nombre = state.nombre.trim(),
            correo = state.correo.trim(),
            direccion = state.direccion.trim(),
            modoEspecial = state.modoEspecial,
            fotoPath = state.fotoUri?.toString(),
            fechaRegistro = Date()
        )

        viewModelScope.launch {
            usuarioDao.insertar(entity)
            onExito()
        }
    }
}
