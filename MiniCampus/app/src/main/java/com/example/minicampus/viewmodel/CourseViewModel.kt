package com.example.minicampus.viewmodel

import androidx.lifecycle.ViewModel
import com.example.minicampus.model.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CourseViewModel : ViewModel() {
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    init {
        loadSampleCourses()
    }

    private fun loadSampleCourses() {
        _courses.value = listOf(
            Course(
                id = "1",
                code = "IF101",
                name = "Pemrograman Kotlin & Compose",
                lecturer = "Dr. Budi Santoso, M.Kom",
                credits = 3,
                schedule = "Senin, 08:00 - 10:30",
                room = "Lab Komputer 3",
                description = "Mempelajari dasar-dasar pemrograman Kotlin dan pembuatan UI deklaratif modern menggunakan Jetpack Compose untuk Android.",
                category = "Wajib",
            ),
            Course(
                id = "2",
                code = "IF102",
                name = "Algoritma & Struktur Data",
                lecturer = "Prof. Siti Aminah, Ph.D",
                credits = 4,
                schedule = "Selasa, 10:00 - 13:20",
                room = "Ruang Teori R201",
                description = "Pemahaman struktur data dasar dan lanjut seperti Array, Linked List, Tree, Graph serta analisis kompleksitas algoritma.",
                category = "Wajib",
            ),
            Course(
                id = "3",
                code = "IF201",
                name = "Pengembangan Aplikasi Bergerak",
                lecturer = "Eko Prasetyo, M.T",
                credits = 3,
                schedule = "Rabu, 13:00 - 15:30",
                room = "Lab Mobile App",
                description = "Pengembangan aplikasi Android komprehensif mencakup Architecture Components, StateFlow, Room DB, dan REST API integration.",
                category = "Pilihan",
            ),
            Course(
                id = "4",
                code = "IF202",
                name = "Basis Data & Cloud",
                lecturer = "Rina Wijaya, S.Kom, M.Sc",
                credits = 3,
                schedule = "Kamis, 09:00 - 11:30",
                room = "Ruang R305",
                description = "Perancangan database relasional SQL, NoSQL, serta implementasi layanan cloud database.",
                category = "Wajib",
            ),
        )
    }

    fun getCourseById(id: String): Course? {
        return _courses.value.find { it.id == id }
    }
}
