package com.projectmatch.android.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectmatch.android.ui.theme.ButtonAccent
import com.projectmatch.android.ui.theme.PageBgEnd
import com.projectmatch.android.ui.theme.TextSubtle

data class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
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
    NavigationBar(
        containerColor = PageBgEnd,
        tonalElevation = 0.dp,
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp),
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                    )
                },
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