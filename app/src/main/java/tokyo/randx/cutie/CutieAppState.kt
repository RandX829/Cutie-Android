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

package tokyo.randx.cutie

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import tokyo.randx.cutie.core.SnackbarManager
import tokyo.randx.cutie.core.model.SnackbarMessage.Companion.toStringMessage

@Stable
class CutieAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val snackbarHostState: SnackbarHostState,
    val resources: Resources,
    val snackbarManager: SnackbarManager
) {
    init {
        coroutineScope.launch {
            SnackbarManager.message.filterNotNull().collect { snackbarMessage ->
                val message = snackbarMessage.toStringMessage(resources)
                snackbarHostState.showSnackbar(message = message)
                SnackbarManager.clearSnackbarMessage()
            }
        }
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigateBack() {
        val canNavigateUp = navController.previousBackStackEntry != null
        if (!canNavigateUp) return

        navController.navigateUp()
    }

    fun navigate(route: String) {
        navController.navigate(route)
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}

@Composable
fun rememberCutieAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember{ SnackbarHostState() },
    resources: Resources = resources(),
    snackbarManager: SnackbarManager = SnackbarManager
): CutieAppState =
    remember(
        navController,
        coroutineScope,
        snackbarHostState,
        resources,
        snackbarManager
    ) {
    CutieAppState(
        navController = navController,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState,
        resources = resources,
        snackbarManager = snackbarManager
    )
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return  LocalContext.current.resources
}