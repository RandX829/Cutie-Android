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

package tokyo.randx.cutie.account.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tokyo.randx.cutie.R
import tokyo.randx.cutie.account.ui.viewmodel.AccountViewModel
import tokyo.randx.cutie.core.ui.component.CutieTopAppBar


@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val state by accountViewModel.state.collectAsStateWithLifecycle()
    val currentUser = state.currentUser
    val name = state.name
    val email = state.email
    val password = state.password
    val passwordConfirm = state.passwordConfirm
    val isPasswordVisible = state.isPasswordVisible
    val isSignUp = state.isSignUp

    Scaffold(
        topBar = {
            CutieTopAppBar(
                title = R.string.account,
                canNavigateBack = true,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        if (currentUser != null && !currentUser.isAnonymous) {
            ProfileScreen(
                modifier = Modifier.padding(paddingValues),
                user = currentUser,
                onSignOut = { accountViewModel.signOut() }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    modifier = Modifier.size(width = 128.dp, height = 128.dp),
                    painter = painterResource(R.drawable.cutie),
                    contentDescription = stringResource(R.string.logo)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isSignUp) stringResource(R.string.create_an_account) else stringResource(R.string.sign_in_to_continue),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(32.dp))

                if (isSignUp) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = name,
                        onValueChange = accountViewModel::onNameChange,
                        label = { Text(text = stringResource(R.string.name)) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = stringResource(R.string.name)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = accountViewModel::onEmailChange,
                    label = { Text(text = stringResource(R.string.email)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = stringResource(R.string.email)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = accountViewModel::onPasswordChange,
                    label = { Text(text = stringResource(R.string.password)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = stringResource(R.string.password)
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { accountViewModel.onPasswordVisibilityChange(!isPasswordVisible) }
                        ) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = stringResource(R.string.password_visibility)
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (isSignUp) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = passwordConfirm,
                        onValueChange = accountViewModel::onPasswordConfirmChange,
                        label = { Text(text = stringResource(R.string.password_visibility)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = stringResource(R.string.confirm_password)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { accountViewModel.onPasswordVisibilityChange(!isPasswordVisible) }
                            ) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = stringResource(R.string.password_visibility)
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = { accountViewModel.onConfirmClick(onSuccess) },
                    enabled = email.isNotBlank() && password.isNotBlank()
                ) {
                    Text(
                        text = if (isSignUp) stringResource(R.string.sign_up) else stringResource(R.string.sign_in),
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSignUp) stringResource(R.string.already_have_account) else stringResource(R.string.do_not_have_account)
                    )
                    Text(
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { accountViewModel.onSwitch(!isSignUp) },
                        text = if (isSignUp) stringResource(R.string.sign_in) else stringResource(R.string.sign_up),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
