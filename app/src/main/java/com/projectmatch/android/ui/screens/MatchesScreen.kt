package com.projectmatch.android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.projectmatch.android.ui.theme.PageBackground
import com.projectmatch.android.ui.theme.Success
import com.projectmatch.android.ui.theme.SuccessText
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

private data class MatchItem(
    val id: String,
    val title: String,
    val description: String,
    val ownerName: String,
    val ownerInitial: String,
    val matchingRoles: List<String>,
)

private val MOCK_MATCHES = listOf(
    MatchItem(
        id = "1",
        title = "Campus Navigator App",
        description = "A mobile app that helps new students navigate City Tech's campus with indoor wayfinding and real-time updates.",
        ownerName = "Alex Rivera",
        ownerInitial = "A",
        matchingRoles = listOf("Mobile Developer", "UX Designer"),
    ),
    MatchItem(
        id = "7",
        title = "AR Art Installation: City as Canvas",
        description = "An augmented reality experience overlaying digital artwork on 5 City Tech campus locations, viewable via mobile.",
        ownerName = "Marcus Johnson",
        ownerInitial = "M",
        matchingRoles = listOf("AR Developer", "3D Artist"),
    ),
)

@Composable
fun MatchesScreen() {
    PageBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp),
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(
                        text = "My Matches",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "You have ${MOCK_MATCHES.size} new matches this week",
                        fontSize = 13.sp,
                        color = TextMuted,
                    )
                }
            }

            items(MOCK_MATCHES) { match ->
                MatchCard(match = match)
            }
        }
    }
}

@Composable
private fun MatchCard(match: MatchItem) {
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
            // Owner + Status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HexChip(cut = 8.dp, background = ButtonPrimary) {
                        Text(
                            text = match.ownerInitial,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        )
                    }
                    Text(match.ownerName, fontSize = 13.sp, color = TextMuted)
                }
                HexChip(cut = 6.dp, background = Success.copy(alpha = 0.2f)) {
                    Text(
                        text = "Matched",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = SuccessText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    )
                }
            }

            Text(match.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(match.description, fontSize = 13.sp, color = TextMuted, lineHeight = 18.sp, maxLines = 2)

            // Matching roles
            Text("Matching roles:", fontSize = 11.sp, color = TextSubtle)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                match.matchingRoles.forEach { role ->
                    HexChip(cut = 4.dp, background = ButtonPrimary.copy(alpha = 0.2f)) {
                        Text(role, fontSize = 11.sp, color = ButtonAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
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
                        .noRippleClickable { /* navigate to project */ },
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
                        .noRippleClickable { /* navigate to messages */ },
                    cut = 8.dp,
                    background = ButtonPrimary,
                ) {
                    Text(
                        text = "Send Message",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
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
