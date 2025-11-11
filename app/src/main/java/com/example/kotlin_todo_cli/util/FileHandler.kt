package com.example.todocli.util

import com.example.todocli.model.Task
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileHandler(private val filePath: String = "data/todos.json") {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    /**
     * タスクをファイルに保存
     */
    fun saveTasks(tasks: List<Task>): Boolean {
        return try {
            val file = File(filePath)

            // 親ディレクトリが存在しない場合は作成
            file.parentFile?.mkdirs()

            val jsonString = json.encodeToString(tasks)
            file.writeText(jsonString, Charsets.UTF_8)

            true
        } catch (e: Exception) {
            System.err.println("エラー: ファイルの保存に失敗しました: ${e.message}")
            false
        }
    }

    /**
     * ファイルからタスクを読み込み
     */
    fun loadTasks(): List<Task> {
        return try {
            val file = File(filePath)

            if (!file.exists()) {
                // ファイルが存在しない場合は空リストを返す
                return emptyList()
            }

            val jsonString = file.readText(Charsets.UTF_8)
            json.decodeFromString<List<Task>>(jsonString)
        } catch (e: Exception) {
            System.err.println("エラー: ファイルの読み込みに失敗しました: ${e.message}")
            emptyList()
        }
    }

    /**
     * ファイルが存在するかチェック
     */
    fun fileExists(): Boolean {
        return File(filePath).exists()
    }

    /**
     * ファイルを削除
     */
    fun deleteFile(): Boolean {
        return try {
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            } else {
                false
            }
        } catch (e: Exception) {
            System.err.println("エラー: ファイルの削除に失敗しました: ${e.message}")
            false
        }
    }

    /**
     * バックアップファイルを作成
     * @return バックアップファイルのパス（成功時）、null（失敗時）
     */
    fun createBackup(): String? {
        return try {
            val sourceFile = File(filePath)
            if (!sourceFile.exists()) {
                return null
            }

            val timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            val backupPath = filePath.replace(".json", "_backup_$timestamp.json")
            val backupFile = File(backupPath)

            sourceFile.copyTo(backupFile, overwrite = false)

            backupPath  // バックアップファイルのパスを返す
        } catch (e: Exception) {
            System.err.println("エラー: バックアップの作成に失敗しました: ${e.message}")
            null
        }
    }
}