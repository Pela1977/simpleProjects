package com.gaston.vibro.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gaston.vibro.haptics.PatternCategory
import com.gaston.vibro.haptics.VivroPattern
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

// Persistencia de patrones creados por el usuario.
// DataStore + JSON en lugar de Room: sin KSP, sin migraciones, y los arrays
// de longitud variable viajan como JSON. Migrable a Room si la app crece.
class CustomPatternRepository(private val context: Context) {

    private val patternsKey = stringPreferencesKey("custom_patterns_json")

    val patterns: Flow<List<VivroPattern>> = context.vibroDataStore.data
        .map { prefs -> parse(prefs[patternsKey] ?: "[]") }

    suspend fun add(
        name: String,
        description: String,
        category: PatternCategory,
        timings: LongArray,
        amplitudes: IntArray
    ): VivroPattern {
        val pattern = VivroPattern(
            id = "custom_" + UUID.randomUUID().toString().take(8),
            name = name,
            category = category,
            description = description,
            timings = timings,
            amplitudes = amplitudes,
            repeat = 0
        )
        context.vibroDataStore.edit { prefs ->
            val list = parse(prefs[patternsKey] ?: "[]").toMutableList()
            list.add(pattern)
            prefs[patternsKey] = serialize(list)
        }
        return pattern
    }

    suspend fun delete(id: String) {
        context.vibroDataStore.edit { prefs ->
            val list = parse(prefs[patternsKey] ?: "[]").filter { it.id != id }
            prefs[patternsKey] = serialize(list)
        }
    }

    private fun parse(json: String): List<VivroPattern> = try {
        val arr = JSONArray(json)
        (0 until arr.length()).mapNotNull { i ->
            try {
                val o = arr.getJSONObject(i)
                val timingsArr = o.getJSONArray("timings")
                val ampsArr = o.getJSONArray("amplitudes")
                val category = try {
                    PatternCategory.valueOf(o.getString("category"))
                } catch (e: IllegalArgumentException) {
                    PatternCategory.SUAVE
                }
                VivroPattern(
                    id = o.getString("id"),
                    name = o.getString("name"),
                    category = category,
                    description = o.optString("description"),
                    timings = LongArray(timingsArr.length()) { j -> timingsArr.getLong(j) },
                    amplitudes = IntArray(ampsArr.length()) { j -> ampsArr.getInt(j) },
                    repeat = o.optInt("repeat", 0)
                )
            } catch (e: Exception) {
                null // un patrón corrupto no rompe la biblioteca entera
            }
        }
    } catch (e: Exception) {
        emptyList()
    }

    private fun serialize(list: List<VivroPattern>): String {
        val arr = JSONArray()
        list.forEach { p ->
            val timingsArr = JSONArray().also { ja -> p.timings.forEach { ja.put(it) } }
            val ampsArr = JSONArray().also { ja -> p.amplitudes.forEach { ja.put(it) } }
            arr.put(
                JSONObject()
                    .put("id", p.id)
                    .put("name", p.name)
                    .put("category", p.category.name)
                    .put("description", p.description)
                    .put("timings", timingsArr)
                    .put("amplitudes", ampsArr)
                    .put("repeat", p.repeat)
            )
        }
        return arr.toString()
    }
}
