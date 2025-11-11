package com.example.todocli.manager

import com.example.todocli.model.Priority
import com.example.todocli.model.Task
import com.example.todocli.util.FileHandler
import java.time.LocalDate

class TodoManager(private val fileHandler: FileHandler = FileHandler()) {
    private val tasks = mutableListOf<Task>()
    private var nextId = 1

    init {
        // 初期化時にファイルから読み込み
        loadFromFile()
    }

    /**
     * タスクを追加
     */
    fun addTask(
        title: String,
        priority: Priority = Priority.MEDIUM,
        deadline: LocalDate? = null
    ): Task {
        val task = Task(
            id = nextId++,
            title = title,
            priority = priority,
            deadline = deadline
        )
        tasks.add(task)
        saveToFile() // 自動保存
        return task
    }

    /**
     * すべてのタスクを取得
     */
    fun getTasks(): List<Task> = tasks.toList()

    /**
     * IDでタスクを検索
     */
    fun findTaskById(id: Int): Task? = tasks.find { it.id == id }

    /**
     * タスクの完了/未完了を切り替え
     */
    fun toggleTaskCompletion(id: Int): Boolean {
        val index = tasks.indexOfFirst { it.id == id }
        if (index == -1) return false

        val task = tasks[index]
        tasks[index] = task.copy(isCompleted = !task.isCompleted)
        saveToFile() // 自動保存
        return true
    }

    /**
     * タスクを削除
     */
    fun deleteTask(id: Int): Boolean {
        val result = tasks.removeIf { it.id == id }
        if (result) {
            saveToFile() // 自動保存
        }
        return result
    }

    /**
     * キーワードでタスクを検索
     */
    fun searchTasks(keyword: String): List<Task> {
        return tasks.filter { it.title.contains(keyword, ignoreCase = true) }
    }

    /**
     * ステータスでフィルタリング
     */
    fun filterByStatus(isCompleted: Boolean): List<Task> {
        return tasks.filter { it.isCompleted == isCompleted }
    }

    /**
     * 優先度でフィルタリング
     */
    fun filterByPriority(priority: Priority): List<Task> {
        return tasks.filter { it.priority == priority }
    }

    /**
     * 期限切れのタスクを取得
     */
    fun getOverdueTasks(): List<Task> {
        return tasks.filter { it.isOverdue() }
    }

    /**
     * タスク数を取得
     */
    fun getTaskCount(): Int = tasks.size

    /**
     * 完了済みタスク数を取得
     */
    fun getCompletedTaskCount(): Int = tasks.count { it.isCompleted }

    /**
     * 未完了タスク数を取得
     */
    fun getIncompleteTaskCount(): Int = tasks.count { !it.isCompleted }

    /**
     * ファイルに保存
     */
    fun saveToFile(): Boolean {
        return fileHandler.saveTasks(tasks)
    }

    /**
     * ファイルから読み込み
     */
    fun loadFromFile(): Boolean {
        val loadedTasks = fileHandler.loadTasks()
        if (loadedTasks.isNotEmpty()) {
            tasks.clear()
            tasks.addAll(loadedTasks)
            // 次のIDを設定（最大ID + 1）
            nextId = (tasks.maxOfOrNull { it.id } ?: 0) + 1
            return true
        }
        return false
    }
}