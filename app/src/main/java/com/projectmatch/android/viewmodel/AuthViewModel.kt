package com.projectmatch.android.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.projectmatch.android.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    val supabase: SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_KEY,
    ) {
        install(Auth)
    }

    private val _user = MutableStateFlow<UserInfo?>(null)
    val user: StateFlow<UserInfo?> = _user.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                supabase.auth.awaitInitialization()
                _user.value = supabase.auth.currentUserOrNull()
            } catch (_: Exception) {
                _user.value = null
            } finally {
                _loading.value = false
            }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            try {
                supabase.auth.signInWith(Google, redirectUrl = "projectmatch://auth/callback")
            } catch (_: Exception) { /* handled by callback */ }
        }
    }

    fun handleAuthCallback(intent: Intent) {
        val uri: Uri = intent.data ?: return
        viewModelScope.launch {
            try {
                supabase.auth.exchangeCodeForSession(uri.toString())
                _user.value = supabase.auth.currentUserOrNull()
            } catch (_: Exception) { }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
            } catch (_: Exception) { }
            _user.value = null
        }
    }

    fun currentUserEmail(): String? = _user.value?.email
    fun currentUserInitial(): String = _user.value?.email?.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
}
