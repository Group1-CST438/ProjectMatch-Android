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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.ui.components.HexChip
import com.projectmatch.android.ui.components.HexPanel
import com.projectmatch.android.ui.theme.ButtonAccent
import com.projectmatch.android.ui.theme.ButtonPrimary
import com.projectmatch.android.ui.theme.MajorBizBg
import com.projectmatch.android.ui.theme.MajorBizText
import com.projectmatch.android.ui.theme.MajorComDesBg
import com.projectmatch.android.ui.theme.MajorComDesText
import com.projectmatch.android.ui.theme.MajorCsBg
import com.projectmatch.android.ui.theme.MajorCsText
import com.projectmatch.android.ui.theme.MajorFilmBg
import com.projectmatch.android.ui.theme.MajorFilmText
import com.projectmatch.android.ui.theme.PageBackground
import com.projectmatch.android.ui.theme.SurfaceDeep
import com.projectmatch.android.ui.theme.SurfacePrimary
import com.projectmatch.android.ui.theme.TextMuted
import com.projectmatch.android.ui.theme.TextPrimary
import com.projectmatch.android.ui.theme.TextSecondary
import com.projectmatch.android.ui.theme.TextSubtle

private data class MajorCard(val label: String, val emoji: String, val bg: androidx.compose.ui.graphics.Color, val text: androidx.compose.ui.graphics.Color)
private val majorCards = listOf(
    MajorCard("CS", "💻", MajorCsBg, MajorCsText),
    MajorCard("Film", "🎬", MajorFilmBg, MajorFilmText),
    MajorCard("Business", "📊", MajorBizBg, MajorBizText),
    MajorCard("Com. Design", "🎨", MajorComDesBg, MajorComDesText),
)

private data class Step(val num: String, val title: String, val desc: String)
private val steps = listOf(
    Step("1", "Post or Browse", "Create your project or explore what others are building across every major."),
    Step("2", "Match & Connect", "Express interest, get matched, and start talking with your new collaborators."),
    Step("3", "Build Together", "Work on something real — not a class assignment, but a project that matters."),
)

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onBrowse: () -> Unit,
    onPost: () -> Unit,
    onLogin: () -> Unit,
    onSignup: () -> Unit,
) {
    PageBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "ProjectMatch",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ButtonAccent,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    HexChip(cut = 8.dp, background = ButtonPrimary.copy(alpha = 0.2f)) {
                        Text(
                            text = "Log In",
                            fontSize = 13.sp,
                            color = ButtonAccent,
                            modifier = Modifier
                                .noRippleClickable(onLogin)
                                .padding(horizontal = 14.dp, vertical = 7.dp),
                        )
                    }
                    HexChip(cut = 8.dp, background = ButtonPrimary) {
                        Text(
                            text = "Sign Up",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            modifier = Modifier
                                .noRippleClickable(onSignup)
                                .padding(horizontal = 14.dp, vertical = 7.dp),
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Hero
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Real projects.\nReal experience.",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Find classmates across every major to collaborate on projects that build your portfolio and your future.",
                    fontSize = 15.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                )
                Spacer(Modifier.height(28.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    HexChip(cut = 10.dp, background = ButtonPrimary) {
                        Text(
                            text = "Post a Project",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            modifier = Modifier
                                .noRippleClickable(onPost)
                                .padding(horizontal = 18.dp, vertical = 10.dp),
                        )
                    }
                    HexChip(cut = 10.dp, background = ButtonPrimary.copy(alpha = 0.2f)) {
                        Text(
                            text = "Browse Projects",
                            fontSize = 14.sp,
                            color = ButtonAccent,
                            modifier = Modifier
                                .noRippleClickable(onBrowse)
                                .padding(horizontal = 18.dp, vertical = 10.dp),
                        )
                    }
                }
            }

            Spacer(Modifier.height(48.dp))

            // Major filter cards
            Text(
                text = "Explore by Major",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            Spacer(Modifier.height(12.dp))
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                majorCards.forEach { card ->
                    HexPanel(
                        modifier = Modifier
                            .weight(1f)
                            .noRippleClickable(onBrowse),
                        cut = 10.dp,
                        fill = Brush.linearGradient(listOf(SurfacePrimary, SurfaceDeep)),
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(card.emoji, fontSize = 24.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(card.label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = card.text)
                        }
                    }
                }
            }

            Spacer(Modifier.height(48.dp))

            // How it works
            Text(
                text = "How It Works",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Three steps to your next real project",
                fontSize = 14.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(20.dp))

            steps.forEach { step ->
                HexPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    cut = 12.dp,
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        HexChip(cut = 8.dp, background = ButtonPrimary) {
                            Text(
                                text = step.num,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            )
                        }
                        Column {
                            Text(step.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Spacer(Modifier.height(4.dp))
                            Text(step.desc, fontSize = 13.sp, color = TextMuted, lineHeight = 18.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(48.dp))

            // Bottom CTA
            HexPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                cut = 16.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Stop waiting for class assignments",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Join students already building their portfolios with real projects.",
                        fontSize = 13.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(16.dp))
                    HexChip(cut = 10.dp, background = ButtonPrimary) {
                        Text(
                            text = "Get Started",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            modifier = Modifier
                                .noRippleClickable(onSignup)
                                .padding(horizontal = 24.dp, vertical = 10.dp),
                        )
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            // Footer
            Text(
                text = "© 2026 ProjectMatch. All rights reserved.",
                fontSize = 11.sp,
                color = TextSubtle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            )
        }
    }
}
