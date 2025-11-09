package com.example.todocli.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Serializable
data class Task(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val priority: Priority = Priority.MEDIUM,
    @Serializable(with = LocalDateSerializer::class)
    val deadline: LocalDate? = null
) {
    fun getStatusDisplay(): String = if (isCompleted) "✓ 完了" else "○ 未完了"

    fun getDeadlineDisplay(): String = deadline?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) ?: "なし"

    fun isOverdue(): Boolean {
        return deadline?.let { it.isBefore(LocalDate.now()) && !isCompleted } ?: false
    }

    override fun toString(): String {
        val overdueMarker = if (isOverdue()) " [期限切れ]" else ""
        return String.format(
            Locale.getDefault(),
            "%-4d | %-20s | %-8s | %-6s | %-12s | %s%s",
            id,
            title.take(20),
            getStatusDisplay(),
            priority.displayName,
            getDeadlineDisplay(),
            createdAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
            overdueMarker
        )
    }
}

// LocalDateTimeのシリアライザー
object LocalDateTimeSerializer : kotlinx.serialization.KSerializer<LocalDateTime> {
    override val descriptor = kotlinx.serialization.descriptors.PrimitiveSerialDescriptor(
        "LocalDateTime",
        kotlinx.serialization.descriptors.PrimitiveKind.STRING
    )

    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: kotlinx.serialization.encoding.Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }
}

// LocalDateのシリアライザー
object LocalDateSerializer : kotlinx.serialization.KSerializer<LocalDate> {
    override val descriptor = kotlinx.serialization.descriptors.PrimitiveSerialDescriptor(
        "LocalDate",
        kotlinx.serialization.descriptors.PrimitiveKind.STRING
    )

    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: kotlinx.serialization.encoding.Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}