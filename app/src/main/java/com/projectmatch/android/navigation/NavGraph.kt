package com.projectmatch.android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.projectmatch.android.ui.components.BottomNavBar
import com.projectmatch.android.ui.screens.*
import com.projectmatch.android.viewmodel.*

@Composable
fun ProjectMatchNavGraph(authViewModel: AuthViewModel) {
    val rootNav = rememberNavController()
    val user by authViewModel.user.collectAsState()
    val loading by authViewModel.loading.collectAsState()

    if (loading) return

    val startDest = if (user != null) "main" else "home"

    NavHost(navController = rootNav, startDestination = startDest) {
        composable("home") {
            HomeScreen(
                onBrowse = { rootNav.navigate("main") },
                onPost = { rootNav.navigate("main") },
                onLogin = { rootNav.navigate("login") },
                onSignup = { rootNav.navigate("signup") },
            )
        }

        composable("login") {
            LoginScreen(authViewModel) {
                rootNav.navigate("main") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }

        composable("signup") {
            SignupScreen(authViewModel) {
                rootNav.navigate("main") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }

        composable("main") {
            MainScaffold(authViewModel) {
                authViewModel.signOut()
                rootNav.navigate("home") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
    }
}

@Composable
private fun MainScaffold(
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit,
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "discover"

    val discoverVm: DiscoverViewModel = viewModel()
    val projectsVm: ProjectsViewModel = viewModel()
    val profileVm: ProfileViewModel = viewModel()

    Scaffold(
        bottomBar = {
            BottomNavBar(currentRoute) { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "discover",
            modifier = Modifier.padding(padding),
        ) {
            composable("discover") {
                DiscoverScreen(
                    viewModel = discoverVm,
                    onPostProject = { navController.navigate("new_project") },
                )
            }

            composable("projects") {
                ProjectsScreen(projectsVm) {
                    navController.navigate("new_project")
                }
            }

            composable("new_project") {
                NewProjectScreen(
                    onBack = { navController.popBackStack() },
                    onCreated = { navController.popBackStack() },
                )
            }

            composable("matches") { MatchesScreen() }
            composable("saved") { SavedScreen() }

            composable("profile") {
                ProfileScreen(profileVm, authViewModel, onSignOut)
            }
        }
    }
}