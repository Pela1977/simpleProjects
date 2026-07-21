package com.gaston.vibro.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gaston.vibro.haptics.RampType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class FavoriteConfig(
    val id: String,
    val name: String,
    val patternId: String,
    val intensityLevel: Int,
    val rampType: RampType,
    val onTimeMs: Long,
    val offTimeMs: Long,
    val createdAt: Long
)

class FavoritesRepository(private val context: Context) {

    private val favoritesKey = stringPreferencesKey("favorites_json")

    val favorites: Flow<List<FavoriteConfig>> = context.vibroDataStore.data
        .map { prefs -> parse(prefs[favoritesKey] ?: "[]") }

    suspend fun add(
        name: String,
        patternId: String,
        intensityLevel: Int,
        rampType: RampType,
        onTimeMs: Long,
        offTimeMs: Long
    ) {
        context.vibroDataStore.edit { prefs ->
            val list = parse(prefs[favoritesKey] ?: "[]").toMutableList()
            list.add(
                FavoriteConfig(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    patternId = patternId,
                    intensityLevel = intensityLevel,
                    rampType = rampType,
                    onTimeMs = onTimeMs,
                    offTimeMs = offTimeMs,
                    createdAt = System.currentTimeMillis()
                )
            )
            prefs[favoritesKey] = serialize(list)
        }
    }

    suspend fun rename(id: String, newName: String) {
        context.vibroDataStore.edit { prefs ->
            val list = parse(prefs[favoritesKey] ?: "[]")
                .map { if (it.id == id) it.copy(name = newName) else it }
            prefs[favoritesKey] = serialize(list)
        }
    }

    suspend fun delete(id: String) {
        context.vibroDataStore.edit { prefs ->
            val list = parse(prefs[favoritesKey] ?: "[]").filter { it.id != id }
            prefs[favoritesKey] = serialize(list)
        }
    }

    private fun parse(json: String): List<FavoriteConfig> = try {
        val arr = JSONArray(json)
        (0 until arr.length()).mapNotNull { i ->
            val o = arr.getJSONObject(i)
            val ramp = try {
                RampType.valueOf(o.getString("rampType"))
            } catch (e: IllegalArgumentException) {
                RampType.LINEAR_UP
            }
            FavoriteConfig(
                id = o.getString("id"),
                name = o.getString("name"),
                patternId = o.getString("patternId"),
                intensityLevel = o.getInt("intensityLevel"),
                rampType = ramp,
                onTimeMs = o.getLong("onTimeMs"),
                offTimeMs = o.getLong("offTimeMs"),
                createdAt = o.optLong("createdAt")
            )
        }
    } catch (e: Exception) {
        emptyList()
    }

    private fun serialize(list: List<FavoriteConfig>): String {
        val arr = JSONArray()
        list.forEach { f ->
            arr.put(
                JSONObject()
                    .put("id", f.id)
                    .put("name", f.name)
                    .put("patternId", f.patternId)
                    .put("intensityLevel", f.intensityLevel)
                    .put("rampType", f.rampType.name)
                    .put("onTimeMs", f.onTimeMs)
                    .put("offTimeMs", f.offTimeMs)
                    .put("createdAt", f.createdAt)
            )
        }
        return arr.toString()
    }
}
