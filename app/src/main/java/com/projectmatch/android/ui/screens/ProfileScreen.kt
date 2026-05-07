package com.projectmatch.android.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.ui.components.HexChip
import com.projectmatch.android.ui.components.HexPanel
import com.projectmatch.android.ui.theme.ButtonAccent
import com.projectmatch.android.ui.theme.ButtonPrimary
import com.projectmatch.android.ui.theme.Danger
import com.projectmatch.android.ui.theme.InputBg
import com.projectmatch.android.ui.theme.InputBorder
import com.projectmatch.android.ui.theme.PageBackground
import com.projectmatch.android.ui.theme.Success
import com.projectmatch.android.ui.theme.SuccessText
import com.projectmatch.android.ui.theme.TextMuted
import com.projectmatch.android.ui.theme.TextPrimary
import com.projectmatch.android.ui.theme.TextSubtle
import com.projectmatch.android.viewmodel.AuthViewModel
import com.projectmatch.android.viewmodel.PRESET_ROLES
import com.projectmatch.android.viewmodel.PRESET_SKILLS
import com.projectmatch.android.viewmodel.ProfileViewModel

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit,
) {
    val user by authViewModel.user.collectAsState()
    val bio by viewModel.bio.collectAsState()
    val roles by viewModel.roles.collectAsState()
    val skills by viewModel.skills.collectAsState()
    val saved by viewModel.saved.collectAsState()

    var skillInput by remember { mutableStateOf("") }
    val email = user?.email ?: ""
    val initial = email.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    LaunchedEffect(user) {
        user?.id?.let { viewModel.loadProfile(it) }
    }

    PageBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Profile header
            HexPanel(modifier = Modifier.fillMaxWidth(), cut = 14.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        HexChip(cut = 16.dp, background = ButtonPrimary) {
                            Text(
                                text = initial,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(12.dp),
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(email.substringBefore("@"), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(email, fontSize = 12.sp, color = TextSubtle)
                        if (roles.isNotEmpty()) {
                            Spacer(Modifier.height(4.dp))
                            Text(roles.joinToString(" · "), fontSize = 11.sp, color = ButtonAccent)
                        }
                    }
                }
            }

            // Bio
            SectionPanel(title = "Bio") {
                OutlinedTextField(
                    value = bio,
                    onValueChange = { viewModel.setBio(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Tell people about yourself…", color = TextSubtle, fontSize = 13.sp) },
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InputBorder,
                        unfocusedBorderColor = InputBorder.copy(alpha = 0.5f),
                        focusedContainerColor = InputBg,
                        unfocusedContainerColor = InputBg,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = ButtonAccent,
                    ),
                )
                Spacer(Modifier.height(8.dp))
                HexChip(
                    cut = 8.dp,
                    background = ButtonPrimary.copy(alpha = 0.3f),
                    modifier = Modifier.noRippleClickable { viewModel.saveProfile() },
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                        if (saved) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = SuccessText, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Saved!", fontSize = 12.sp, color = SuccessText)
                        } else {
                            Text("Save Bio", fontSize = 12.sp, color = ButtonAccent)
                        }
                    }
                }
            }

            // Roles
            SectionPanel(title = "Roles") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PRESET_ROLES.forEach { role ->
                        val selected = role in roles
                        HexChip(
                            cut = 8.dp,
                            background = if (selected) Success.copy(alpha = 0.3f) else InputBg,
                            modifier = Modifier.noRippleClickable { viewModel.toggleRole(role) },
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            ) {
                                if (selected) {
                                    Icon(Icons.Filled.Check, contentDescription = null,
                                        tint = SuccessText, modifier = Modifier.size(12.dp))
                                    Spacer(Modifier.width(4.dp))
                                }
                                Text(
                                    role,
                                    fontSize = 12.sp,
                                    color = if (selected) SuccessText else TextMuted,
                                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                                )
                            }
                        }
                    }
                }
            }

            // Skills
            SectionPanel(title = "Skills") {
                // Preset skills
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PRESET_SKILLS.forEach { skill ->
                        val selected = skill in skills
                        HexChip(
                            cut = 6.dp,
                            background = if (selected) ButtonPrimary.copy(alpha = 0.3f) else InputBg,
                            modifier = Modifier.noRippleClickable {
                                if (selected) viewModel.removeSkill(skill) else viewModel.addSkill(skill)
                            },
                        ) {
                            Text(
                                skill,
                                fontSize = 12.sp,
                                color = if (selected) ButtonAccent else TextSubtle,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Custom skill input
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = skillInput,
                        onValueChange = { skillInput = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Add custom skill…", color = TextSubtle, fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InputBorder,
                            unfocusedBorderColor = InputBorder.copy(alpha = 0.5f),
                            focusedContainerColor = InputBg,
                            unfocusedContainerColor = InputBg,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = ButtonAccent,
                        ),
                    )
                    HexChip(
                        cut = 6.dp,
                        background = ButtonPrimary.copy(alpha = 0.3f),
                        modifier = Modifier.noRippleClickable {
                            viewModel.addSkill(skillInput)
                            skillInput = ""
                        },
                    ) {
                        Text("Add", fontSize = 12.sp, color = ButtonAccent,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp))
                    }
                }

                // User's custom skills (non-preset)
                val customSkills = skills - PRESET_SKILLS.toSet()
                if (customSkills.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        customSkills.forEach { skill ->
                            HexChip(cut = 6.dp, background = ButtonPrimary.copy(alpha = 0.2f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 8.dp, end = 2.dp, top = 4.dp, bottom = 4.dp),
                                ) {
                                    Text(skill, fontSize = 12.sp, color = ButtonAccent)
                                    IconButton(onClick = { viewModel.removeSkill(skill) }, modifier = Modifier.size(18.dp)) {
                                        Icon(Icons.Filled.Close, contentDescription = "Remove",
                                            tint = ButtonAccent, modifier = Modifier.size(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Sign out
            HexChip(
                cut = 10.dp,
                background = Danger.copy(alpha = 0.1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable(onSignOut),
            ) {
                Text(
                    text = "Sign Out",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Danger,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionPanel(title: String, content: @Composable () -> Unit) {
    HexPanel(modifier = Modifier.fillMaxWidth(), cut = 12.dp) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}
