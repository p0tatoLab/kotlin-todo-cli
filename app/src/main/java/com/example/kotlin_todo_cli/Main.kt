package com.example.todocli

import com.example.todocli.model.Priority
import com.example.todocli.model.Task
import java.time.LocalDate

fun main() {
    // UTF-8で出力
    val out = System.out.writer(Charsets.UTF_8)

    out.write("=== Kotlin TODO CLI - データモデルテスト ===\n\n")
    out.flush()

    // タスクの作成テスト
    val task1 = Task(
        id = 1,
        title = "Kotlinの勉強",
        priority = Priority.HIGH
    )

    val task2 = Task(
        id = 2,
        title = "買い物に行く",
        isCompleted = true,
        priority = Priority.LOW,
        deadline = LocalDate.now().plusDays(3)
    )

    val task3 = Task(
        id = 3,
        title = "レポート提出",
        priority = Priority.HIGH,
        deadline = LocalDate.now().minusDays(1)
    )

    // タスクの表示
    out.write("ID   | タイトル                | ステータス   | 優先度 | 期限           | 作成日時              \n")
    out.write("-----|------------------------|------------|--------|----------------|---------------------\n")
    out.write(task1.toString() + "\n")
    out.write(task2.toString() + "\n")
    out.write(task3.toString() + "\n")
    out.flush()

    out.write("\n--- 各タスクの詳細 ---\n")
    out.write("タスク1 期限切れ: ${task1.isOverdue()}\n")
    out.write("タスク2 期限切れ: ${task2.isOverdue()}\n")
    out.write("タスク3 期限切れ: ${task3.isOverdue()}\n")
    out.flush()

    // Priorityのテスト
    out.write("\n--- Priority enumのテスト ---\n")
    Priority.entries.forEach { priority ->
        out.write("${priority.name}: ${priority.displayName}\n")
    }
    out.flush()

    out.write("\n文字列からPriorityへの変換: ${Priority.fromString("high")}\n")
    out.flush()
}