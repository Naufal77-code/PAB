package com.example.todoapp.ui

import com.example.todoapp.data.Todo

sealed interface TodoUiState {
    data object Loading : TodoUiState
    data class Success(val todos: List<Todo>, val featuredTodo: Todo) : TodoUiState
    data class Error(val message: String) : TodoUiState
}