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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.data.repository.ProjectRepository
import com.projectmatch.android.ui.components.HexChip
import com.projectmatch.android.ui.theme.ButtonAccent
import com.projectmatch.android.ui.theme.ButtonPrimary
import com.projectmatch.android.ui.theme.Danger
import com.projectmatch.android.ui.theme.DangerText
import com.projectmatch.android.ui.theme.InputBg
import com.projectmatch.android.ui.theme.InputBorder
import com.projectmatch.android.ui.theme.PageBackground
import com.projectmatch.android.ui.theme.SurfaceSecondary
import com.projectmatch.android.ui.theme.TextMuted
import com.projectmatch.android.ui.theme.TextPrimary
import com.projectmatch.android.ui.theme.TextSubtle
import kotlinx.coroutines.launch

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewProjectScreen(
    onBack: () -> Unit,
    onCreated: () -> Unit,
) {
    val repo = remember { ProjectRepository() }
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var catInput by remember { mutableStateOf("") }
    var roleInput by remember { mutableStateOf("") }
    val categories = remember { mutableStateListOf<String>() }
    val roles = remember { mutableStateListOf<String>() }
    var submitting by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun addTag(input: String, list: MutableList<String>, clear: () -> Unit) {
        val tag = input.trim()
        if (tag.isNotBlank() && tag !in list) {
            list.add(tag)
            clear()
        }
    }

    PageBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextMuted)
                }
                Text(
                    text = "New Project",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Title
                LabeledField("Project Title *") {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. Campus Navigator App", color = TextSubtle, fontSize = 13.sp) },
                        singleLine = true,
                        colors = fieldColors(),
                    )
                }

                // Description
                LabeledField("Description") {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("What is this project about?", color = TextSubtle, fontSize = 13.sp) },
                        minLines = 4,
                        colors = fieldColors(),
                    )
                }

                // Categories
                LabeledField("Categories") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = catInput,
                            onValueChange = { catInput = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("e.g. CS, Film…", color = TextSubtle, fontSize = 13.sp) },
                            singleLine = true,
                            colors = fieldColors(),
                        )
                        HexChip(
                            cut = 6.dp,
                            background = ButtonPrimary.copy(alpha = 0.3f),
                            modifier = Modifier.noRippleClickable {
                                addTag(catInput, categories) { catInput = "" }
                            },
                        ) {
                            Text("Add", fontSize = 12.sp, color = ButtonAccent,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp))
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        categories.forEach { cat ->
                            TagChip(label = cat, onRemove = { categories.remove(cat) })
                        }
                    }
                }

                // Roles
                LabeledField("Open Roles") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = roleInput,
                            onValueChange = { roleInput = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("e.g. Developer, Designer…", color = TextSubtle, fontSize = 13.sp) },
                            singleLine = true,
                            colors = fieldColors(),
                        )
                        HexChip(
                            cut = 6.dp,
                            background = ButtonPrimary.copy(alpha = 0.3f),
                            modifier = Modifier.noRippleClickable {
                                addTag(roleInput, roles) { roleInput = "" }
                            },
                        ) {
                            Text("Add", fontSize = 12.sp, color = ButtonAccent,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp))
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        roles.forEach { role ->
                            TagChip(label = role, onRemove = { roles.remove(role) })
                        }
                    }
                }

                error?.let {
                    Text(text = it, color = Danger, fontSize = 13.sp)
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    HexChip(
                        modifier = Modifier
                            .weight(1f)
                            .noRippleClickable(onBack),
                        cut = 10.dp,
                        background = SurfaceSecondary,
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 14.sp,
                            color = TextMuted,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                    }
                    HexChip(
                        modifier = Modifier
                            .weight(1f)
                            .noRippleClickable {
                                if (title.isBlank()) { error = "Title is required"; return@noRippleClickable }
                                submitting = true
                                error = null
                                scope.launch {
                                    try {
                                        repo.createProject(
                                            title = title.trim(),
                                            description = description.trim().ifBlank { null },
                                            type = (categories + roles).joinToString(", ").ifBlank { null },
                                        )
                                        onCreated()
                                    } catch (e: Exception) {
                                        error = e.message ?: "Failed to create project"
                                    } finally {
                                        submitting = false
                                    }
                                }
                            },
                        cut = 10.dp,
                        background = ButtonPrimary,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (submitting) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = TextPrimary, strokeWidth = 2.dp)
                            } else {
                                Text("Post Project", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun LabeledField(label: String, content: @Composable () -> Unit) {
    Column {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextSubtle)
        Spacer(Modifier.height(4.dp))
        content()
    }
}

@Composable
private fun TagChip(label: String, onRemove: () -> Unit) {
    HexChip(cut = 6.dp, background = ButtonPrimary.copy(alpha = 0.2f)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
        ) {
            Text(label, fontSize = 12.sp, color = ButtonAccent)
            IconButton(onClick = onRemove, modifier = Modifier.size(18.dp)) {
                Icon(Icons.Filled.Close, contentDescription = "Remove", tint = ButtonAccent,
                    modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = InputBorder,
    unfocusedBorderColor = InputBorder.copy(alpha = 0.5f),
    focusedContainerColor = InputBg,
    unfocusedContainerColor = InputBg,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    cursorColor = ButtonAccent,
)
