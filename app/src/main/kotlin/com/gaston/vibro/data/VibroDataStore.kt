package com.gaston.vibro.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Único DataStore de la app — todos los repositorios comparten este archivo
// con keys distintas. DataStore exige una sola instancia por archivo.
val Context.vibroDataStore: DataStore<Preferences> by preferencesDataStore(name = "vibro_prefs")
