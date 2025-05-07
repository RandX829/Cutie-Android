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

package tokyo.randx.cutie.todo.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import tokyo.randx.cutie.todo.data.model.TaskDocument
import javax.inject.Inject

interface TaskRepository {
    fun getTasks(userId: String): Flow<List<TaskDocument>>
    suspend fun getTask(taskId: String): TaskDocument?
    suspend fun createTask(userId: String, task: TaskDocument)
    suspend fun updateTask(task: TaskDocument)
    suspend fun deleteTask(taskId: String)
}

class TaskRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
) : TaskRepository {
    private val collection = firestore.collection(TASKS)

    override fun getTasks(userId: String): Flow<List<TaskDocument>> {
        return collection
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(COMPLETED, false)
            .orderBy(CREATED_AT, Query.Direction.DESCENDING)
            .dataObjects<TaskDocument>()
    }

    override suspend fun getTask(taskId: String): TaskDocument? =
        collection
            .document(taskId)
            .get()
            .await()
            .toObject(TaskDocument::class.java)

    override suspend fun createTask(userId: String, task: TaskDocument) {
        val newTask = task.copy(userId = userId)
        collection
            .add(newTask)
            .await()
    }

    override suspend fun updateTask(task: TaskDocument) {
        collection
            .document(task.id)
            .set(task)
            .await()
    }

    override suspend fun deleteTask(taskId: String) {
        collection
            .document(taskId)
            .delete()
            .await()
    }

    companion object {
        private const val TASKS = "tasks"
        private const val COMPLETED = "completed"
        private const val USER_ID = "userId"
        private const val CREATED_AT = "createdAt"
    }
}