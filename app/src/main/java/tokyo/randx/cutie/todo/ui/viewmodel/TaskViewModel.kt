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

package tokyo.randx.cutie.todo.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tokyo.randx.cutie.core.viewmodel.BaseViewModel
import tokyo.randx.cutie.todo.domain.model.Task
import tokyo.randx.cutie.todo.domain.usecase.GetTasksUseCase
import tokyo.randx.cutie.todo.domain.usecase.UpdateTaskUseCase
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(TaskListUiState())
    val state: StateFlow<TaskListUiState> = _state

    init {
        getTasks()
    }

    fun getTasks() {
        _state.value = state.value.copy(isLoading = true)
        launchCatching {
            getTasksUseCase()
                ?.collect { tasks ->
                    _state.value = state.value.copy(tasks = tasks, isLoading = false)
                }
        }
    }

    fun updateTask(task: Task) {
        launchCatching {
            updateTaskUseCase(task)
            getTasks()
        }
    }
}

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)
