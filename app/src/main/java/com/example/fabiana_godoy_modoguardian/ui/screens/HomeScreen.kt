package com.example.fabiana_godoy_modoguardian.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fabiana_godoy_modoguardian.model.Role
import com.example.fabiana_godoy_modoguardian.model.User
import com.example.fabiana_godoy_modoguardian.navigation.NavigationViewModel
import com.example.fabiana_godoy_modoguardian.navigation.Routes
import com.example.fabiana_godoy_modoguardian.ui.theme.Fabiana_Godoy_modoguardianTheme
import com.example.fabiana_godoy_modoguardian.viewmodel.LoginViewModel

// Pantalla principal Home.
// Observa el mismo LoginViewModel mediante collectAsState() e integra acceso al Módulo Integral.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    loginViewModel: LoginViewModel,
    navigationViewModel: NavigationViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by loginViewModel.uiState.collectAsState()
    val user = uiState.currentUser

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modo Guardián - Panel Principal") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = {
                        loginViewModel.logout()
                        navigationViewModel.navigateTo(
                            route = Routes.LOGIN,
                            popUpToRoute = Routes.HOME,
                            inclusive = true
                        )
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión"
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (user != null) {
                // Tarjeta de información del usuario y su rol
                UserInfoHeader(user = user)

                Spacer(modifier = Modifier.height(12.dp))

                // Acceso destacado al Módulo Integral (Guía 14)
                Button(
                    onClick = { navigationViewModel.navigateTo(Routes.REGISTRO) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Abrir Módulo Integral (ROOM + Cámara + Animaciones)")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Vista diferenciada por rol
                when (user.role) {
                    Role.ADMIN -> AdminDashboard(onNavigateToRegistro = { navigationViewModel.navigateTo(Routes.REGISTRO) })
                    Role.SUPERVISOR -> SupervisorDashboard(onNavigateToRegistro = { navigationViewModel.navigateTo(Routes.REGISTRO) })
                    Role.OPERADOR -> OperadorDashboard(onNavigateToRegistro = { navigationViewModel.navigateTo(Routes.REGISTRO) })
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No se ha detectado una sesión activa.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        navigationViewModel.navigateTo(Routes.LOGIN)
                    }) {
                        Text("Ir al Login")
                    }
                }
            }
        }
    }
}

@Composable
private fun UserInfoHeader(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Usuario",
                modifier = Modifier.padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            RoleBadge(role = user.role)
        }
    }
}

@Composable
private fun RoleBadge(role: Role) {
    val (backgroundColor, textColor) = when (role) {
        Role.ADMIN -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        Role.SUPERVISOR -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        Role.OPERADOR -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = role.displayName,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun AdminDashboard(onNavigateToRegistro: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Panel de Administración del Sistema",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        item {
            ActionCard(
                title = "Registro e Inspección en Base de Datos ROOM",
                description = "Captura de foto con cámara/galería y persistencia local offline.",
                icon = Icons.Default.Storage,
                onClick = onNavigateToRegistro
            )
        }
        item {
            ActionCard(
                title = "Gestión de Usuarios y Permisos",
                description = "Crear, editar o desactivar cuentas de guardias y supervisores.",
                icon = Icons.Default.Security
            )
        }
        item {
            ActionCard(
                title = "Auditoría Global de Eventos",
                description = "Ver bitácora de accesos, intentos fallidos y registros del sistema.",
                icon = Icons.Default.Dashboard
            )
        }
    }
}

@Composable
private fun SupervisorDashboard(onNavigateToRegistro: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Panel de Monitoreo de Supervisión",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        item {
            ActionCard(
                title = "Registro de Inspección con Cámara",
                description = "Registrar fotos de rondas e historial guardado en ROOM.",
                icon = Icons.Default.CameraAlt,
                onClick = onNavigateToRegistro
            )
        }
        item {
            ActionCard(
                title = "Rondas de Guardia en Vivo",
                description = "Supervisar el estado de patrulla de los operadores en terreno.",
                icon = Icons.Default.CheckCircle
            )
        }
        item {
            ActionCard(
                title = "Centro de Alertas de Incidencias",
                description = "Revisar reportes críticos ingresados por la guardia.",
                icon = Icons.Default.Warning
            )
        }
    }
}

@Composable
private fun OperadorDashboard(onNavigateToRegistro: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Panel Operativo de Ronda de Guardia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        item {
            ActionCard(
                title = "Capturar Foto de Ronda y Guardar en ROOM",
                description = "Tomar foto con cámara o cargar desde galería y validar.",
                icon = Icons.Default.CameraAlt,
                onClick = onNavigateToRegistro
            )
        }
        item {
            ActionCard(
                title = "Registrar Punto de Ronda",
                description = "Marcar checkpoint de presencia mediante escaneo de punto.",
                icon = Icons.Default.CheckCircle
            )
        }
        item {
            ActionCard(
                title = "Checklist de Entrega de Turno",
                description = "Verificar equipamiento, radiofrecuencia y novedades.",
                icon = Icons.AutoMirrored.Filled.Assignment
            )
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenAdminPreview() {
    Fabiana_Godoy_modoguardianTheme {
        UserInfoHeader(
            user = User(
                email = "admin@guardian.test",
                nombre = "Administrador Modo Guardián",
                role = Role.ADMIN
            )
        )
    }
}
