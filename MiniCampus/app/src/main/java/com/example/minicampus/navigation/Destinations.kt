package com.example.minicampus.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object Home : Destination

    @Serializable
    data object Courses : Destination

    @Serializable
    data class CourseDetail(val courseId: String) : Destination
}
