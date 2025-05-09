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

package tokyo.randx.cutie.core

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tokyo.randx.cutie.core.model.SnackbarMessage

object SnackbarManager {
    private val _message: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
    val message: StateFlow<SnackbarMessage?> = _message.asStateFlow()

    fun showSnackbar(@StringRes resId: Int) {
        _message.value = SnackbarMessage.ResourceMessage(resId)
    }

    fun showSnackbar(message: SnackbarMessage) {
        _message.value = message
    }

    fun clearSnackbarMessage() {
        _message.value = null
    }
}