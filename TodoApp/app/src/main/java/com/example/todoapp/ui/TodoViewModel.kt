package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TodoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<TodoUiState>(TodoUiState.Loading)
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    init {
        loadTodos()
    }

    fun loadTodos() {
        _uiState.value = TodoUiState.Loading
        viewModelScope.launch {
            try {
                val todos = RetrofitClient.api.getTodos()
                val singleTodo = RetrofitClient.api.getTodo(5)
                _uiState.value = TodoUiState.Success(todos, singleTodo)
            } catch (_: IOException) {
                _uiState.value = TodoUiState.Error("No internet connection")
            } catch (e: HttpException) {
                _uiState.value = TodoUiState.Error("Server error: ${e.code()}")
            } catch (e: Exception) {
                _uiState.value = TodoUiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}