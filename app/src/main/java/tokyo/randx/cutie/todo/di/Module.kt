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

package tokyo.randx.cutie.todo.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tokyo.randx.cutie.account.domain.usecase.GetCurrentUserUseCase
import tokyo.randx.cutie.todo.data.repository.TaskRepository
import tokyo.randx.cutie.todo.data.repository.TaskRepositoryImpl
import tokyo.randx.cutie.todo.domain.usecase.CreateTaskUseCase
import tokyo.randx.cutie.todo.domain.usecase.GetTaskUseCase
import tokyo.randx.cutie.todo.domain.usecase.GetTasksUseCase

@Module
@InstallIn(SingletonComponent::class)
object ProvideModule {
    @Provides
    fun provideGetTasksUseCase(
        getCurrentUserUseCase: GetCurrentUserUseCase,
        taskRepository: TaskRepository
    ): GetTasksUseCase =
        GetTasksUseCase(getCurrentUserUseCase, taskRepository)
    @Provides
    fun provideCreateTaskUseCase(
        getCurrentUserUseCase: GetCurrentUserUseCase,
        taskRepository: TaskRepository
    ): CreateTaskUseCase =
        CreateTaskUseCase(getCurrentUserUseCase, taskRepository)
    @Provides
    fun provideGetTaskUseCase(
        taskRepository: TaskRepository
    ): GetTaskUseCase =
        GetTaskUseCase(taskRepository)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}