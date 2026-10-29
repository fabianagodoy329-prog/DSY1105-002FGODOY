package com.example.fabiana_godoy_modoguardian.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fabiana_godoy_modoguardian.model.LoginErrores
import com.example.fabiana_godoy_modoguardian.model.LoginUiState
import com.example.fabiana_godoy_modoguardian.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    // Estado privado mutable del formulario de login
    private val _uiState = MutableStateFlow(LoginUiState())

    // Estado público inmutable observado por la UI
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    //Actualiza el valor del correo electrónico en el estado y limpia errores previos del campo.

    fun onEmailChange(newEmail: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = newEmail,
                errores = currentState.errores.copy(
                    emailError = null,
                    generalError = null
                )
            )
        }
    }

    //Actualiza la contraseña en el estado y limpia errores previos del campo.

    fun onPasswordChange(newPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = newPassword,
                errores = currentState.errores.copy(
                    passwordError = null,
                    generalError = null
                )
            )
        }
    }

    //Valida los campos del formulario antes de procesar la autenticación.

    fun validarFormulario(): Boolean {
        val currentEmail = _uiState.value.email.trim()
        val currentPassword = _uiState.value.password

        var emailError: String? = null
        var passwordError: String? = null

        // Validación de formato de correo electrónico
        if (currentEmail.isEmpty()) {
            emailError = "El correo electrónico es obligatorio."
        } else if (!Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()) {
            emailError = "Ingrese un correo electrónico válido (ej: usuario@guardian.test)."
        }

        // Validación de contraseña
        if (currentPassword.isEmpty()) {
            passwordError = "La contraseña es obligatoria."
        } else if (currentPassword.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres."
        }

        val nuevosErrores = LoginErrores(
            emailError = emailError,
            passwordError = passwordError,
            generalError = null
        )

        // Actualizamos el estado con los errores detectados
        _uiState.update { it.copy(errores = nuevosErrores) }

        return !nuevosErrores.hasErrors
    }

    //Procesa el inicio de sesión contra el repositorio de autenticación.

    fun login(onSuccess: () -> Unit) {
        if (!validarFormulario()) return

        // Iniciar estado de carga en la UI
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val email = _uiState.value.email
            val password = _uiState.value.password

            val resultado = repository.login(email, password)

            resultado.onSuccess { usuario ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        currentUser = usuario,
                        isLoggedIn = true,
                        errores = LoginErrores()
                    )
                }
                onSuccess()
            }.onFailure { error ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isLoggedIn = false,
                        currentUser = null,
                        errores = currentState.errores.copy(
                            generalError = error.message ?: "Error de autenticación."
                        )
                    )
                }
            }
        }
    }

    //Cierra la sesión activa y restablece el estado del formulario a cero.
    fun logout() {
        _uiState.value = LoginUiState()
    }
}
