package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MissionItem
import com.example.ui.theme.*
import com.example.util.VoiceHelper

@Composable
fun HomeScreen(
    voiceHelper: VoiceHelper,
    onOpenSolver: () -> Unit,
    onOpenReading: () -> Unit,
    onOpenSpelling: () -> Unit,
    onOpenVoiceAssistant: () -> Unit,
    onOpenParentPortal: () -> Unit,
    onOpenFaceRecognition: () -> Unit = {}
) {
    var missions by remember {
        mutableStateOf(
            listOf(
                MissionItem("m1", "10 min Story: 'The Brave Little Turtle'", "+25 Stars Earned ⭐", isCompleted = true),
                MissionItem("m2", "15 min Math: 2-Digit Addition", "In progress • 7 mins left", inProgress = true),
                MissionItem("m3", "10 min Spelling: 5 New Words", "Flashcards & Quiz"),
                MissionItem("m4", "10 min Science: Solar System", "3D Planet Explorer")
            )
        )
    }

    var isListeningPrompt by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_scale"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP GREETING & MASCOT STORYTELLER BANNER
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
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
                    // Learner level pill
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryFixed.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = "GRADE 3 LEARNER 🌟",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Primary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Greeting
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Hello Aarav!",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "👋",
                                    fontSize = 24.sp
                                )
                            }
                            Text(
                                text = "I am your AI Homework Teacher",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceVariant
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Tertiary.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Tertiary.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable { onOpenFaceRecognition() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = "Face ID",
                                    tint = Tertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Face ID 📸",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Tertiary
                                )
                            }
                        }
                    }

                    // Mascot & Speech Bubble Split
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Mascot Avatar with Ring
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(PrimaryContainer, PrimaryFixedDim)
                                    )
                                )
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(SurfaceContainerLowest)
                            ) {
                                AsyncImage(
                                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuApdNAFyBnjSWNPz_VNF6lq82A7mlmUZn5q0UeUdHoto5OUJx5iwWTplcXV0HLmMDP_gFnumzQgace4rfHMQKnVXOfhI5ZqpOVHfnLMg-XiTSEVSTC_KQM8Y46i_FHrIHYT1KT1WZJBRv1CqXo_AFRGpVdbwNh_tqmg1p9mOIzM3WJ1AUDdkt5wZb_6VPGWYEKN_IVQM3kgzv546IKpCbXqLVWMIpUQgx18Fu65FSdc0HkZNqHFixQBew",
                                    contentDescription = "Homework Teacher Mascot",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            // Hearing badge
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.BottomEnd)
                                    .clip(CircleShape)
                                    .background(Tertiary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Hearing,
                                    contentDescription = "Listening",
                                    tint = OnTertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        // Speech bubble
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = SecondaryFixed.copy(alpha = 0.35f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "\"What would you like to explore today? Speak or tap below!\"",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSecondaryFixed,
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SentimentVerySatisfied,
                                        contentDescription = null,
                                        tint = Tertiary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Ready in Kannada & English",
                                        fontSize = 11.sp,
                                        color = OnSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Big Tactile Voice Action Pill
                    Button(
                        onClick = {
                            isListeningPrompt = !isListeningPrompt
                            if (isListeningPrompt) {
                                voiceHelper.speak("Hi Aarav! I am listening. What can I help you with today?")
                            }
                            onOpenVoiceAssistant()
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryContainer,
                            contentColor = OnPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Speak in Kannada / English",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            // Sound wave bars
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height((12 * waveScale).dp)
                                        .clip(CircleShape)
                                        .background(OnPrimary)
                                )
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height((20 * waveScale).dp)
                                        .clip(CircleShape)
                                        .background(OnPrimary)
                                )
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height((14 * waveScale).dp)
                                        .clip(CircleShape)
                                        .background(OnPrimary)
                                )
                            }
                        }
                    }

                    // Kids Face Recognition & Motivational Greeting Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryFixed.copy(alpha = 0.85f)),
                        border = BorderStroke(1.5.dp, Primary.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenFaceRecognition() }
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Primary
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CameraAlt,
                                            contentDescription = null,
                                            tint = OnPrimary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "FACE DETECTED • AARAV 🌟",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = OnPrimary
                                        )
                                    }
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = SurfaceContainerLowest,
                                    modifier = Modifier.clickable {
                                        voiceHelper.speak("Hello, Aarav! How is your day today? What new thing did you learn? Keep shining, Superstar!")
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Speak",
                                            tint = Primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "Hear Pip",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Primary
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "\"Hello, Aarav! 🌟 How's your day today? What new thing did you learn? Keep shining, Superstar! 🚀\"",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnPrimaryFixed,
                                lineHeight = 19.sp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Smile: 100% 😄 • Superhero Mode 🦸",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnPrimaryFixedVariant
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = "Open Face ID",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Primary
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceContainerLow,
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
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FlagCircle,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Daily Goal: 35 of 60 mins",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = OnSurface
                                    )
                                }
                                Surface(
                                    shape = CircleShape,
                                    color = TertiaryFixed
                                ) {
                                    Text(
                                        text = "58% Done",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Tertiary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            // Rainbow Progress Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(14.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceContainerHighest)
                                    .padding(2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.58f)
                                        .fillMaxHeight()
                                        .clip(CircleShape)
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(PrimaryContainer, Secondary, TertiaryContainer)
                                            )
                                        )
                                )
                            }

                            // Badges row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = PrimaryFixed
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = Primary,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "4 Day Streak!",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = OnPrimaryFixed
                                        )
                                    }
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = SecondaryFixed
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = PrimaryContainer,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "140 Stars Won",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = OnSecondaryFixed
                                        )
                                    }
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = TertiaryFixed
                                ) {
                                    Text(
                                        text = "🎖️ Never Gives Up",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = OnTertiaryFixed,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // QUICK START HOMEWORK SECTION
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = null,
                            tint = PrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Quick Start Homework",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                    }
                    Text(
                        text = "Tap to open",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceVariant
                    )
                }

                // 1. Start Homework (Amber Card)
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(listOf(PrimaryContainer, PrimaryFixedDim))
                        )
                        .clickable { onOpenSolver() }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = CircleShape,
                                    color = SurfaceContainerLowest.copy(alpha = 0.3f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Timer,
                                            contentDescription = null,
                                            tint = OnPrimary,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "2 TASKS DUE TODAY",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = OnPrimary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Start Homework",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = OnPrimary
                                )
                                Text(
                                    text = "Step-by-step friendly help with Math, Science & English",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = OnPrimary.copy(alpha = 0.95f)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceContainerLowest.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = null,
                                    tint = OnPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Open Solver",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnPrimary
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // 2. Camera Teacher (Sky Blue Card)
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(listOf(Secondary, SecondaryContainer))
                        )
                        .clickable { onOpenSolver() }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = CircleShape,
                                    color = SurfaceContainerLowest.copy(alpha = 0.3f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PhotoCamera,
                                            contentDescription = null,
                                            tint = OnSecondary,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "INSTANT OCR HELP",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = OnSecondary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Camera Teacher",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = OnSecondary
                                )
                                Text(
                                    text = "Point camera at any textbook or notebook page for hints",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = OnSecondary.copy(alpha = 0.95f)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceContainerLowest.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DocumentScanner,
                                    contentDescription = null,
                                    tint = OnSecondary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Snap a Photo",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSecondary
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = OnSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Dual Mini Row: Read With Me & Word Builder
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Read With Me (Mint)
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(listOf(Tertiary, TertiaryContainer))
                            )
                            .clickable { onOpenReading() }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(SurfaceContainerLowest.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoStories,
                                        contentDescription = null,
                                        tint = OnTertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Read With Me",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnTertiary
                                )
                                Text(
                                    text = "AI highlights words & guides pronunciation",
                                    fontSize = 11.sp,
                                    color = OnTertiary.copy(alpha = 0.9f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "Start Story",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnTertiary
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = OnTertiary,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }

                    // Word Builder (Warm Violet / Primary)
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(listOf(Primary, PrimaryFixedDim))
                            )
                            .clickable { onOpenSpelling() }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(SurfaceContainerLowest.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Spellcheck,
                                        contentDescription = null,
                                        tint = OnPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Word Builder",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnPrimary
                                )
                                Text(
                                    text = "Connect letters, phonics & spelling mini-games",
                                    fontSize = 11.sp,
                                    color = OnPrimary.copy(alpha = 0.9f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "Play Game",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = OnPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // TODAY'S MISSIONS CHECKLIST
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
                                text = "Today's Missions",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = TertiaryFixed
                        ) {
                            val completedCount = missions.count { it.isCompleted }
                            Text(
                                text = "$completedCount / ${missions.size} Completed",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Tertiary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // Missions List
                    missions.forEachIndexed { index, mission ->
                        val itemColor = when {
                            mission.isCompleted -> SurfaceContainerLow
                            mission.inProgress -> SecondaryFixed.copy(alpha = 0.35f)
                            else -> SurfaceContainer
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = itemColor,
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
                                    if (mission.isCompleted) {
                                        Box(
                                            modifier = Modifier
                                                .size(26.dp)
                                                .clip(CircleShape)
                                                .background(Tertiary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Completed",
                                                tint = OnTertiary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else if (mission.inProgress) {
                                        Box(
                                            modifier = Modifier
                                                .size(26.dp)
                                                .clip(CircleShape)
                                                .background(Secondary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = "In progress",
                                                tint = OnSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(26.dp)
                                                .clip(CircleShape)
                                                .background(SurfaceContainerHighest),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(Outline)
                                            )
                                        }
                                    }

                                    Column {
                                        Text(
                                            text = mission.title,
                                            fontSize = 13.sp,
                                            fontWeight = if (mission.inProgress) FontWeight.ExtraBold else FontWeight.Bold,
                                            color = if (mission.inProgress) OnSecondaryFixed else OnSurface,
                                            textDecoration = if (mission.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = mission.subtitle,
                                            fontSize = 11.sp,
                                            fontWeight = if (mission.isCompleted) FontWeight.Bold else FontWeight.Normal,
                                            color = if (mission.isCompleted) Tertiary else if (mission.inProgress) Secondary else OnSurfaceVariant
                                        )
                                    }
                                }

                                if (mission.isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Done",
                                        tint = Tertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else if (mission.inProgress) {
                                    Button(
                                        onClick = { onOpenSolver() },
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Secondary,
                                            contentColor = OnSecondary
                                        ),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text(text = "Resume", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            // Toggle completion or start
                                            missions = missions.toMutableList().also { list ->
                                                list[index] = mission.copy(isCompleted = true)
                                            }
                                            voiceHelper.speak("Great job starting ${mission.title}!")
                                        },
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = SurfaceContainerLowest,
                                            contentColor = Primary
                                        ),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text(text = "Start", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // SUBJECTS & SKILLS EXPLORER GRID
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                            imageVector = Icons.Default.Category,
                            contentDescription = null,
                            tint = Tertiary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Subjects & Skills",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                    }
                    Text(
                        text = "Grade 1-5",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }

                // Grid rows
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Mathematics
                    SubjectCard(
                        title = "Mathematics",
                        sub = "ಗಣಿತ • Math",
                        desc = "Counting, Addition, Fractions & Word Problems",
                        levelBadge = "Level 4",
                        icon = Icons.Default.Calculate,
                        iconBg = PrimaryFixed,
                        iconTint = Primary,
                        badgeColor = Tertiary,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenSolver
                    )

                    // Science Lab
                    SubjectCard(
                        title = "Science Lab",
                        sub = "ವಿಜ್ಞಾನ • Science",
                        desc = "Plants, Solar System & DIY safe experiments",
                        levelBadge = "3 Labs Ready",
                        icon = Icons.Default.Biotech,
                        iconBg = SecondaryFixed,
                        iconTint = Secondary,
                        badgeColor = Secondary,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenSolver
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Handwriting
                    SubjectCard(
                        title = "Handwriting",
                        sub = "ಬರಹ • Trace",
                        desc = "Trace letters, numbers & sentence copy practice",
                        levelBadge = "Pad Ready",
                        icon = Icons.Default.Draw,
                        iconBg = TertiaryFixed,
                        iconTint = Tertiary,
                        badgeColor = Tertiary,
                        actionIcon = Icons.Default.Brush,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenSpelling
                    )

                    // Projects
                    SubjectCard(
                        title = "Projects",
                        sub = "School Ideas",
                        desc = "Safe step-by-step school science project maker",
                        levelBadge = "New Ideas",
                        icon = Icons.Default.Science,
                        iconBg = PrimaryFixedDim.copy(alpha = 0.4f),
                        iconTint = Primary,
                        badgeColor = Primary,
                        actionIcon = Icons.Default.Lightbulb,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenSolver
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Brain Games
                    SubjectCard(
                        title = "Brain Games",
                        sub = "ಆಟವಾಡಿ • Play",
                        desc = "Word Match, Number Match & Brain Puzzles",
                        levelBadge = "6 Games",
                        icon = Icons.Default.SportsEsports,
                        iconBg = SecondaryFixed,
                        iconTint = Secondary,
                        badgeColor = Secondary,
                        actionIcon = Icons.Default.SmartToy,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenSpelling
                    )

                    // TV Cast Mode
                    SubjectCard(
                        title = "TV Cast Mode",
                        sub = "Big Screen",
                        desc = "Cast lessons to Smart TV or classroom wall",
                        levelBadge = "Connect",
                        icon = Icons.Default.ConnectedTv,
                        iconBg = SurfaceContainerHigh,
                        iconTint = OnSurface,
                        badgeColor = OnSurfaceVariant,
                        actionIcon = Icons.Default.Cast,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenParentPortal
                    )
                }
            }
        }

        // MULTILINGUAL VOICE & PRONUNCIATION HELPER SECTION
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(PrimaryFixed.copy(alpha = 0.4f), SurfaceContainerLowest, SecondaryFixed.copy(alpha = 0.4f))
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "AI Voice Guide",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface
                            )
                            Text(
                                text = "Instant answers in Kannada & English",
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    // Suggested chips
                    Text(
                        text = "TRY SAYING:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        VoiceChip(
                            text = "🗣️ \"Teach me in Kannada\"",
                            onClick = {
                                voiceHelper.speak("Namaskara! Kannada kaliyona banni. Shall we start with alphabet?")
                                onOpenVoiceAssistant()
                            }
                        )
                        VoiceChip(
                            text = "🔢 \"What is 8 × 7?\"",
                            onClick = {
                                voiceHelper.speak("8 times 7 is 56! Great math question!")
                                onOpenVoiceAssistant()
                            }
                        )
                    }

                    VoiceChip(
                        text = "🦋 \"Spell Butterfly\"",
                        onClick = {
                            voiceHelper.speak("B-U-T-T-E-R-F-L-Y! Butterfly!")
                            onOpenSpelling()
                        }
                    )

                    // Word of the day card with real audio trigger
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceContainerLowest,
                        shadowElevation = 1.dp,
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
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(SecondaryFixed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Speak",
                                        tint = OnSecondaryFixed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Apple = ಸೇಬು (Kannada) / सेब (Hindi)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = OnSurface
                                    )
                                    Text(
                                        text = "Word of the Day",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Secondary
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    voiceHelper.speak("Apple! In Kannada it is Sebu. In Hindi it is Seb.")
                                },
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(SecondaryContainer)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Pronunciation",
                                    tint = OnSecondaryContainer,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // FOOTER SAFE AI BANNER
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceContainerLow,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🌱", fontSize = 20.sp)
                        Column {
                            Text(
                                text = "Learning is fun with AI Teacher!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface
                            )
                            Text(
                                text = "Safe, kid-friendly and parent verified",
                                fontSize = 10.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                    Surface(
                        shape = CircleShape,
                        color = TertiaryFixed
                    ) {
                        Text(
                            text = "Safe AI 🛡️",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Tertiary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SubjectCard(
    title: String,
    sub: String,
    desc: String,
    levelBadge: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    badgeColor: Color,
    actionIcon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Default.PlayCircle,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OnSurface
                )
                Text(
                    text = sub,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = iconTint
                )
                Text(
                    text = desc,
                    fontSize = 11.sp,
                    color = OnSurfaceVariant,
                    lineHeight = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceContainerLow,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = levelBadge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = badgeColor
                    )
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun VoiceChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = SurfaceContainerLowest,
        shadowElevation = 1.dp,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
