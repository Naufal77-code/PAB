package com.example.minicampus.model

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String,
    val code: String,
    val name: String,
    val lecturer: String,
    val credits: Int,
    val schedule: String,
    val room: String,
    val description: String,
    val category: String = "Wajib",
)
