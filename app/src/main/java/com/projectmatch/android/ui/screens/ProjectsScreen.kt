package com.projectmatch.android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.data.model.BackendProject
import com.projectmatch.android.ui.components.HexChip
import com.projectmatch.android.ui.components.HexPanel
import com.projectmatch.android.ui.theme.ButtonAccent
import com.projectmatch.android.ui.theme.ButtonPrimary
import com.projectmatch.android.ui.theme.Danger
import com.projectmatch.android.ui.theme.InputBg
import com.projectmatch.android.ui.theme.InputBorder
import com.projectmatch.android.ui.theme.PageBackground
import com.projectmatch.android.ui.theme.TextMuted
import com.projectmatch.android.ui.theme.TextPrimary
import com.projectmatch.android.ui.theme.TextSubtle
import com.projectmatch.android.viewmodel.ProjectsViewModel

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )

@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel,
    onNewProject: () -> Unit,
) {
    val projects by viewModel.filtered.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val search by viewModel.search.collectAsState()

    var deleteTarget by remember { mutableStateOf<BackendProject?>(null) }
    var editTarget by remember { mutableStateOf<BackendProject?>(null) }
    var editTitle by remember { mutableStateOf("") }
    var editDesc by remember { mutableStateOf("") }
    var editType by remember { mutableStateOf("") }

    PageBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search bar + create button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = search,
                    onValueChange = { viewModel.setSearch(it) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search projects…", color = TextSubtle, fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TextSubtle) },
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
                    cut = 8.dp,
                    background = ButtonPrimary,
                    modifier = Modifier.noRippleClickable(onNewProject),
                ) {
                    Text(
                        text = "+ Create",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                    )
                }
            }

            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ButtonAccent)
                }
            } else if (projects.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No projects yet", color = TextMuted, fontSize = 15.sp)
                        Spacer(Modifier.height(4.dp))
                        Text("Tap + Create to get started", color = TextSubtle, fontSize = 12.sp)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp),
                ) {
                    items(projects) { project ->
                        ProjectListItem(
                            project = project,
                            onEdit = {
                                editTarget = project
                                editTitle = project.title
                                editDesc = project.generalDescription ?: ""
                                editType = project.type ?: ""
                            },
                            onDelete = { deleteTarget = project },
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    deleteTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("Delete Project", color = TextPrimary) },
            text = { Text("Are you sure you want to delete \"${target.title}\"?", color = TextMuted) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteProject(target.id)
                    deleteTarget = null
                }) {
                    Text("Delete", color = Danger)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text("Cancel", color = TextSubtle)
                }
            },
            containerColor = InputBg,
        )
    }

    // Edit dialog
    editTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { editTarget = null },
            title = { Text("Edit Project", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Title", color = TextSubtle) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InputBorder,
                            unfocusedBorderColor = InputBorder.copy(alpha = 0.5f),
                            focusedContainerColor = InputBg,
                            unfocusedContainerColor = InputBg,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                        ),
                    )
                    OutlinedTextField(
                        value = editDesc,
                        onValueChange = { editDesc = it },
                        label = { Text("Description", color = TextSubtle) },
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InputBorder,
                            unfocusedBorderColor = InputBorder.copy(alpha = 0.5f),
                            focusedContainerColor = InputBg,
                            unfocusedContainerColor = InputBg,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                        ),
                    )
                    OutlinedTextField(
                        value = editType,
                        onValueChange = { editType = it },
                        label = { Text("Categories / Type", color = TextSubtle) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InputBorder,
                            unfocusedBorderColor = InputBorder.copy(alpha = 0.5f),
                            focusedContainerColor = InputBg,
                            unfocusedContainerColor = InputBg,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                        ),
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateProject(target.id, editTitle, editDesc, editType)
                    editTarget = null
                }) {
                    Text("Save", color = ButtonAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { editTarget = null }) {
                    Text("Cancel", color = TextSubtle)
                }
            },
            containerColor = InputBg,
        )
    }
}

@Composable
private fun ProjectListItem(
    project: BackendProject,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    HexPanel(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        cut = 12.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                )
                project.generalDescription?.takeIf { it.isNotBlank() }?.let { desc ->
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = desc,
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 2,
                    )
                }
                project.type?.takeIf { it.isNotBlank() }?.let { type ->
                    Spacer(Modifier.height(4.dp))
                    Text(text = type, fontSize = 11.sp, color = TextSubtle)
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = ButtonAccent)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Danger)
                }
            }
        }
    }
}
