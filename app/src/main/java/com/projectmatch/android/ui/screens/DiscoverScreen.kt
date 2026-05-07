package com.projectmatch.android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.data.model.MajorTag
import com.projectmatch.android.data.model.ProjectStatus
import com.projectmatch.android.ui.components.HexChip
import com.projectmatch.android.ui.components.ProjectCard
import com.projectmatch.android.ui.theme.ButtonAccent
import com.projectmatch.android.ui.theme.ButtonPrimary
import com.projectmatch.android.ui.theme.InputBg
import com.projectmatch.android.ui.theme.PageBackground
import com.projectmatch.android.ui.theme.TextMuted
import com.projectmatch.android.ui.theme.TextPrimary
import com.projectmatch.android.ui.theme.TextSubtle
import com.projectmatch.android.viewmodel.DiscoverViewModel

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    onPostProject: () -> Unit,
) {
    val projects by viewModel.projects.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val majorFilter by viewModel.majorFilter.collectAsState()
    val statusFilter by viewModel.statusFilter.collectAsState()
    val hasMore by viewModel.hasMore.collectAsState()
    val interested by viewModel.interested.collectAsState()
    val passed by viewModel.passed.collectAsState()

    PageBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp),
        ) {
            // Header
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Discover Projects",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                        )
                        HexChip(
                            cut = 8.dp,
                            background = ButtonPrimary,
                            modifier = Modifier.noRippleClickable(onPostProject),
                        ) {
                            Text(
                                text = "+ Post",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            )
                        }
                    }
                    Spacer(Modifier.height(14.dp))

                    // Major filters
                    Text("Major", fontSize = 11.sp, color = TextSubtle, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChipRow(
                            label = "All",
                            selected = majorFilter == null,
                            onClick = { viewModel.setMajorFilter(null) },
                        )
                        listOf(MajorTag.CS to "CS", MajorTag.FILM to "Film",
                               MajorTag.BUSINESS to "Business", MajorTag.COMDES to "Com. Design")
                            .forEach { (tag, label) ->
                                FilterChipRow(
                                    label = label,
                                    selected = majorFilter == tag,
                                    onClick = { viewModel.setMajorFilter(if (majorFilter == tag) null else tag) },
                                )
                            }
                    }
                    Spacer(Modifier.height(10.dp))

                    // Status filters
                    Text("Status", fontSize = 11.sp, color = TextSubtle, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChipRow(
                            label = "Any Status",
                            selected = statusFilter == null,
                            onClick = { viewModel.setStatusFilter(null) },
                        )
                        listOf(ProjectStatus.OPEN to "Open", ProjectStatus.IN_PROGRESS to "In Progress")
                            .forEach { (status, label) ->
                                FilterChipRow(
                                    label = label,
                                    selected = statusFilter == status,
                                    onClick = { viewModel.setStatusFilter(if (statusFilter == status) null else status) },
                                )
                            }
                    }
                }
            }

            // Loading
            if (loading && projects.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ButtonAccent)
                    }
                }
            }

            // Error
            error?.let { err ->
                item {
                    Text(
                        text = "Error: $err",
                        color = androidx.compose.ui.graphics.Color(0xFFDC2626),
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

            // Projects
            items(projects) { project ->
                ProjectCard(
                    project = project,
                    isInterested = project.id in interested,
                    isPassed = project.id in passed,
                    onInterest = { viewModel.expressInterest(it) },
                    onPass = { viewModel.passProject(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                )
            }

            // Load more
            if (hasMore) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        HexChip(
                            cut = 8.dp,
                            background = ButtonPrimary.copy(alpha = 0.2f),
                            modifier = Modifier.noRippleClickable { viewModel.loadMore() },
                        ) {
                            Text(
                                text = if (loading) "Loading…" else "Load more projects",
                                fontSize = 13.sp,
                                color = ButtonAccent,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                            )
                        }
                    }
                }
            }

            // Empty state
            if (!loading && projects.isEmpty() && error == null) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("No projects found", color = TextMuted, fontSize = 15.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("Try changing your filters", color = TextSubtle, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterChipRow(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = ButtonPrimary.copy(alpha = 0.3f),
            selectedLabelColor = ButtonAccent,
            containerColor = InputBg,
            labelColor = TextSubtle,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            selectedBorderColor = ButtonAccent.copy(alpha = 0.4f),
            borderColor = Color(0xFF476897).copy(alpha = 0.4f),
        ),
    )
}
