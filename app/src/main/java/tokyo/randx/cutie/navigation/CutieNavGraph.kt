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

package tokyo.randx.cutie.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import tokyo.randx.cutie.CutieAppState
import tokyo.randx.cutie.account.ui.screen.AccountScreen
import tokyo.randx.cutie.home.HomeScreen
import tokyo.randx.cutie.ledger.ui.screen.LedgerScreen
import tokyo.randx.cutie.todo.ui.screen.TaskAddEditScreen
import tokyo.randx.cutie.todo.ui.screen.TaskScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CutieNavGraph(
    appState: CutieAppState
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState,
                modifier = Modifier.padding(8.dp),
                snackbar = { snackbarData ->
                    Snackbar(snackbarData)
                }
            )
        },
    ) {
        NavHost(
            navController = appState.navController,
            startDestination = CutieRoute.Home.name
        ) {
            composable(route = CutieRoute.Home.name) {
                HomeScreen(
                    onSignInClick = { appState.navigate(CutieRoute.Account.name) },
                    onLedgerClick = { appState.navigate(route = CutieRoute.Ledger.name) },
                    onTodoClick = { appState.navigate(route = CutieRoute.Todo.name) }
                )
            }
            composable(route = CutieRoute.Account.name) {
                AccountScreen(
                    onBack = { appState.navigateBack() },
                    onSuccess = { appState.navigateBack() }
                )
            }
            composable(route = CutieRoute.Todo.name) {
                TaskScreen(
                    onAddClick = { appState.navigate(CutieRoute.TodoAdd.name) },
                    onEditClick = { appState.navigate(route = CutieRoute.TodoEdit.name.plus("/${it.id}")) },
                    onBack = { appState.navigateBack() }
                )
            }
            composable(route = CutieRoute.TodoAdd.name) {
                TaskAddEditScreen(
                    onSuccess = { appState.navigateBack() },
                    onCancel = { appState.navigateBack() }
                )
            }
            composable(
                route = CutieRoute.TodoEdit.name.plus("/{taskId}"),
                arguments = listOf(navArgument("taskId"){
                    type = NavType.StringType
                })
            ) {
                TaskAddEditScreen(
                    taskId = it.arguments?.getString("taskId"),
                    onSuccess = { appState.navigateBack() },
                    onCancel = { appState.navigateBack() }
                )
            }
            composable(route = CutieRoute.Ledger.name) {
                LedgerScreen(
                    onBack = { appState.navigateBack() }
                )
            }
        }
    }
}
