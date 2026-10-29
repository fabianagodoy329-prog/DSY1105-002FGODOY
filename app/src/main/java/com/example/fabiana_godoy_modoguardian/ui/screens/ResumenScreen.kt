package com.example.fabiana_godoy_modoguardian.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fabiana_godoy_modoguardian.ui.components.ImagenInteligente
import com.example.fabiana_godoy_modoguardian.viewmodel.UsuarioViewModel

// Pantalla de Resumen que observa el estado inmutable desde el UsuarioViewModel compartido.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    usuarioViewModel: UsuarioViewModel,
    onNavigateToListado: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by usuarioViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen del Registro") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de perfil capturada/seleccionada
            ImagenInteligente(fotoUri = uiState.fotoUri)

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta con la información consolidada
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Información del Usuario",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Nombre: ${uiState.nombre}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Correo: ${uiState.correo}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Dirección: ${uiState.direccion}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Modo: ${if (uiState.modoEspecial) "Premium / Especial" else "Estándar / Básico"}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (uiState.modoEspecial) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para ir al Listado Offline guardado en ROOM
            Button(
                onClick = onNavigateToListado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver Registros Guardados en ROOM", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
