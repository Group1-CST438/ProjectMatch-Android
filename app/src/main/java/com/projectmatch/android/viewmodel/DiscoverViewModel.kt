package com.projectmatch.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectmatch.android.data.model.MajorTag
import com.projectmatch.android.data.model.Project
import com.projectmatch.android.data.model.ProjectStatus
import com.projectmatch.android.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiscoverViewModel : ViewModel() {
    private val repo = ProjectRepository()

    private val _allProjects = MutableStateFlow<List<Project>>(emptyList())
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _majorFilter = MutableStateFlow<MajorTag?>(null)
    val majorFilter: StateFlow<MajorTag?> = _majorFilter.asStateFlow()

    private val _statusFilter = MutableStateFlow<ProjectStatus?>(null)
    val statusFilter: StateFlow<ProjectStatus?> = _statusFilter.asStateFlow()

    private val _page = MutableStateFlow(1)
    private val PAGE_SIZE = 9

    private val _interested = MutableStateFlow<Set<String>>(emptySet())
    val interested: StateFlow<Set<String>> = _interested.asStateFlow()

    private val _passed = MutableStateFlow<Set<String>>(emptySet())
    val passed: StateFlow<Set<String>> = _passed.asStateFlow()

    val projects: StateFlow<List<Project>> get() = _filteredProjects
    private val _filteredProjects = MutableStateFlow<List<Project>>(emptyList())

    private val _hasMore = MutableStateFlow(false)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val all = repo.getProjects()
                _allProjects.value = all
                _page.value = 1
                applyFilters()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMore() {
        _page.value += 1
        applyFilters()
    }

    fun setMajorFilter(tag: MajorTag?) {
        _majorFilter.value = tag
        _page.value = 1
        applyFilters()
    }

    fun setStatusFilter(status: ProjectStatus?) {
        _statusFilter.value = status
        _page.value = 1
        applyFilters()
    }

    private fun applyFilters() {
        val major = _majorFilter.value
        val status = _statusFilter.value
        val filtered = _allProjects.value.filter { p ->
            (major == null || major in p.majors) &&
            (status == null || p.status == status)
        }
        val end = _page.value * PAGE_SIZE
        _filteredProjects.value = filtered.take(end)
        _hasMore.value = filtered.size > end
    }

    fun expressInterest(id: String) {
        _interested.value = _interested.value + id
    }

    fun passProject(id: String) {
        _passed.value = _passed.value + id
    }
}
