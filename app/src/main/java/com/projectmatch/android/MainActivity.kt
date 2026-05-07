package com.projectmatch.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.projectmatch.android.navigation.ProjectMatchNavGraph
import com.projectmatch.android.ui.theme.ProjectMatchTheme
import com.projectmatch.android.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle OAuth deep link if launched from callback
        intent?.let { authViewModel.handleAuthCallback(it) }

        setContent {
            ProjectMatchTheme {
                ProjectMatchNavGraph(authViewModel = authViewModel)
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        authViewModel.handleAuthCallback(intent)
    }
}
