package com.projectmatch.android.data.remote

import com.projectmatch.android.data.model.BackendProject
import com.projectmatch.android.data.model.CreateProjectRequest
import com.projectmatch.android.data.model.UpdateProjectRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("projects")
    suspend fun getProjects(): List<BackendProject>

    @GET("projects/{id}")
    suspend fun getProject(@Path("id") id: String): BackendProject

    @POST("projects")
    suspend fun createProject(@Body body: CreateProjectRequest): BackendProject

    @PATCH("projects/{id}")
    suspend fun updateProject(@Path("id") id: String, @Body body: UpdateProjectRequest): BackendProject

    @DELETE("projects/{id}")
    suspend fun deleteProject(@Path("id") id: String)
}
