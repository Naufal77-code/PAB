package com.example.todoapp.data

import retrofit2.http.GET
import retrofit2.http.Path

interface TodoApiService {
    @GET("todos")
    suspend fun getTodos(): List<Todo>

    @GET("todos/{id}")
    suspend fun getTodo(@Path("id") id: Int): Todo

    @GET("posts")
    suspend fun getPosts(): List<Post>
}