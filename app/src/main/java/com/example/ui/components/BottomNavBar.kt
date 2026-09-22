package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class NavDestination(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Default.Home),
    TASKS("tasks", "Tasks", Icons.Default.Assignment),
    ASK_AI("ask_ai", "Ask AI", Icons.Default.Mic),
    GAMES("games", "Games", Icons.Default.SportsEsports),
    PROGRESS("progress", "Progress", Icons.Default.Insights)
}

@Composable
fun BottomNavBar(
    currentDestination: NavDestination,
    onNavigate: (NavDestination) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            NavItem(
                destination = NavDestination.HOME,
                isSelected = currentDestination == NavDestination.HOME,
                onSelect = { onNavigate(NavDestination.HOME) }
            )

            // Tasks
            NavItem(
                destination = NavDestination.TASKS,
                isSelected = currentDestination == NavDestination.TASKS,
                onSelect = { onNavigate(NavDestination.TASKS) }
            )

            // Central Elevated Mic FAB for "Ask AI"
            Box(
                modifier = Modifier
                    .offset(y = (-14).dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onNavigate(NavDestination.ASK_AI) },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .shadow(8.dp, CircleShape, spotColor = PrimaryContainer)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Ask AI",
                            tint = OnPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Ask AI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnPrimaryContainer
                    )
                }
            }

            // Games
            NavItem(
                destination = NavDestination.GAMES,
                isSelected = currentDestination == NavDestination.GAMES,
                onSelect = { onNavigate(NavDestination.GAMES) }
            )

            // Progress
            NavItem(
                destination = NavDestination.PROGRESS,
                isSelected = currentDestination == NavDestination.PROGRESS,
                onSelect = { onNavigate(NavDestination.PROGRESS) }
            )
        }
    }
}

@Composable
private fun NavItem(
    destination: NavDestination,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.05f else 1.0f, label = "nav_scale")

    Column(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onSelect() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = destination.icon,
            contentDescription = destination.label,
            tint = if (isSelected) Primary else OnSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = destination.label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
            color = if (isSelected) Primary else OnSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}
