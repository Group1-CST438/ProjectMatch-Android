package com.projectmatch.android.viewmodel

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val android.content.Context.dataStore: DataStore<Preferences>
    by preferencesDataStore(name = "projectmatch_profile")

val PRESET_ROLES = listOf("Developer", "Designer", "Producer", "Writer", "Business", "Marketing")
val PRESET_SKILLS = listOf("React", "Python", "Figma", "Premiere Pro", "After Effects", "Node.js", "Swift")

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    private val _bio = MutableStateFlow("")
    val bio: StateFlow<String> = _bio.asStateFlow()

    private val _roles = MutableStateFlow<Set<String>>(emptySet())
    val roles: StateFlow<Set<String>> = _roles.asStateFlow()

    private val _skills = MutableStateFlow<Set<String>>(emptySet())
    val skills: StateFlow<Set<String>> = _skills.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved: StateFlow<Boolean> = _saved.asStateFlow()

    private var uid: String = "guest"

    fun loadProfile(userId: String) {
        uid = userId
        viewModelScope.launch {
            val prefs = context.dataStore.data.first()
            _bio.value = prefs[stringPreferencesKey("bio_$uid")] ?: ""
            _roles.value = prefs[stringSetPreferencesKey("roles_$uid")] ?: emptySet()
            _skills.value = prefs[stringSetPreferencesKey("skills_$uid")] ?: emptySet()
        }
    }

    fun setBio(value: String) { _bio.value = value }

    fun toggleRole(role: String) {
        _roles.value = if (role in _roles.value) _roles.value - role else _roles.value + role
        saveProfile()
    }

    fun addSkill(skill: String) {
        if (skill.isBlank()) return
        _skills.value = _skills.value + skill.trim()
        saveProfile()
    }

    fun removeSkill(skill: String) {
        _skills.value = _skills.value - skill
        saveProfile()
    }

    fun saveProfile() {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[stringPreferencesKey("bio_$uid")] = _bio.value
                prefs[stringSetPreferencesKey("roles_$uid")] = _roles.value
                prefs[stringSetPreferencesKey("skills_$uid")] = _skills.value
            }
            _saved.value = true
            kotlinx.coroutines.delay(2000)
            _saved.value = false
        }
    }
}
