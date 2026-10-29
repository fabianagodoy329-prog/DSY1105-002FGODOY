// 3 usuarios ficticios obligatorios, sintéticos, nunca credenciales reales:
//admin@guardian.test / 123456 -> Rol Admin
//supervisor@guardian.test / 123456 -> Rol Supervisor
//operador@guardian.test / 123456 -> Rol Operador

package com.example.fabiana_godoy_modoguardian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fabiana_godoy_modoguardian.navigation.NavigationEvent
import com.example.fabiana_godoy_modoguardian.navigation.NavigationViewModel
import com.example.fabiana_godoy_modoguardian.navigation.Routes
import com.example.fabiana_godoy_modoguardian.ui.screens.HomeScreen
import com.example.fabiana_godoy_modoguardian.ui.screens.ListadoOfflineScreen
import com.example.fabiana_godoy_modoguardian.ui.screens.LoginScreen
import com.example.fabiana_godoy_modoguardian.ui.screens.RegistroIntegralScreen
import com.example.fabiana_godoy_modoguardian.ui.screens.ResumenScreen
import com.example.fabiana_godoy_modoguardian.ui.theme.Fabiana_Godoy_modoguardianTheme
import com.example.fabiana_godoy_modoguardian.viewmodel.LoginViewModel
import com.example.fabiana_godoy_modoguardian.viewmodel.UsuarioViewModel

// Actividad Principal de la aplicación modo Guardián.
// inicializa la navegación central, los viewModels y la tabla de rutas.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Fabiana_Godoy_modoguardianTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ModoGuardianApp()
                }
            }
        }
    }
}

// composable raíz que configura el navController, los ViewModels compartidos y las rutas de la aplicación.
@Composable
fun ModoGuardianApp() {
    val navController = rememberNavController()

    // Instancias de ViewModel compartidas
    val navigationViewModel: NavigationViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    // recolección reactiva de eventos de navegación emitidos con sharedflow
    LaunchedEffect(Unit) {
        navigationViewModel.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route) {
                        event.popUpToRoute?.let { popRoute ->
                            popUpTo(popRoute) {
                                inclusive = event.inclusive
                            }
                        }
                    }
                }
                is NavigationEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    // configuración del NavHost central
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // ruta de inicio de sesión
        composable(Routes.LOGIN) {
            LoginScreen(
                loginViewModel = loginViewModel,
                navigationViewModel = navigationViewModel
            )
        }

        // ruuta de pantalla principal
        composable(Routes.HOME) {
            HomeScreen(
                loginViewModel = loginViewModel,
                navigationViewModel = navigationViewModel
            )
        }

        // ruta de registro integral romm + cámara + galería + datastore
        composable(Routes.REGISTRO) {
            RegistroIntegralScreen(
                usuarioViewModel = usuarioViewModel,
                onNavigateToResumen = { navigationViewModel.navigateTo(Routes.RESUMEN) },
                onNavigateToListado = { navigationViewModel.navigateTo(Routes.LISTADO) }
            )
        }

        // ruta de resumen
        composable(Routes.RESUMEN) {
            ResumenScreen(
                usuarioViewModel = usuarioViewModel,
                onNavigateToListado = { navigationViewModel.navigateTo(Routes.LISTADO) },
                onNavigateBack = { navigationViewModel.navigateBack() }
            )
        }

        // ruta de listado offline en ROOM
        composable(Routes.LISTADO) {
            ListadoOfflineScreen(
                usuarioViewModel = usuarioViewModel,
                onNavigateBack = { navigationViewModel.navigateBack() }
            )
        }
    }
}


//