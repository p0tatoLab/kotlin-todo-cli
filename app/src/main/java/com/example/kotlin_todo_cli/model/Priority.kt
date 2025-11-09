package com.example.todocli.model

enum class Priority(val displayName: String) {
    HIGH("高"),
    MEDIUM("中"),
    LOW("低");

    companion object {
        fun fromString(value: String): Priority? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}