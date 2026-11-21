package com.example.dailyscribe.data.local.converters

import androidx.room.TypeConverter
import org.json.JSONArray

class DatabaseConverters {

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val jsonArray = JSONArray()
        value.forEach { jsonArray.put(it) }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        val list = mutableListOf<String>()
        try {
            val jsonArray = JSONArray(value)
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
        } catch (_: Exception) {
            return value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        }
        return list
    }

    @TypeConverter
    fun fromLongList(value: List<Long>): String {
        val jsonArray = JSONArray()
        value.forEach { jsonArray.put(it) }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toLongList(value: String): List<Long> {
        if (value.isBlank()) return emptyList()
        val list = mutableListOf<Long>()
        try {
            val jsonArray = JSONArray(value)
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getLong(i))
            }
        } catch (_: Exception) {
            return value.split(",").mapNotNull { it.trim().toLongOrNull() }
        }
        return list
    }
}
