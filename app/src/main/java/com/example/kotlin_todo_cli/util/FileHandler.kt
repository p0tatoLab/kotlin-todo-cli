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
}