package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*
import com.example.util.VoiceHelper

@Composable
fun ScanAndLearnScreen(
    voiceHelper: VoiceHelper,
    onBackClick: () -> Unit,
    onOpenVoiceAssistant: () -> Unit,
    onOpenFaceRecognition: () -> Unit = {}
) {
    var selectedMode by remember { mutableStateOf(0) } // 0: Scan Work, 1: Tasks, 2: Textbook, 3: Face ID
    var isFlashOn by remember { mutableStateOf(true) }
    var isStep3Solved by remember { mutableStateOf(false) }
    var isTtsPlaying by remember { mutableStateOf(false) }
    var isTvCasting by remember { mutableStateOf(false) }

    // Laser scan animation
    val infiniteTransition = rememberInfiniteTransition(label = "laser")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "laser_pos"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mode Selector Pills
        item {
            Surface(
                shape = CircleShape,
                color = SurfaceContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val modes = listOf(
                        Triple(0, "Scan Work", Icons.Default.PhotoCamera),
                        Triple(1, "Tasks (3)", Icons.Default.Assignment),
                        Triple(2, "Textbook", Icons.Default.MenuBook),
                        Triple(3, "Face ID 👦", Icons.Default.Face)
                    )

                    modes.forEach { (index, title, icon) ->
                        val isSelected = selectedMode == index
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) SurfaceContainerLowest else Color.Transparent,
                            shadowElevation = if (isSelected) 2.dp else 0.dp,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    if (index == 3) {
                                        onOpenFaceRecognition()
                                    } else {
                                        selectedMode = index
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Primary else OnSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                    color = if (isSelected) Primary else OnSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Camera Viewfinder Section
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF202124)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Notebook image
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBHE7ZAKnfH_IMvDOgZmhbt3vXVF-nqIciWxKGBpk10Z8zTj4MiFxLpHQcCnkwlgQ8M9eEPA9QV9bUTkaSLWrCPui3nddP1KPrJPRsgi-MVwNKRsvPM9qKRLE9xVu180jHiK4U_heONzj_0nMKpTnotfofZtvflfK3OoiSjyYdvt9uIS8b4jfRbA6y-5HUb4SHJjDH94fqd_jn41mkIuiVN3i58wSSJnqSszJMGUmAB1td7KyIsvmTLfA",
                        contentDescription = "Homework notebook view",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 54.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Laser scan line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .offset(y = (laserY * 220).dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, SecondaryContainer, Color.Transparent)
                                )
                            )
                    )

                    // Top Badges inside Viewfinder
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xDD202124)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(TertiaryContainer)
                                )
                                Text(
                                    text = "Math • Grade 3",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "99% match",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SecondaryContainer
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xBB202124)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = PrimaryContainer,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Smart Auto-Detect",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // OCR Bounding Box 1: 24 + 18 = ?
                    Box(
                        modifier = Modifier
                            .padding(start = 24.dp, top = 65.dp, end = 24.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SecondaryContainer.copy(alpha = 0.2f))
                            .border(2.dp, SecondaryContainer, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Secondary
                                ) {
                                    Text(
                                        text = "Target: Question 1",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SecondaryFixed,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "24 + 18 = ?",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    // OCR Bounding Box 2: Next Question
                    Box(
                        modifier = Modifier
                            .padding(start = 24.dp, top = 160.dp, end = 48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x66000000))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notes,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Q2: Why do plants need sunlight?",
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                        }
                    }

                    // Viewfinder Bottom Control Shelf
                    Surface(
                        color = Color(0xFF1E1E1E),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .height(58.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = "Upload",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Button(
                                onClick = {
                                    voiceHelper.speak("Snap taken! Question detected: 24 plus 18. Let's solve it step by step!")
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryContainer,
                                    contentColor = OnPrimaryContainer
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CameraEnhance,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Snap & Explain",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }

                            IconButton(onClick = { isFlashOn = !isFlashOn }) {
                                Icon(
                                    imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                    contentDescription = "Flash",
                                    tint = if (isFlashOn) PrimaryContainer else Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.FlipCameraIos,
                                    contentDescription = "Flip",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // AI TEACHER INTERACTIVE STEP-BY-STEP SCAFFOLDING
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Teacher Header
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(PrimaryContainer, SecondaryFixed))
                                )
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(SurfaceContainerLowest)
                            ) {
                                AsyncImage(
                                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCUsQ9hz-b8ThwKD9kfXD3z244Di7lFz_eXBj7NHk71B5fFx9eZvBVsdIuxRfiW9lrrAxzPDZ723JPhbQxHNJGtgHhZFW9aFN6OPVA5Ej7hNEbYjLMNkC0lNhSoJDQ_7SzSfGVFbzQHY8hCAcbmrPow33JGa48Le9Ji_gMWEgHs3hW7V1yY5ybHtTjLzW0btXAFX-YcZYkUWFNL54qIMbFDwauAk3CinqEHzZKiWzYUsrCzYkvDQLmv4A",
                                    contentDescription = "Tutor Pip",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.BottomEnd)
                                    .clip(CircleShape)
                                    .background(TertiaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = OnTertiaryContainer,
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                        }

                        // Speech Bubble
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "TUTOR PIP AI",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Primary
                                    )
                                    Text(
                                        text = "🧠 Guiding Mode",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Tertiary
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "I read your sum: 24 + 18! Let's think it through like a puzzle. What is 24 + 10 first?",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // Growth Mindset banner
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = TertiaryFixed.copy(alpha = 0.4f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Never worry about wrong guesses — every try stretches your brain muscles!",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnTertiaryFixedVariant,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Step-by-Step Ladder
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Step-by-Step Ladder",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceVariant
                            )
                            Text(
                                text = if (isStep3Solved) "Step 3 of 3 (Complete!)" else "Step 2 of 3",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isStep3Solved) Tertiary else Primary
                            )
                        }

                        // Step 1: Friendly Numbers
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceContainerLow
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Tertiary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Column {
                                        Text(text = "Step 1: Friendly Numbers", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                        Text(text = "Split 18 into 10 + 8", fontSize = 11.sp, color = OnSurfaceVariant)
                                    }
                                }
                                Text(text = "Done!", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Tertiary)
                            }
                        }

                        // Step 2: Add the Ten
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceContainerLow
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Tertiary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Column {
                                        Text(text = "Step 2: Add the Ten", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                        Text(text = "24 + 10 = 34", fontSize = 11.sp, color = OnSurfaceVariant)
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = PrimaryContainer, modifier = Modifier.size(14.dp))
                                    Text(text = "+5 pts", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary)
                                }
                            }
                        }

                        // Step 3: Child's Turn
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isStep3Solved) TertiaryFixed.copy(alpha = 0.35f) else SecondaryFixed.copy(alpha = 0.35f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(if (isStep3Solved) Tertiary else Secondary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (isStep3Solved) "✓" else "3",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Text(
                                            text = if (isStep3Solved) "Step 3 Solved!" else "Step 3: Child's Turn!",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isStep3Solved) Tertiary else OnSecondaryFixed
                                        )
                                    }
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isStep3Solved) Tertiary else Secondary
                                    ) {
                                        Text(
                                            text = if (isStep3Solved) "Mastered ⭐" else "Active",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = if (isStep3Solved) "Now add remaining 8 to 34:\n34 + 8 = 42! 🎉 Correct!" else "Now add remaining 8 to 34:\n34 + 8 = [ ? ]",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isStep3Solved) Tertiary else OnSecondaryFixedVariant
                                )

                                Button(
                                    onClick = {
                                        isStep3Solved = true
                                        voiceHelper.speak("Fantastic Aarav! 34 plus 8 equals 42! You solved 24 plus 18 = 42! You earned 10 stars!")
                                    },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isStep3Solved) Tertiary else Secondary,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isStep3Solved) Icons.Default.DoneAll else Icons.Default.Mic,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isStep3Solved) "Awesome! 34 + 8 = 42 ⭐ (+10 Pts)" else "Tap & Say: \"42\"",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Quick Action Exploration Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionPill(
                            icon = Icons.Default.Lightbulb,
                            label = "Give a Hint",
                            tint = PrimaryContainer,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                voiceHelper.speak("Here is a hint: 34 plus 6 makes 40! Then add the last 2 to reach 42!")
                            }
                        )
                        ActionPill(
                            icon = Icons.Default.VolumeUp,
                            label = "Read Aloud",
                            tint = Secondary,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                voiceHelper.speak("Question 1: Twenty four plus eighteen equals question mark.")
                            }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionPill(
                            icon = Icons.Default.Translate,
                            label = "ಕನ್ನಡ / हिंदी",
                            tint = Tertiary,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                voiceHelper.speak("Kannada: Ippattu naalku koodisu hadinentu samanagide nalavatteradu!")
                            }
                        )
                        ActionPill(
                            icon = Icons.Default.Draw,
                            label = "Draw Shapes",
                            tint = Primary,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                voiceHelper.speak("Let's draw 24 apples and 18 oranges on the screen pad!")
                            }
                        )
                    }
                }
            }
        }

        // CURRENT HOMEWORK PLAN
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Checklist,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Current Homework Plan",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface
                            )
                        }
                        Text(
                            text = "3 Assignments",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                    }

                    // Homework Item 1
                    HomeworkPlanItem(
                        title = "Math Worksheet #4",
                        subtitle = "Pending Step 3 • Due Tomorrow",
                        icon = Icons.Default.Calculate,
                        iconBg = PrimaryFixed,
                        iconTint = Primary,
                        buttonLabel = "Resume",
                        isButtonPrimary = true,
                        onClick = {
                            voiceHelper.speak("Resuming Math Worksheet number 4!")
                        }
                    )

                    // Homework Item 2
                    HomeworkPlanItem(
                        title = "Science Plant Journal",
                        subtitle = "All 4 steps complete • Due Friday",
                        icon = Icons.Default.Spa,
                        iconBg = TertiaryFixed,
                        iconTint = Tertiary,
                        completedBadge = true
                    )

                    // Homework Item 3
                    HomeworkPlanItem(
                        title = "Spelling Dictation",
                        subtitle = "10 Words • Due Monday",
                        icon = Icons.Default.Spellcheck,
                        iconBg = SecondaryFixed,
                        iconTint = Secondary,
                        buttonLabel = "Start",
                        isButtonPrimary = false,
                        onClick = {
                            voiceHelper.speak("Starting Spelling Dictation session!")
                        }
                    )
                }
            }
        }

        // TV / CLASSROOM PROJECTOR MIRROR ACTION
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Secondary),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tv,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Study on Big Screen",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isTvCasting) "Live Mirroring to Living Room TV ✓" else "Mirror live step-by-step whiteboard to Family TV",
                                fontSize = 11.sp,
                                color = SecondaryFixed
                            )
                        }
                    }

                    Button(
                        onClick = {
                            isTvCasting = !isTvCasting
                            if (isTvCasting) {
                                voiceHelper.speak("Connected to Big Screen TV!")
                            }
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTvCasting) TertiaryFixed else SurfaceContainerLowest,
                            contentColor = if (isTvCasting) OnTertiaryFixedVariant else Secondary
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isTvCasting) "Connected ✓" else "Cast TV",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = CircleShape,
        color = SurfaceContainerHigh,
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
        }
    }
}

@Composable
private fun HomeworkPlanItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    buttonLabel: String? = null,
    isButtonPrimary: Boolean = false,
    completedBadge: Boolean = false,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceContainerLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = if (completedBadge) Tertiary else OnSurfaceVariant
                    )
                }
            }

            if (completedBadge) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Completed",
                    tint = Tertiary,
                    modifier = Modifier.size(22.dp)
                )
            } else if (buttonLabel != null) {
                Button(
                    onClick = onClick,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isButtonPrimary) Primary else SurfaceContainerHigh,
                        contentColor = if (isButtonPrimary) OnPrimary else OnSurface
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        text = buttonLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
