package com.projectmatch.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmatch.android.data.model.BackendProject
import com.projectmatch.android.data.model.toProject
import com.projectmatch.android.data.model.Project
import com.projectmatch.android.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProjectsViewModel : ViewModel() {
    private val repo = ProjectRepository()

    private val _projects = MutableStateFlow<List<BackendProject>>(emptyList())
    val projects: StateFlow<List<BackendProject>> = _projects.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    val filtered: StateFlow<List<BackendProject>> get() = _filtered
    private val _filtered = MutableStateFlow<List<BackendProject>>(emptyList())

    init {
        loadProjects()
    }

    fun loadProjects() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = repo.getProjects()
                // Store as BackendProject for CRUD by fetching raw list
                val raw = try {
                    com.projectmatch.android.data.remote.RetrofitClient.api.getProjects()
                } catch (_: Exception) { emptyList() }
                _projects.value = raw
                applySearch()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun setSearch(query: String) {
        _search.value = query
        applySearch()
    }

    private fun applySearch() {
        val q = _search.value.lowercase()
        _filtered.value = if (q.isBlank()) _projects.value
        else _projects.value.filter {
            it.title.lowercase().contains(q) ||
            (it.generalDescription?.lowercase()?.contains(q) == true)
        }
    }

    fun createProject(title: String, description: String, type: String, county: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repo.createProject(title, description, type, county)
                loadProjects()
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun updateProject(id: String, title: String, description: String, type: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repo.updateProject(id, title, description, type)
                loadProjects()
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun deleteProject(id: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repo.deleteProject(id)
                loadProjects()
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }
}
