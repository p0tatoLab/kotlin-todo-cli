package com.example.todocli

import com.example.todocli.manager.TodoManager
import com.example.todocli.ui.ConsoleUI

fun main() {
    val manager = TodoManager()
    val ui = ConsoleUI(manager)
    ui.start()
}