package com.projectmatch.android.data.repository

import com.projectmatch.android.data.model.BackendProject
import com.projectmatch.android.data.model.CreateProjectRequest
import com.projectmatch.android.data.model.MOCK_PROJECTS
import com.projectmatch.android.data.model.Project
import com.projectmatch.android.data.model.UpdateProjectRequest
import com.projectmatch.android.data.model.toProject
import com.projectmatch.android.data.remote.RetrofitClient

class ProjectRepository {
    private val api = RetrofitClient.api

    suspend fun getProjects(): List<Project> {
        return try {
            val backend = api.getProjects()
            if (backend.isEmpty()) MOCK_PROJECTS else backend.map { it.toProject() }
        } catch (_: Exception) {
            MOCK_PROJECTS
        }
    }

    suspend fun createProject(
        title: String,
        description: String? = null,
        type: String? = null,
        county: String? = null,
    ): BackendProject {
        return api.createProject(
            CreateProjectRequest(
                title = title,
                generalDescription = description,
                type = type,
                county = county,
            )
        )
    }

    suspend fun updateProject(
        id: String,
        title: String? = null,
        description: String? = null,
        type: String? = null,
        county: String? = null,
    ): BackendProject {
        return api.updateProject(id, UpdateProjectRequest(title, description, type, county))
    }

    suspend fun deleteProject(id: String) {
        api.deleteProject(id)
    }
}
