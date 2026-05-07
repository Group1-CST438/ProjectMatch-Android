package com.projectmatch.android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.projectmatch.android.ui.components.BottomNavBar
import com.projectmatch.android.ui.screens.DiscoverScreen
import com.projectmatch.android.ui.screens.HomeScreen
import com.projectmatch.android.ui.screens.LoginScreen
import com.projectmatch.android.ui.screens.MatchesScreen
import com.projectmatch.android.ui.screens.NewProjectScreen
import com.projectmatch.android.ui.screens.ProfileScreen
import com.projectmatch.android.ui.screens.ProjectsScreen
import com.projectmatch.android.ui.screens.SavedScreen
import com.projectmatch.android.ui.screens.SignupScreen
import com.projectmatch.android.viewmodel.AuthViewModel
import com.projectmatch.android.viewmodel.DiscoverViewModel
import com.projectmatch.android.viewmodel.ProfileViewModel
import com.projectmatch.android.viewmodel.ProjectsViewModel

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
            LoginScreen(
                authViewModel = authViewModel,
                onSuccess = {
                    rootNav.navigate("main") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onSignup = { rootNav.navigate("signup") },
            )
        }
        composable("signup") {
            SignupScreen(
                authViewModel = authViewModel,
                onSuccess = {
                    rootNav.navigate("main") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onLogin = { rootNav.navigate("login") },
            )
        }
        composable("main") {
            MainScaffold(
                authViewModel = authViewModel,
                onSignOut = {
                    authViewModel.signOut()
                    rootNav.navigate("home") {
                        popUpTo("main") { inclusive = true }
                    }
                },
            )
        }
    }
}

@Composable
private fun MainScaffold(
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit,
) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: "discover"

    val bottomRoutes = setOf("discover", "projects", "matches", "saved", "profile")

    val discoverVm: DiscoverViewModel = viewModel()
    val projectsVm: ProjectsViewModel = viewModel()
    val profileVm: ProfileViewModel = viewModel()

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomRoutes) {
                BottomNavBar(currentRoute = currentRoute) { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "discover",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("discover") {
                DiscoverScreen(
                    viewModel = discoverVm,
                    onPostProject = { navController.navigate("new_project") },
                )
            }
            composable("projects") {
                ProjectsScreen(
                    viewModel = projectsVm,
                    onNewProject = { navController.navigate("new_project") },
                )
            }
            composable("new_project") {
                NewProjectScreen(
                    onBack = { navController.popBackStack() },
                    onCreated = { navController.popBackStack() },
                )
            }
            composable("matches") {
                MatchesScreen()
            }
            composable("saved") {
                SavedScreen()
            }
            composable("profile") {
                ProfileScreen(
                    viewModel = profileVm,
                    authViewModel = authViewModel,
                    onSignOut = onSignOut,
                )
            }
        }
    }
}
