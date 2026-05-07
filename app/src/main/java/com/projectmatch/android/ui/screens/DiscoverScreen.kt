package com.projectmatch.android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.data.model.Project
import com.projectmatch.android.ui.components.HexChip
import com.projectmatch.android.ui.components.HexPanel
import com.projectmatch.android.ui.theme.ButtonAccent
import com.projectmatch.android.ui.theme.ButtonPrimary
import com.projectmatch.android.ui.theme.Danger
import com.projectmatch.android.ui.theme.PageBackground
import com.projectmatch.android.ui.theme.SuccessText
import com.projectmatch.android.ui.theme.TextMuted
import com.projectmatch.android.ui.theme.TextPrimary
import com.projectmatch.android.ui.theme.TextSubtle
import com.projectmatch.android.viewmodel.DiscoverViewModel
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    onPostProject: () -> Unit,
) {
    val projects by viewModel.projects.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val interested by viewModel.interested.collectAsState()
    val passed by viewModel.passed.collectAsState()

    val visibleProjects = projects.filter { it.id !in interested && it.id !in passed }
    val currentProject = visibleProjects.getOrNull(0)
    val nextProject = visibleProjects.getOrNull(1)

    PageBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "Discover",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                    )
                    Text(
                        text = "Swipe right if interested, left to pass",
                        fontSize = 12.sp,
                        color = TextSubtle,
                    )
                }

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

            Spacer(Modifier.height(18.dp))

            when {
                loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ButtonAccent)
                    }
                }

                error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Could not load projects: $error",
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                currentProject == null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No more projects",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Check back later for new matches.",
                                color = TextSubtle,
                                fontSize = 13.sp,
                            )
                        }
                    }
                }

                else -> {
                    SwipeProjectStack(
                        currentProject = currentProject,
                        nextProject = nextProject,
                        onPass = { viewModel.passProject(currentProject.id) },
                        onInterested = { viewModel.expressInterest(currentProject.id) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SwipeProjectStack(
    currentProject: Project,
    nextProject: Project?,
    onPass: () -> Unit,
    onInterested: () -> Unit,
) {
    var offsetX by remember(currentProject.id) { mutableStateOf(0f) }

    val swipeDistance = 150f
    val progress = (abs(offsetX) / swipeDistance).coerceIn(0f, 1f)
    val rotation = (offsetX / 25f).coerceIn(-12f, 12f)
    val currentAlpha = (1f - progress * 0.45f).coerceIn(0.55f, 1f)

    val nextScale = 0.92f + (progress * 0.08f)
    val nextAlpha = 0.45f + (progress * 0.55f)

    val showInterested = offsetX > 40f
    val showPass = offsetX < -40f

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (nextProject != null) {
            ProjectCard(
                project = nextProject,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .graphicsLayer {
                        scaleX = nextScale
                        scaleY = nextScale
                        alpha = nextAlpha
                    },
                showInterested = false,
                showPass = false,
            )
        }

        ProjectCard(
            project = currentProject,
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .graphicsLayer {
                    rotationZ = rotation
                    alpha = currentAlpha
                }
                .pointerInput(currentProject.id) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            when {
                                offsetX > swipeDistance -> onInterested()
                                offsetX < -swipeDistance -> onPass()
                            }
                            offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX += dragAmount
                        },
                    )
                },
            showInterested = showInterested,
            showPass = showPass,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
    showInterested: Boolean,
    showPass: Boolean,
) {
    HexPanel(
        modifier = modifier,
        cut = 18.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (showInterested) {
                Text(
                    text = "INTERESTED",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessText,
                    modifier = Modifier.rotate(-8f),
                )
            }

            if (showPass) {
                Text(
                    text = "PASS",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Danger,
                    modifier = Modifier.rotate(8f),
                )
            }

            Text(
                text = project.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                lineHeight = 31.sp,
            )

            Text(
                text = project.description.ifBlank { "No description yet." },
                fontSize = 15.sp,
                color = TextMuted,
                lineHeight = 22.sp,
            )

            Text(
                text = "Posted by ${project.ownerName}",
                fontSize = 12.sp,
                color = TextSubtle,
            )

            if (project.tags.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    project.tags.forEach { tag ->
                        HexChip(
                            cut = 4.dp,
                            background = Color(0xFF6F95C5).copy(alpha = 0.12f),
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp,
                                color = TextSubtle,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}