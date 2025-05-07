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

package tokyo.randx.cutie.core.model

import android.content.res.Resources
import androidx.annotation.StringRes
import tokyo.randx.cutie.R

sealed class SnackbarMessage {
    class StringMessage(val message: String) : SnackbarMessage()
    class ResourceMessage(@StringRes val resId: Int) : SnackbarMessage()

    companion object {
        fun SnackbarMessage.toStringMessage(resources: Resources): String {
            return when (this) {
                is StringMessage -> this.message
                is ResourceMessage -> resources.getString(this.resId)
            }
        }

        fun Throwable.toSnackbarMessage(): SnackbarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringMessage(message)
            else ResourceMessage(R.string.generic_error)
        }
    }
}