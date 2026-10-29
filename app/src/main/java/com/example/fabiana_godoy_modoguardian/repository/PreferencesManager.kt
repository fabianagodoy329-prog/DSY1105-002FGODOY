package com.example.fabiana_godoy_modoguardian.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para instanciar el DataStore de Preferencias a nivel de Contexto.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "modo_guardian_prefs")

// Gestor de persistencia ligera mediante Jetpack DataStore Preferences.
// Administra la preferencia del estado 'modoEspecial'.
class PreferencesManager(private val context: Context) {

    companion object {
        private val MODO_ESPECIAL_KEY = booleanPreferencesKey("modo_especial")
    }

    // Flow reactivo que emite el estado guardado del modo especial (por defecto false).
    val modoEspecialFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[MODO_ESPECIAL_KEY] ?: false
        }

    // Guarda de manera asíncrona la preferencia del modo especial en DataStore.
    suspend fun guardarModoEspecial(modo: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MODO_ESPECIAL_KEY] = modo
        }
    }
}
