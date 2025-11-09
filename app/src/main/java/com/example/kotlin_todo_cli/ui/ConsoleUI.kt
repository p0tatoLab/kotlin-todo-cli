package com.example.todocli.ui

import com.example.todocli.manager.TodoManager
import com.example.todocli.model.Priority
import com.example.todocli.model.Task
import java.io.Writer
import java.time.LocalDate
import java.time.format.DateTimeParseException

class ConsoleUI(private val manager: TodoManager) {
    private val out: Writer = System.out.writer(Charsets.UTF_8)
    private val reader = System.`in`.bufferedReader()

    /**
     * メインループ
     */
    fun start() {
        out.write("=== Kotlin TODO CLI ===\n")
        out.write("タスク管理システムへようこそ！\n\n")
        out.flush()

        var running = true
        while (running) {
            showMenu()
            val choice = readInput("選択してください: ")
            out.write("\n")
            out.flush()

            when (choice) {
                "1" -> addTask()
                "2" -> listAllTasks()
                "3" -> toggleTaskCompletion()
                "4" -> deleteTask()
                "5" -> searchTasks()
                "6" -> filterTasks()
                "7" -> showStatistics()
                "0" -> {
                    out.write("アプリケーションを終了します。\n")
                    out.flush()
                    running = false
                }
                else -> {
                    out.write("無効な選択です。もう一度お試しください。\n\n")
                    out.flush()
                }
            }
        }
    }

    /**
     * メニューを表示
     */
    private fun showMenu() {
        out.write("==========================================\n")
        out.write("1. タスクを追加\n")
        out.write("2. タスク一覧を表示\n")
        out.write("3. タスクの完了/未完了を切り替え\n")
        out.write("4. タスクを削除\n")
        out.write("5. タスクを検索\n")
        out.write("6. タスクをフィルタリング\n")
        out.write("7. 統計情報を表示\n")
        out.write("0. 終了\n")
        out.write("==========================================\n")
        out.flush()
    }

    /**
     * タスクを追加
     */
    private fun addTask() {
        out.write("--- タスクの追加 ---\n")
        out.flush()

        val title = readInput("タスク名: ")
        if (title.isBlank()) {
            out.write("エラー: タスク名は空にできません。\n\n")
            out.flush()
            return
        }

        // 優先度の選択
        out.write("優先度を選択 (1:高, 2:中, 3:低) [デフォルト: 2]: ")
        out.flush()
        val priorityInput = reader.readLine() ?: "2"
        val priority = when (priorityInput) {
            "1" -> Priority.HIGH
            "3" -> Priority.LOW
            else -> Priority.MEDIUM
        }

        // 期限の入力
        out.write("期限 (yyyy-MM-dd形式、なしの場合は空Enter): ")
        out.flush()
        val deadlineInput = reader.readLine() ?: ""
        val deadline = if (deadlineInput.isNotBlank()) {
            try {
                LocalDate.parse(deadlineInput)
            } catch (e: DateTimeParseException) {
                out.write("警告: 日付の形式が正しくありません。期限なしで登録します。\n")
                out.flush()
                null
            }
        } else {
            null
        }

        val task = manager.addTask(title, priority, deadline)
        out.write("タスクを追加しました: ${task.title} (ID: ${task.id})\n\n")
        out.flush()
    }

    /**
     * すべてのタスクを表示
     */
    private fun listAllTasks() {
        out.write("--- タスク一覧 ---\n")
        out.flush()
        displayTasks(manager.getTasks())
    }

    /**
     * タスクの完了/未完了を切り替え
     */
    private fun toggleTaskCompletion() {
        out.write("--- タスクの完了/未完了切り替え ---\n")
        out.flush()

        listAllTasks()

        val idInput = readInput("切り替えるタスクのID: ")
        val id = idInput.toIntOrNull()

        if (id == null) {
            out.write("エラー: 有効なIDを入力してください。\n\n")
            out.flush()
            return
        }

        if (manager.toggleTaskCompletion(id)) {
            val task = manager.findTaskById(id)
            val status = if (task?.isCompleted == true) "完了" else "未完了"
            out.write("タスク$id を${status}に変更しました。\n\n")
            out.flush()
        } else {
            out.write("エラー: ID $id のタスクが見つかりません。\n\n")
            out.flush()
        }
    }

    /**
     * タスクを削除
     */
    private fun deleteTask() {
        out.write("--- タスクの削除 ---\n")
        out.flush()

        listAllTasks()

        val idInput = readInput("削除するタスクのID: ")
        val id = idInput.toIntOrNull()

        if (id == null) {
            out.write("エラー: 有効なIDを入力してください。\n\n")
            out.flush()
            return
        }

        if (manager.deleteTask(id)) {
            out.write("タスク$id を削除しました。\n\n")
            out.flush()
        } else {
            out.write("エラー: ID $id のタスクが見つかりません。\n\n")
            out.flush()
        }
    }

    /**
     * タスクを検索
     */
    private fun searchTasks() {
        out.write("--- タスクの検索 ---\n")
        out.flush()

        val keyword = readInput("検索キーワード: ")
        if (keyword.isBlank()) {
            out.write("エラー: キーワードを入力してください。\n\n")
            out.flush()
            return
        }

        val results = manager.searchTasks(keyword)
        out.write("検索結果 (「${keyword}」を含むタスク):\n")
        out.flush()
        displayTasks(results)
    }

    /**
     * タスクをフィルタリング
     */
    private fun filterTasks() {
        out.write("--- タスクのフィルタリング ---\n")
        out.write("1. 未完了のタスク\n")
        out.write("2. 完了済みのタスク\n")
        out.write("3. 優先度が高いタスク\n")
        out.write("4. 優先度が中のタスク\n")
        out.write("5. 優先度が低いタスク\n")
        out.write("6. 期限切れのタスク\n")
        out.flush()

        val choice = readInput("選択してください: ")

        val tasks = when (choice) {
            "1" -> manager.filterByStatus(false)
            "2" -> manager.filterByStatus(true)
            "3" -> manager.filterByPriority(Priority.HIGH)
            "4" -> manager.filterByPriority(Priority.MEDIUM)
            "5" -> manager.filterByPriority(Priority.LOW)
            "6" -> manager.getOverdueTasks()
            else -> {
                out.write("無効な選択です。\n\n")
                out.flush()
                return
            }
        }

        out.write("フィルタリング結果:\n")
        out.flush()
        displayTasks(tasks)
    }

    /**
     * 統計情報を表示
     */
    private fun showStatistics() {
        out.write("--- 統計情報 ---\n")
        out.write("総タスク数: ${manager.getTaskCount()}\n")
        out.write("完了済み: ${manager.getCompletedTaskCount()}\n")
        out.write("未完了: ${manager.getIncompleteTaskCount()}\n")
        out.write("期限切れ: ${manager.getOverdueTasks().size}\n\n")
        out.flush()
    }

    /**
     * タスクを表示
     */
    private fun displayTasks(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            out.write("タスクがありません。\n\n")
            out.flush()
            return
        }

        out.write("ID   | タイトル                | ステータス   | 優先度 | 期限           | 作成日時              \n")
        out.write("-----|------------------------|------------|--------|----------------|---------------------\n")
        tasks.forEach {
            out.write(it.toString() + "\n")
        }
        out.write("\n")
        out.flush()
    }

    /**
     * ユーザー入力を読み取る
     */
    private fun readInput(prompt: String): String {
        out.write(prompt)
        out.flush()
        return reader.readLine() ?: ""
    }
}