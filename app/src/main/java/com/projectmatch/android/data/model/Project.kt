package com.projectmatch.android.data.model

import com.google.gson.annotations.SerializedName

enum class ProjectStatus { OPEN, IN_PROGRESS, CLOSED }
enum class MajorTag { CS, FILM, BUSINESS, COMDES }

data class Project(
    val id: String,
    val title: String,
    val description: String,
    val ownerName: String,
    val majors: List<MajorTag>,
    val tags: List<String>,
    val status: ProjectStatus,
    val spotsTotal: Int,
    val spotsFilled: Int,
    val createdAt: String,
) {
    val spotsLeft get() = spotsTotal - spotsFilled
    val canJoin get() = status == ProjectStatus.OPEN && spotsLeft > 0
}

// Raw backend shape from Spring Boot /projects endpoint
data class BackendProject(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String?,
    @SerializedName("title") val title: String,
    @SerializedName("generalDescription") val generalDescription: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("county") val county: String?,
)

fun BackendProject.toProject(): Project = Project(
    id = id,
    title = title,
    description = generalDescription ?: "",
    ownerName = userId ?: "Unknown",
    majors = emptyList(),
    tags = type?.split(", ")?.filter { it.isNotBlank() } ?: emptyList(),
    status = ProjectStatus.OPEN,
    spotsTotal = 0,
    spotsFilled = 0,
    createdAt = "",
)

// Request bodies
data class CreateProjectRequest(
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("title") val title: String,
    @SerializedName("generalDescription") val generalDescription: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("county") val county: String? = null,
)

data class UpdateProjectRequest(
    @SerializedName("title") val title: String? = null,
    @SerializedName("generalDescription") val generalDescription: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("county") val county: String? = null,
)

val MOCK_PROJECTS = listOf(
    Project("1", "Campus Navigator App",
        "A mobile app that helps new students navigate City Tech's campus with indoor wayfinding, event listings, and real-time accessibility updates.",
        "Alex Rivera", listOf(MajorTag.CS, MajorTag.COMDES), listOf("mobile", "react-native", "maps"),
        ProjectStatus.OPEN, 4, 1, "2026-04-20T12:00:00Z"),
    Project("2", "City Tech Documentary: The Commuter",
        "A short documentary exploring the lives of City Tech students who commute two hours each way — their sacrifices, resilience, and dreams.",
        "Maya Chen", listOf(MajorTag.FILM), listOf("documentary", "storytelling", "interview"),
        ProjectStatus.OPEN, 5, 2, "2026-04-18T09:00:00Z"),
    Project("3", "TutorLink — Peer Tutoring Platform",
        "A web platform connecting City Tech students who need tutoring with peers who can help. Features scheduling, ratings, and session tracking.",
        "Jordan Kim", listOf(MajorTag.CS, MajorTag.BUSINESS), listOf("web", "nextjs", "education", "startup"),
        ProjectStatus.OPEN, 6, 3, "2026-04-15T14:00:00Z"),
    Project("4", "CUNY Green Brand Identity System",
        "Designing a cohesive brand identity for a student-led sustainability initiative at CUNY, including logo, typography, color system, and motion guidelines.",
        "Sofia Patel", listOf(MajorTag.COMDES), listOf("branding", "identity", "sustainability", "figma"),
        ProjectStatus.OPEN, 3, 1, "2026-04-12T11:00:00Z"),
    Project("5", "Campus Event Management System",
        "A full-stack platform for managing and discovering campus events, club meetings, and study sessions with notifications and RSVP tracking.",
        "Daniel Torres", listOf(MajorTag.CS), listOf("fullstack", "typescript", "events", "postgres"),
        ProjectStatus.IN_PROGRESS, 4, 3, "2026-03-28T10:00:00Z"),
    Project("6", "Social Media Strategy: Local NYC Businesses",
        "Developing a multi-platform social media campaign for 3 local Brooklyn businesses to increase their digital reach and community engagement.",
        "Priya Williams", listOf(MajorTag.COMDES, MajorTag.BUSINESS), listOf("marketing", "instagram", "content", "strategy"),
        ProjectStatus.OPEN, 4, 2, "2026-04-10T16:00:00Z"),
    Project("7", "AR Art Installation: City as Canvas",
        "An augmented reality experience overlaying digital artwork on 5 City Tech campus locations, viewable via mobile. Blends tech and visual storytelling.",
        "Marcus Johnson", listOf(MajorTag.CS, MajorTag.FILM, MajorTag.COMDES), listOf("ar", "creative-tech", "three.js", "art"),
        ProjectStatus.OPEN, 6, 2, "2026-04-22T08:00:00Z"),
    Project("8", "Career Paths in STEM Podcast",
        "A podcast series interviewing City Tech alumni about their careers in STEM, produced and distributed on Spotify, Apple Podcasts, and YouTube.",
        "Aisha Morgan", listOf(MajorTag.FILM, MajorTag.BUSINESS), listOf("podcast", "audio", "production", "interviews"),
        ProjectStatus.OPEN, 4, 1, "2026-04-08T13:00:00Z"),
    Project("9", "Student Portal UX Redesign",
        "Redesigning the City Tech student portal with modern UX principles — improving navigation, mobile responsiveness, and accessibility for 18,000+ students.",
        "Leo Chang", listOf(MajorTag.COMDES, MajorTag.CS), listOf("ux", "figma", "accessibility", "research"),
        ProjectStatus.IN_PROGRESS, 5, 4, "2026-03-15T09:00:00Z"),
    Project("10", "Brooklyn Startup Case Competition",
        "Organizing and documenting a startup pitch competition for CUNY students, with industry judges, prizes, and a produced highlight reel.",
        "Nadia Foster", listOf(MajorTag.BUSINESS, MajorTag.FILM), listOf("startup", "pitch", "competition", "event"),
        ProjectStatus.OPEN, 8, 4, "2026-04-25T15:00:00Z"),
    Project("11", "Open Source CLI for Dev Environments",
        "Building an open-source command-line tool that helps developers manage multiple project environments. Contributing to an existing GitHub repo.",
        "Sam Ortiz", listOf(MajorTag.CS), listOf("cli", "open-source", "golang", "devtools"),
        ProjectStatus.OPEN, 3, 1, "2026-04-17T12:00:00Z"),
    Project("12", "Sustainability Impact Report Design",
        "Designing an annual sustainability impact report for a NYC nonprofit — infographics, data visualization, and 40-page editorial layout in print and digital.",
        "Emma Liu", listOf(MajorTag.COMDES, MajorTag.BUSINESS), listOf("print", "infographic", "editorial", "nonprofit"),
        ProjectStatus.CLOSED, 3, 3, "2026-03-01T10:00:00Z"),
)
