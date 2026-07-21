package com.gaston.vibro.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gaston.vibro.ui.theme.SkinRegistry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SkinRepository(private val context: Context) {

    private val activeSkinKey = stringPreferencesKey("active_skin_id")

    val activeSkinId: Flow<String> = context.vibroDataStore.data
        .map { prefs -> prefs[activeSkinKey] ?: SkinRegistry.default.id }

    suspend fun setActiveSkin(skinId: String) {
        context.vibroDataStore.edit { prefs -> prefs[activeSkinKey] = skinId }
    }
}
