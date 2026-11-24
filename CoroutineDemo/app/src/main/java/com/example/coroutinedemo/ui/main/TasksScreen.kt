package com.example.coroutinedemo.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TasksContent(
        state = state,
        onLoad = { viewModel.loadData(shouldFail = false) },
        onLoadFail = { viewModel.loadData(shouldFail = true) },
        onCancel = { viewModel.cancelLoading() },
        onTap = { viewModel.onTap() },
        onBlockUI = { viewModel.blockMainThread() },
        onSort = { viewModel.sortTasks() },
        modifier = modifier
    )
}

@Composable
fun TasksContent(
    state: TasksUiState,
    onLoad: () -> Unit,
    onLoadFail: () -> Unit,
    onCancel: () -> Unit,
    onTap: () -> Unit,
    onBlockUI: () -> Unit,
    onSort: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Coroutine Demo",
            style = MaterialTheme.typography.headlineSmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onLoad,
                enabled = !state.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Load")
            }
            Button(
                onClick = onLoadFail,
                enabled = !state.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Load (fail)")
            }
            Button(
                onClick = onCancel,
                enabled = state.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onTap,
                modifier = Modifier.weight(1f)
            ) {
                Text("Tap me: ${state.tapCount}")
            }
            Button(
                onClick = onBlockUI,
                modifier = Modifier.weight(1f)
            ) {
                Text("Block UI")
            }
            Button(
                onClick = onSort,
                enabled = state.tasks.isNotEmpty() && !state.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Sort")
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        if (state.errorMessage != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = onLoad) {
                    Text("Retry")
                }
            }
        }

        if (state.announcement.isNotEmpty()) {
            Text(
                text = "Announcement: ${state.announcement} (Active Courses: ${state.courseCount})",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.tasks, key = { it.id }) { task ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.course,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
