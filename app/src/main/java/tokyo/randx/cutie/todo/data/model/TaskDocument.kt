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

package tokyo.randx.cutie.todo.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import tokyo.randx.cutie.todo.domain.model.Task
import java.util.Date

data class TaskDocument(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val detail: String? = null,
    val completed: Boolean = false,
    val userId: String = "",
    @ServerTimestamp
    val createdAt: Date = Date()
)

fun TaskDocument.toModel() =
    Task(
        id = this.id,
        title = this.title,
        detail = this.detail,
        isCompleted = this.completed,
        userId = this.userId
    )

fun List<TaskDocument>.toModel() =
    this.map {
        Task(
            id = it.id,
            title = it.title,
            detail = it.detail,
            isCompleted = it.completed,
            userId = it.userId
        )
    }