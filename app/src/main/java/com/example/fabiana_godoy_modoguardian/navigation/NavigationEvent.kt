package com.example.fabiana_godoy_modoguardian.navigation

// Eventos sealed class que representan las acciones de navegación dentro de la app.
// Utilizados con SharedFlow para desacoplar la navegación de los Composables.
sealed class NavigationEvent {

    data class NavigateTo(
        val route: String,
        val popUpToRoute: String? = null,
        val inclusive: Boolean = false
    ) : NavigationEvent()

    object NavigateBack : NavigationEvent()
}

// Rutas de la aplicación Modo Guardián.
object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val REGISTRO = "registro"
    const val RESUMEN = "resumen"
    const val LISTADO = "listado"
}
