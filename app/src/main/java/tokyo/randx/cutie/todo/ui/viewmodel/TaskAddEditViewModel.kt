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
import kotlinx.coroutines.flow.asStateFlow
import tokyo.randx.cutie.R
import tokyo.randx.cutie.core.SnackbarManager
import tokyo.randx.cutie.core.viewmodel.BaseViewModel
import tokyo.randx.cutie.todo.domain.usecase.CreateTaskUseCase
import tokyo.randx.cutie.todo.domain.model.Task
import tokyo.randx.cutie.todo.domain.usecase.GetTaskUseCase
import tokyo.randx.cutie.todo.domain.usecase.UpdateTaskUseCase
import javax.inject.Inject

@HiltViewModel
class TaskAddEditViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
    ) : BaseViewModel() {
    private val _state = MutableStateFlow(TaskAddEditUiState())
    val state: StateFlow<TaskAddEditUiState> = _state.asStateFlow()

    fun initTask(taskId: String?) {
        taskId?.let {
            _state.value = state.value.copy(isEdit = true)

            launchCatching {
                getTaskUseCase(taskId)?.let {
                    _state.value = state.value.copy(task = it)
                }
            }
        }
    }

    fun onTitleChange(newValue: String) {
        val newTask = state.value.task.copy(title = newValue)
        _state.value = state.value.copy(task = newTask)
    }

    fun onDetailChange(newValue: String) {
        val newTask = state.value.task.copy(detail = newValue)
        _state.value = state.value.copy(task = newTask)
    }

    fun onConfirmClick(onSuccess: () -> Unit) {
        if (state.value.task.title.isBlank()) {
            SnackbarManager.showSnackbar(R.string.error_empty_title)
            return
        }

        launchCatching {
            val task = state.value.task
            if (state.value.isEdit) {
                updateTaskUseCase(task)
                onSuccess()
            } else {
                createTaskUseCase(task)
                onSuccess()
            }
        }
    }
}

data class TaskAddEditUiState(
    val task: Task = Task(),
    val isEdit: Boolean = false
)