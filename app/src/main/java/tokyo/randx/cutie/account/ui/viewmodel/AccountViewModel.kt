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

package tokyo.randx.cutie.account.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tokyo.randx.cutie.R
import tokyo.randx.cutie.core.SnackbarManager
import tokyo.randx.cutie.account.domain.usecase.GetCurrentUserUseCase
import tokyo.randx.cutie.account.domain.usecase.SignInUseCase
import tokyo.randx.cutie.account.domain.usecase.SignOutUseCase
import tokyo.randx.cutie.account.domain.usecase.SignUpUseCase
import tokyo.randx.cutie.account.domain.model.User
import tokyo.randx.cutie.core.viewmodel.BaseViewModel
import tokyo.randx.cutie.core.extension.isValidEmail
import tokyo.randx.cutie.core.extension.isValidPassword
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(AccountUiState())
    val state: StateFlow<AccountUiState> = _state.asStateFlow()

    init {
        getCurrentUser()
    }

    fun onNameChange(newValue: String) {
        _state.value = state.value.copy(name = newValue)
    }

    fun onEmailChange(newValue: String) {
        _state.value = state.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _state.value = state.value.copy(password = newValue)
    }

    fun onPasswordConfirmChange(newValue: String) {
        _state.value = state.value.copy(passwordConfirm = newValue)
    }

    fun onPasswordVisibilityChange(newValue: Boolean) {
        _state.value = state.value.copy(isPasswordVisible = newValue)
    }

    fun onConfirmClick(onSuccess: () -> Unit) {
        val name = state.value.name
        val email = state.value.email
        val password = state.value.password
        val passwordConfirm = state.value.passwordConfirm
        val isSignUp = state.value.isSignUp

        if (email.isBlank() || password.isBlank()) {
            SnackbarManager.showSnackbar(R.string.error_empty_email_password)
            return
        }

        if (isSignUp && name.isBlank()) {
            SnackbarManager.showSnackbar(R.string.error_empty_name)
            return
        }

        if (!email.isValidEmail()) {
            SnackbarManager.showSnackbar(R.string.error_invalid_email)
            return
        }

        if (password.isValidPassword()) {
            SnackbarManager.showSnackbar(R.string.error_unsafe_password)
            return
        }

        if (isSignUp && password != passwordConfirm) {
            SnackbarManager.showSnackbar(R.string.error_passwords_not_match)
            return
        }

        launchCatching {
            if (isSignUp) {
                signUpUseCase(name, email, password)
                _state.value = state.value.copy(isSignUp = false)
            } else {
                signInUseCase(email, password)
                onSuccess()
            }
        }
    }

    fun onSwitch(newValue: Boolean) {
        _state.value = state.value.copy(isSignUp = newValue)
    }

    fun signOut() {
        launchCatching {
            signOutUseCase()
            getCurrentUser()
        }
    }

    private fun getCurrentUser() =
        getCurrentUserUseCase().also {
            _state.value = state.value.copy(currentUser = it)
        }
}

data class AccountUiState(
    val currentUser: User? = null,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isPasswordVisible: Boolean = false,
    val isSignUp: Boolean = false
)
