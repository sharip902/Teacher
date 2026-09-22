package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*

@Composable
fun TopHeaderBar(
    currentScreenTitle: String = "Home",
    streakDays: Int = 4,
    starsCount: Int = 125,
    selectedLang: String = "ENG • ಕನ್ನಡ",
    onToggleLang: () -> Unit = {},
    onParentClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBackClick: (() -> Unit)? = null
) {
    Surface(
        color = Surface.copy(alpha = 0.95f),
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // First row: App Title + Mascot Logo + Language Pill + Profile
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerLow)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = OnSurface
                            )
                        }
                    } else {
                        // Mascot small round logo
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida/AEtjO1VjiYfOJMMAC3_yCEwgUYdkWjNLFWCCZsbty_nOyG1W2nbvZhBfRVb2qfxuU1j5h74apsilws_UNwbqiTUkrCCxNw14jvLpBUtdIEzEtOVXjRiqWIDn38x-5c4CyQ29-6vABgFFj38j4kWQpudRzIcqrR27o5VQGBgsIBzzkGs_sSB2QgJbU6wxXtWLLXdW_9gp7MaGgCyKo1nzDT35sCoOtDFNQDs3oo5QDpnThq1LbQu24ot6EfYLYMsj",
                                contentDescription = "Homework Teacher Mascot Logo",
                                modifier = Modifier.size(32.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (onBackClick != null) currentScreenTitle else "Homework Teacher AI",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (onBackClick != null) OnSurface else Primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (onBackClick == null) {
                            Text(
                                text = "Your AI Learning Friend",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Language switcher button
                    Surface(
                        shape = CircleShape,
                        color = SurfaceContainerLow,
                        modifier = Modifier.clickable { onToggleLang() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Translate",
                                tint = Secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = selectedLang,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Secondary
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Primary)
                            .clickable { onProfileClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Profile",
                            tint = OnPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Second row: Streak + Stars + Sub-label + Parent Control button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Streak Pill
                    Surface(
                        shape = CircleShape,
                        color = PrimaryFixed,
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = Primary,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "$streakDays Days",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnPrimaryFixed
                            )
                        }
                    }

                    // Stars Pill
                    Surface(
                        shape = CircleShape,
                        color = SurfaceContainerLowest,
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Stars",
                                tint = PrimaryContainer,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "$starsCount Stars",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface
                            )
                        }
                    }

                    Text(
                        text = currentScreenTitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                // Parent Mode Button
                Surface(
                    shape = CircleShape,
                    color = SurfaceContainer,
                    modifier = Modifier.clickable { onParentClick() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FamilyRestroom,
                            contentDescription = "Parent",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Parent",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "PIN Protected",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}
