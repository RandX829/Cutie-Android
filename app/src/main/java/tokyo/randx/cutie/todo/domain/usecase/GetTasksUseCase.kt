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

package tokyo.randx.cutie.todo.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tokyo.randx.cutie.account.domain.usecase.GetCurrentUserUseCase
import tokyo.randx.cutie.todo.data.repository.TaskRepository
import tokyo.randx.cutie.todo.data.model.toModel
import tokyo.randx.cutie.todo.domain.model.Task
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>>? =
        getCurrentUserUseCase()?.let { user ->
            taskRepository.getTasks(user.uid)
                .map { it.toModel() }
        }
}