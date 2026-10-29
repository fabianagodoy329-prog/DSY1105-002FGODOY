package com.example.fabiana_godoy_modoguardian.db

import androidx.room.TypeConverter
import java.util.Date

// Converters para permitir la persistencia de tipos de datos complejos en ROOM.
// Convierte objetos java.util.Date a Long (timestamp) y viceversa.
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
