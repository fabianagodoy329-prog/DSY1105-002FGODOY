package com.example.fabiana_godoy_modoguardian.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * NavigationViewModel encargado de emitir eventos de navegación de forma reactiva.
 * Permite que cualquier pantalla ejecute un evento de navegación utilizando MutableSharedFlow.
 */
class NavigationViewModel : ViewModel() {

    // SharedFlow mutable para emitir eventos de navegación sin conservar estado obsoleto
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    // Exposed as SharedFlow inmutable para ser colectado en MainActivity con LaunchedEffect
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    /**
     * Emite una solicitud de navegación hacia una ruta destino.
     */
    fun navigateTo(route: String, popUpToRoute: String? = null, inclusive: Boolean = false) {
        viewModelScope.launch {
            _navigationEvents.emit(
                NavigationEvent.NavigateTo(
                    route = route,
                    popUpToRoute = popUpToRoute,
                    inclusive = inclusive
                )
            )
        }
    }

    /**
     * Emite una solicitud de retroceso en la navegación.
     */
    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateBack)
        }
    }
}
