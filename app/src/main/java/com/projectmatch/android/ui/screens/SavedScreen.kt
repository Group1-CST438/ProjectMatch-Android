package com.projectmatch.android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.ui.components.HexChip
import com.projectmatch.android.ui.components.HexPanel
import com.projectmatch.android.ui.theme.ButtonAccent
import com.projectmatch.android.ui.theme.ButtonPrimary
import com.projectmatch.android.ui.theme.Danger
import com.projectmatch.android.ui.theme.MajorCsBg
import com.projectmatch.android.ui.theme.MajorCsText
import com.projectmatch.android.ui.theme.MajorFilmBg
import com.projectmatch.android.ui.theme.MajorFilmText
import com.projectmatch.android.ui.theme.PageBackground
import com.projectmatch.android.ui.theme.TextMuted
import com.projectmatch.android.ui.theme.TextPrimary
import com.projectmatch.android.ui.theme.TextSubtle

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )

private data class SavedItem(
    val id: String,
    val title: String,
    val description: String,
    val ownerName: String,
    val categories: List<Pair<String, androidx.compose.ui.graphics.Color>>,
    val roles: List<String>,
    val savedDate: String,
)

private val INITIAL_SAVED = listOf(
    SavedItem(
        id = "3",
        title = "TutorLink — Peer Tutoring Platform",
        description = "A web platform connecting City Tech students who need tutoring with peers who can help.",
        ownerName = "Jordan Kim",
        categories = listOf("CS" to MajorCsText),
        roles = listOf("Backend Dev", "Frontend Dev"),
        savedDate = "Apr 28",
    ),
    SavedItem(
        id = "8",
        title = "Career Paths in STEM Podcast",
        description = "A podcast series interviewing City Tech alumni about their careers in STEM.",
        ownerName = "Aisha Morgan",
        categories = listOf("Film" to MajorFilmText),
        roles = listOf("Audio Engineer", "Editor"),
        savedDate = "Apr 25",
    ),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SavedScreen() {
    val saved = remember { mutableStateListOf(*INITIAL_SAVED.toTypedArray()) }

    PageBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp),
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(
                        text = "Saved Projects",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${saved.size} saved project${if (saved.size != 1) "s" else ""}",
                        fontSize = 13.sp,
                        color = TextMuted,
                    )
                }
            }

            items(saved, key = { it.id }) { item ->
                HexPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    cut = 14.dp,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        // Categories + saved date
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                item.categories.forEach { (label, color) ->
                                    HexChip(cut = 4.dp, background = MajorCsBg) {
                                        Text(
                                            text = label,
                                            fontSize = 11.sp,
                                            color = color,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        )
                                    }
                                }
                            }
                            HexChip(cut = 4.dp, background = ButtonPrimary.copy(alpha = 0.15f)) {
                                Text(
                                    text = "Saved ${item.savedDate}",
                                    fontSize = 10.sp,
                                    color = TextSubtle,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                )
                            }
                        }

                        Text(item.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(item.description, fontSize = 13.sp, color = TextMuted, lineHeight = 18.sp, maxLines = 2)

                        // Owner
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            HexChip(cut = 6.dp, background = ButtonPrimary) {
                                Text(
                                    item.ownerName.firstOrNull()?.toString() ?: "?",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                )
                            }
                            Text(item.ownerName, fontSize = 12.sp, color = TextSubtle)
                        }

                        // Roles
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            item.roles.forEach { role ->
                                HexChip(cut = 4.dp, background = ButtonPrimary.copy(alpha = 0.15f)) {
                                    Text(role, fontSize = 11.sp, color = ButtonAccent,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp))
                                }
                            }
                        }

                        // Action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            HexChip(
                                modifier = Modifier
                                    .weight(1f)
                                    .noRippleClickable { /* view project */ },
                                cut = 8.dp,
                                background = ButtonPrimary.copy(alpha = 0.2f),
                            ) {
                                Text(
                                    text = "View Project",
                                    fontSize = 13.sp,
                                    color = ButtonAccent,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                )
                            }
                            HexChip(
                                modifier = Modifier
                                    .weight(1f)
                                    .noRippleClickable { saved.remove(item) },
                                cut = 8.dp,
                                background = Danger.copy(alpha = 0.1f),
                            ) {
                                Text(
                                    text = "Remove",
                                    fontSize = 13.sp,
                                    color = Danger,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }

            if (saved.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("No saved projects", color = TextMuted, fontSize = 15.sp)
                        Spacer(Modifier.height(4.dp))
                        Text("Browse Discover to find projects to save", color = TextSubtle, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
