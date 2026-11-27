package com.example.minicampus.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.minicampus.ui.screens.CourseDetailScreen
import com.example.minicampus.ui.screens.CoursesScreen
import com.example.minicampus.ui.screens.HomeScreen
import com.example.minicampus.viewmodel.CourseViewModel

data class TopLevelDestination<T : Any>(
    val name: String,
    val route: T,
    val icon: ImageVector,
)

val topLevelDestinations = listOf(
    TopLevelDestination(
        name = "Beranda",
        route = Destination.Home,
        icon = Icons.Default.Home,
    ),
    TopLevelDestination(
        name = "Mata Kuliah",
        route = Destination.Courses,
        icon = Icons.AutoMirrored.Filled.List
    )
)

@Composable
fun CampusApp(
    viewModel: CourseViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                topLevelDestinations.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(item.route::class)
                    } == true

                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.name) },
                        label = { Text(item.name) },
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Destination.Home> {
                HomeScreen(
                    viewModel = viewModel,
                    onCourseClick = { courseId ->
                        navController.navigate(Destination.CourseDetail(courseId))
                    },
                    onViewAllCoursesClick = {
                        navController.navigate(Destination.Courses) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable<Destination.Courses> {
                CoursesScreen(
                    viewModel = viewModel,
                    onCourseClick = { courseId ->
                        navController.navigate(Destination.CourseDetail(courseId))
                    }
                )
            }
            composable<Destination.CourseDetail> { backStackEntry ->
                val detailRoute: Destination.CourseDetail = backStackEntry.toRoute()
                CourseDetailScreen(
                    courseId = detailRoute.courseId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
