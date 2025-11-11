package com.example.todocli.model

enum class Priority(val displayName: String, val sortOrder: Int) {
    HIGH("高", 1),
    MEDIUM("中", 2),
    LOW("低", 3);

    companion object {
        fun fromString(value: String): Priority? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }

        fun fromNumber(number: Int): Priority? {
            return when (number) {
                1 -> HIGH
                2 -> MEDIUM
                3 -> LOW
                else -> null
            }
        }

        fun prompt(): String {
            return "優先度を選択 (1:${HIGH.displayName}, 2:${MEDIUM.displayName}, 3:${LOW.displayName})"
        }
    }
}