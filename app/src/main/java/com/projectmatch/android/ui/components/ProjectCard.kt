package com.projectmatch.android.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.data.model.MajorTag
import com.projectmatch.android.data.model.Project
import com.projectmatch.android.data.model.ProjectStatus
import com.projectmatch.android.ui.theme.ButtonPrimary
import com.projectmatch.android.ui.theme.Danger
import com.projectmatch.android.ui.theme.DangerText
import com.projectmatch.android.ui.theme.MajorBizBg
import com.projectmatch.android.ui.theme.MajorBizText
import com.projectmatch.android.ui.theme.MajorComDesBg
import com.projectmatch.android.ui.theme.MajorComDesText
import com.projectmatch.android.ui.theme.MajorCsBg
import com.projectmatch.android.ui.theme.MajorCsText
import com.projectmatch.android.ui.theme.MajorFilmBg
import com.projectmatch.android.ui.theme.MajorFilmText
import com.projectmatch.android.ui.theme.PanelBorder
import com.projectmatch.android.ui.theme.Success
import com.projectmatch.android.ui.theme.SuccessText
import com.projectmatch.android.ui.theme.TextMuted
import com.projectmatch.android.ui.theme.TextPrimary
import com.projectmatch.android.ui.theme.TextSubtle

private data class StatusStyle(val label: String, val bg: Color, val text: Color)
private data class MajorStyle(val label: String, val bg: Color, val text: Color)

private fun statusStyle(s: ProjectStatus) = when (s) {
    ProjectStatus.OPEN -> StatusStyle("Open", Success.copy(alpha = 0.25f), SuccessText)
    ProjectStatus.IN_PROGRESS -> StatusStyle("In Progress", ButtonPrimary.copy(alpha = 0.25f), Color(0xFF8FB7E0))
    ProjectStatus.CLOSED -> StatusStyle("Closed", Color(0xFF646478).copy(alpha = 0.25f), TextSubtle)
}

private fun majorStyle(m: MajorTag) = when (m) {
    MajorTag.CS -> MajorStyle("CS", MajorCsBg, MajorCsText)
    MajorTag.FILM -> MajorStyle("Film", MajorFilmBg, MajorFilmText)
    MajorTag.BUSINESS -> MajorStyle("Business", MajorBizBg, MajorBizText)
    MajorTag.COMDES -> MajorStyle("Com. Design", MajorComDesBg, MajorComDesText)
}

@Composable
private fun Modifier.noRippleClickable(enabled: Boolean = true, onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClick = onClick,
    )

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProjectCard(
    project: Project,
    isInterested: Boolean,
    isPassed: Boolean,
    onInterest: (String) -> Unit,
    onPass: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val status = statusStyle(project.status)
    val borderColor = if (isInterested) Success.copy(alpha = 0.75f) else PanelBorder

    HexPanel(
        modifier = modifier,
        cut = 14.dp,
        borderColor = borderColor,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Status + major pills row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                HexChip(cut = 4.dp, background = status.bg) {
                    Text(
                        text = status.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = status.text,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    project.majors.forEach { m ->
                        val ms = majorStyle(m)
                        HexChip(cut = 4.dp, background = ms.bg) {
                            Text(
                                text = ms.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = ms.text,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            )
                        }
                    }
                }
            }

            // Title
            Text(
                text = project.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            // Description
            Text(
                text = project.description,
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 19.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            // Owner + spots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "by ${project.ownerName}", fontSize = 11.sp, color = TextSubtle)
                Text(
                    text = if (project.canJoin) "${project.spotsLeft} spot${if (project.spotsLeft != 1) "s" else ""} left" else "Full",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (project.canJoin) SuccessText else TextSubtle,
                )
            }

            // Tags
            if (project.tags.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    project.tags.take(4).forEach { tag ->
                        HexChip(cut = 3.dp, background = Color(0xFF6F95C5).copy(alpha = 0.1f)) {
                            Text(
                                text = tag,
                                fontSize = 11.sp,
                                color = TextSubtle,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            )
                        }
                    }
                }
            }

            // Pass / Interested buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                HexChip(
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable(enabled = !isPassed && !isInterested) { onPass(project.id) },
                    cut = 8.dp,
                    background = if (isPassed) Danger.copy(alpha = 0.25f) else Danger.copy(alpha = 0.1f),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = if (isPassed) "Passed" else "✕  Pass",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isPassed) DangerText else Danger,
                        )
                    }
                }

                HexChip(
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable(enabled = !isPassed && project.canJoin && !isInterested) { onInterest(project.id) },
                    cut = 8.dp,
                    background = if (isInterested) Success.copy(alpha = 0.35f) else Success.copy(alpha = 0.12f),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "✓  Interested",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isInterested) SuccessText else Success,
                        )
                    }
                }
            }
        }
    }
}
