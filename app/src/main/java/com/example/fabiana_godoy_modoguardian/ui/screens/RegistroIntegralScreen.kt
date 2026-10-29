package com.example.fabiana_godoy_modoguardian.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.fabiana_godoy_modoguardian.ui.components.ImagenInteligente
import com.example.fabiana_godoy_modoguardian.ui.components.PremiumCard
import com.example.fabiana_godoy_modoguardian.viewmodel.UsuarioViewModel
import java.io.File

// Pantalla principal del Registro Integral con Cámara, Galería, Validaciones, DataStore, ROOM y Animaciones.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroIntegralScreen(
    usuarioViewModel: UsuarioViewModel,
    onNavigateToResumen: () -> Unit,
    onNavigateToListado: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by usuarioViewModel.uiState.collectAsState()
    val errores by usuarioViewModel.errores.collectAsState()
    val cargando by usuarioViewModel.cargando.collectAsState()
    val mensajeExito by usuarioViewModel.mensajeExito.collectAsState()

    // Launcher de Galería mediante GetContent
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { usuarioViewModel.actualizarFoto(it) }
    }

    // Launcher de Cámara básico con vista previa rápida
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val file = File(context.cacheDir, "foto_camara.jpg")
            file.outputStream().use { out ->
                it.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
            }
            usuarioViewModel.actualizarFoto(Uri.fromFile(file))
        }
    }

    // Texto derivado mediante derivedStateOf
    val textoBotonModo by remember(uiState.modoEspecial) {
        derivedStateOf {
            if (uiState.modoEspecial) "Desactivar Modo Premium" else "Activar Modo Premium"
        }
    }

    // Color animado con animateColorAsState (Gris a Verde #2E7D32)
    val colorBotonModo by animateColorAsState(
        targetValue = if (uiState.modoEspecial) Color(0xFF2E7D32) else Color(0xFF757575),
        animationSpec = tween(durationMillis = 500),
        label = "ColorBotonModo"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro Integral de Guardia") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    Button(onClick = onNavigateToListado) {
                        Text("Ver Offline")
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->

        if (cargando) {
            // Indicador de carga inicial simulada (1.5s)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Cargando componentes e historial...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Componente ImagenInteligente centrado al inicio
                ImagenInteligente(
                    fotoUri = uiState.fotoUri,
                    onClick = { galleryLauncher.launch("image/*") }
                )

                errores.fotoError?.let { err ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botones en Row para Cámara y Galería
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = { cameraLauncher.launch(null) }) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Cámara",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cámara")
                    }

                    OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Galería",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Galería")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Campos del formulario con validaciones
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = usuarioViewModel::onNombreChange,
                    label = { Text("Nombre Completo") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    isError = errores.nombreError != null,
                    supportingText = {
                        errores.nombreError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.correo,
                    onValueChange = usuarioViewModel::onCorreoChange,
                    label = { Text("Correo Electrónico") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    isError = errores.correoError != null,
                    supportingText = {
                        errores.correoError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.clave,
                    onValueChange = usuarioViewModel::onClaveChange,
                    label = { Text("Contraseña de Seguridad") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = errores.claveError != null,
                    supportingText = {
                        errores.claveError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.direccion,
                    onValueChange = usuarioViewModel::onDireccionChange,
                    label = { Text("Dirección / Puesto de Guardia") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    isError = errores.direccionError != null,
                    supportingText = {
                        errores.direccionError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Checkbox Términos
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = uiState.aceptaTerminos,
                        onCheckedChange = usuarioViewModel::onTerminosChange
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Acepto los términos y condiciones de seguridad")
                }
                errores.terminosError?.let { err ->
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de Modo Premium con color animado
                Button(
                    onClick = { usuarioViewModel.toggleModoEspecial() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorBotonModo),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Star, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = textoBotonModo, fontWeight = FontWeight.Bold)
                }

                // Card Animada de Beneficios Premium
                PremiumCard(modoEspecial = uiState.modoEspecial)

                // Feedback animado de DataStore
                AnimatedVisibility(visible = mensajeExito != null) {
                    mensajeExito?.let { msg ->
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = msg,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Registrar y ver Resumen (Guarda en ROOM)
                Button(
                    onClick = {
                        usuarioViewModel.guardarEnRoom {
                            onNavigateToResumen()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Registrar y ver Resumen", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
