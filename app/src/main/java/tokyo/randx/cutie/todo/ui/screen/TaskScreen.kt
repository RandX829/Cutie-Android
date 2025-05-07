/*
 * Copyright 2025 RandX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tokyo.randx.cutie.todo.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tokyo.randx.cutie.R
import tokyo.randx.cutie.core.ui.component.CutieTopAppBar
import tokyo.randx.cutie.todo.domain.model.Task
import tokyo.randx.cutie.todo.ui.viewmodel.TaskViewModel

@Composable
fun TaskScreen(
    onAddClick: () -> Unit,
    onEditClick: (Task) -> Unit,
    onBack: () -> Unit,
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    val state by taskViewModel.state.collectAsStateWithLifecycle()
    val tasks = state.tasks

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CutieTopAppBar(
                title = R.string.todo,
                actionIcon = Icons.Default.Refresh,
                onAction = { taskViewModel.getTasks() },
                canNavigateBack = true,
                onBack = onBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.shadow(8.dp, RoundedCornerShape(16.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        TaskListScreen(
            modifier = Modifier.padding(paddingValues),
            tasks = tasks,
            onEditClick = { onEditClick(it) },
            onCompleteClick = { taskViewModel.updateTask(it) }
        )
    }
}
