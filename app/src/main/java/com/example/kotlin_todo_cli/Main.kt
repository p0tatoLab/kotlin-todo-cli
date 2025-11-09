package com.example.todocli

import com.example.todocli.manager.TodoManager
import com.example.todocli.model.Priority
import com.example.todocli.model.Task
import java.time.LocalDate

fun main() {
    // UTF-8で出力
    val out = System.out.writer(Charsets.UTF_8)

    out.write("=== Kotlin TODO CLI - TodoManagerテスト ===\n\n")
    out.flush()

    val manager = TodoManager()

    // タスクを追加
    out.write("--- タスクを追加 ---\n")
    out.flush()
    manager.addTask("Kotlinの基礎を学ぶ", Priority.HIGH)
    manager.addTask("買い物に行く", Priority.LOW, LocalDate.now().plusDays(2))
    manager.addTask("レポートを書く", Priority.HIGH, LocalDate.now().plusDays(7))
    manager.addTask("本を読む", Priority.MEDIUM)
    out.write("${manager.getTaskCount()}件のタスクを追加しました\n\n")
    out.flush()

    // すべてのタスクを表示
    out.write("--- すべてのタスク ---\n")
    out.flush()
    displayTasks(out, manager.getTasks())

    // タスクを完了にする
    out.write("\n--- タスク2を完了にする ---\n")
    out.flush()
    if (manager.toggleTaskCompletion(2)) {
        out.write("タスク2を完了にしました\n")
        out.flush()
    }
    displayTasks(out, manager.getTasks())

    // タスクを削除
    out.write("\n--- タスク4を削除 ---\n")
    out.flush()
    if (manager.deleteTask(4)) {
        out.write("タスク4を削除しました\n")
        out.flush()
    }
    displayTasks(out, manager.getTasks())

    // 検索テスト
    out.write("\n--- 「レポート」で検索 ---\n")
    out.flush()
    val searchResults = manager.searchTasks("レポート")
    displayTasks(out, searchResults)

    // フィルタリングテスト
    out.write("\n--- 未完了タスクのみ表示 ---\n")
    out.flush()
    val incompleteTasks = manager.filterByStatus(false)
    displayTasks(out, incompleteTasks)

    // 優先度フィルタリング
    out.write("\n--- 優先度が高いタスク ---\n")
    out.flush()
    val highPriorityTasks = manager.filterByPriority(Priority.HIGH)
    displayTasks(out, highPriorityTasks)

    // 統計情報
    out.write("\n--- 統計情報 ---\n")
    out.write("総タスク数: ${manager.getTaskCount()}\n")
    out.write("完了済み: ${manager.getCompletedTaskCount()}\n")
    out.write("未完了: ${manager.getIncompleteTaskCount()}\n")
    out.write("期限切れ: ${manager.getOverdueTasks().size}\n")
    out.flush()
}

fun displayTasks(out: java.io.Writer, tasks: List<Task>) {
    if (tasks.isEmpty()) {
        out.write("タスクがありません\n")
        out.flush()
        return
    }

    out.write("ID   | タイトル                | ステータス   | 優先度 | 期限           | 作成日時              \n")
    out.write("-----|------------------------|------------|--------|----------------|---------------------\n")
    tasks.forEach {
        out.write(it.toString() + "\n")
    }
    out.flush()
}