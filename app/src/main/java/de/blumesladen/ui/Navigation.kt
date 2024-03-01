/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.blumesladen.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.blumesladen.R
import de.blumesladen.ui.diaryentry.DiaryEntryEditDialog
import de.blumesladen.ui.diaryentry.DiaryEntryScreen

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object DiaryEntryEditRoute : Screen("rDiaryEntryEdit", R.string.rDiaryEntryEdit)
    object DiaryEntryViewRoute : Screen("rDiaryEntryView", R.string.rDiaryEntryView)
}

val items = listOf(
    Screen.DiaryEntryEditRoute,
    Screen.DiaryEntryViewRoute,
)
val itemsIcons = listOf(
    R.drawable.edit_calendar,
    R.drawable.calendar_month
)

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = MaterialTheme.colorScheme.secondaryContainer)
            {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.zip(itemsIcons).forEach { screenPair ->
                    BottomNavigationItem(
                        icon = { Icon(painterResource(id = screenPair.component2()), contentDescription = null) },
                        label = { Text(stringResource(screenPair.component1().resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screenPair.component1().route } == true,
                        onClick = {
                            navController.navigate(screenPair.component1().route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.DiaryEntryEditRoute.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.DiaryEntryEditRoute.route) {
                DiaryEntryEditDialog(navController, modifier = Modifier.padding(16.dp))
            }
            // TODO: Add more destinations
            composable(Screen.DiaryEntryViewRoute.route) {
                DiaryEntryScreen(navController, modifier = Modifier.padding(16.dp))
            }
        }
    }

}