package com.example.fabiana_godoy_modoguardian.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fabiana_godoy_modoguardian.R
import com.example.fabiana_godoy_modoguardian.model.LoginUiState
import com.example.fabiana_godoy_modoguardian.navigation.NavigationViewModel
import com.example.fabiana_godoy_modoguardian.navigation.Routes
import com.example.fabiana_godoy_modoguardian.ui.theme.Fabiana_Godoy_modoguardianTheme
import com.example.fabiana_godoy_modoguardian.ui.utils.WindowSizeClass
import com.example.fabiana_godoy_modoguardian.ui.utils.rememberWindowSizeClass
import com.example.fabiana_godoy_modoguardian.viewmodel.LoginViewModel

// Pantalla de Inicio de Sesión de la aplicación Modo Guardián.
// Implementa adaptabilidad según WindowSizeClass (Compact para móviles, Medium/Expanded para tablets).
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    navigationViewModel: NavigationViewModel,
    modifier: Modifier = Modifier
) {
    // Recopilación del estado inmutable desde el ViewModel
    val uiState by loginViewModel.uiState.collectAsState()

    // Determinación del tamaño de ventana para adaptar la interfaz
    val windowSizeClass = rememberWindowSizeClass()

    LoginContent(
        uiState = uiState,
        windowSizeClass = windowSizeClass,
        onEmailChange = loginViewModel::onEmailChange,
        onPasswordChange = loginViewModel::onPasswordChange,
        onLoginClick = {
            loginViewModel.login {
                // Navegar al Home tras login exitoso sin acumular el login en el historial
                navigationViewModel.navigateTo(
                    route = Routes.HOME,
                    popUpToRoute = Routes.LOGIN,
                    inclusive = true
                )
            }
        },
        modifier = modifier
    )
}

// Contenido Composable estático y previsualizable de la pantalla de Login.
@Composable
fun LoginContent(
    uiState: LoginUiState,
    windowSizeClass: WindowSizeClass,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Adaptabilidad de diseño: Layout Compact vs Medium/Expanded
            when (windowSizeClass) {
                WindowSizeClass.COMPACT -> {
                    // Diseño Vertical para Teléfonos Móviles
                    CompactLoginLayout(
                        uiState = uiState,
                        onEmailChange = onEmailChange,
                        onPasswordChange = onPasswordChange,
                        onLoginClick = onLoginClick
                    )
                }
                WindowSizeClass.MEDIUM, WindowSizeClass.EXPANDED -> {
                    // Diseño Horizontal / Centrado en Card para Tablets y Pantallas Anchas
                    ExpandedLoginLayout(
                        uiState = uiState,
                        onEmailChange = onEmailChange,
                        onPasswordChange = onPasswordChange,
                        onLoginClick = onLoginClick
                    )
                }
            }
        }
    }
}

// Layout de Login para pantallas verticales / de tamaño compacto (Celulares).
@Composable
private fun CompactLoginLayout(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo oficial de la aplicación
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Modo Guardián",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado de bienvenida
        Text(
            text = "Modo Guardián",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Sistema de Control de Acceso y Seguridad",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Formulario de entrada
        LoginFormFields(
            uiState = uiState,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLoginClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Layout de Login para pantallas horizontales / de tamaño amplio (Tablets).
@Composable
private fun ExpandedLoginLayout(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .widthIn(max = 700.dp)
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Lado Izquierdo: Branding y Logo
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Modo Guardián",
                    modifier = Modifier.size(130.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Modo Guardián",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Acceso seguro para administradores, supervisores y operadores.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Lado Derecho: Formulario de credenciales
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginFormFields(
                    uiState = uiState,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onLoginClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// Componente reutilizable con los campos del formulario de entrada.
@Composable
private fun LoginFormFields(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado local para alternar la visibilidad de la contraseña
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {

        // Campo OutlinedTextField para Correo Electrónico
        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = { Text("Correo Electrónico") },
            placeholder = { Text("ejemplo@guardian.test") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Ícono de correo"
                )
            },
            isError = uiState.errores.emailError != null,
            supportingText = {
                uiState.errores.emailError?.let { msg ->
                    Text(text = msg, color = MaterialTheme.colorScheme.error)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo OutlinedTextField para Contraseña con PasswordVisualTransformation
        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Ícono de candado"
                )
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = uiState.errores.passwordError != null,
            supportingText = {
                uiState.errores.passwordError?.let { msg ->
                    Text(text = msg, color = MaterialTheme.colorScheme.error)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Mensaje de Error General de Conectividad o Credenciales
        uiState.errores.generalError?.let { generalErrorMsg ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = generalErrorMsg,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón de Inicio de Sesión
        // CÓDIGO CORREGIDO:
        Button(
            onClick = onLoginClick,
            enabled = !uiState.isLoading, // El botón estará activo siempre, salvo cuando cargue
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (uiState.isLoading) {
                // Indicador de carga en progreso
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
// Previews de Composable

@Preview(showBackground = true, name = "Login Compacto (Celular)")
@Composable
fun LoginScreenCompactPreview() {
    Fabiana_Godoy_modoguardianTheme {
        LoginContent(
            uiState = LoginUiState(
                email = "admin@guardian.test",
                password = "123"
            ),
            windowSizeClass = WindowSizeClass.COMPACT,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Login Amplio (Tablet)")
@Composable
fun LoginScreenExpandedPreview() {
    Fabiana_Godoy_modoguardianTheme {
        LoginContent(
            uiState = LoginUiState(
                email = "supervisor@guardian.test",
                password = "123"
            ),
            windowSizeClass = WindowSizeClass.EXPANDED,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}
