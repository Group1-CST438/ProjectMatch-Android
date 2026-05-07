package com.projectmatch.android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.ui.theme.*

data class NavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

val bottomNavItems = listOf(
    NavItem("discover", "Discover", Icons.Filled.Search),
    NavItem("projects", "Projects", Icons.Filled.FolderOpen),
    NavItem("matches", "Matches", Icons.Filled.Star),
    NavItem("saved", "Saved", Icons.Filled.Bookmark),
    NavItem("profile", "Profile", Icons.Filled.Person),
)

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
) {
    NavigationBar(containerColor = PageBgEnd, tonalElevation = 0.dp) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, item.label) },
                label = { Text(item.label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ButtonAccent,
                    selectedTextColor = ButtonAccent,
                    unselectedIconColor = TextSubtle,
                    unselectedTextColor = TextSubtle,
                    indicatorColor = Color(0xFF1C2E49),
                ),
            )
        }
    }
}