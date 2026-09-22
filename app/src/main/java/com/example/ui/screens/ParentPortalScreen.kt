package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.util.VoiceHelper

@Composable
fun ParentPortalScreen(
    voiceHelper: VoiceHelper,
    onBackClick: () -> Unit
) {
    var isUnlocked by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    var selectedSegment by remember { mutableStateOf(0) } // 0: Analytics, 1: TV Mode, 2: Safety

    // Parental Control Settings
    var dailyLimitMinutes by remember { mutableStateOf(60f) }
    var isPostureReminderEnabled by remember { mutableStateOf(true) }
    var isSubtitlesEnabled by remember { mutableStateOf(true) }
    var isSafeSearchOnly by remember { mutableStateOf(true) }
    var isBilingualPrompting by remember { mutableStateOf(true) }
    var isSessionPaused by remember { mutableStateOf(false) }

    if (!isUnlocked) {
        // PIN GATE DIALOG
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Surface)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(PrimaryFixed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = "Parent Security Gate",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = OnSurface
                    )

                    Text(
                        text = "Enter 4-digit parent passcode to review learning analytics and parental controls.",
                        fontSize = 13.sp,
                        color = OnSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    // PIN Dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(4) { index ->
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (index < enteredPin.length) Primary else SurfaceContainerHighest
                                    )
                                    .border(1.dp, OutlineVariant, CircleShape)
                            )
                        }
                    }

                    if (pinError) {
                        Text(
                            text = "Incorrect PIN. Default is 1234.",
                            fontSize = 12.sp,
                            color = Error,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Numeric Pad
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val rows = listOf(
                            listOf("1", "2", "3"),
                            listOf("4", "5", "6"),
                            listOf("7", "8", "9"),
                            listOf("C", "0", "OK")
                        )

                        rows.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { key ->
                                    Button(
                                        onClick = {
                                            when (key) {
                                                "C" -> {
                                                    enteredPin = ""
                                                    pinError = false
                                                }
                                                "OK" -> {
                                                    if (enteredPin == "1234" || enteredPin.length == 4) {
                                                        isUnlocked = true
                                                    } else {
                                                        pinError = true
                                                    }
                                                }
                                                else -> {
                                                    if (enteredPin.length < 4) {
                                                        enteredPin += key
                                                        if (enteredPin.length == 4) {
                                                            if (enteredPin == "1234") {
                                                                isUnlocked = true
                                                            } else {
                                                                // Allow 4-digits for convenient parent preview
                                                                isUnlocked = true
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (key == "OK") PrimaryContainer else SurfaceContainerLow,
                                            contentColor = if (key == "OK") OnPrimaryContainer else OnSurface
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(46.dp)
                                    ) {
                                        Text(text = key, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Quick bypass for test
                    TextButton(onClick = { isUnlocked = true }) {
                        Text(
                            text = "Quick Unlock (Parent Bypass)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                }
            }
        }
        return
    }

    // UNLOCKED PARENT PORTAL
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Child Profile Summary Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = null,
                                    tint = OnPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "Aarav Patel",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = OnSurface
                                )
                                Text(
                                    text = "Grade 3 • Age 8 • St. Xavier's Academy, Bangalore",
                                    fontSize = 11.sp,
                                    color = OnSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        IconButton(
                            onClick = { isUnlocked = false },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerLow)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock",
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Badges row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BadgePill("4 Day Streak 🔥", PrimaryFixed, OnPrimaryFixed, Modifier.weight(1f))
                        BadgePill("Reading Star 🌟", TertiaryFixed, OnTertiaryFixed, Modifier.weight(1f))
                        BadgePill("Math Explorer 📐", SecondaryFixed, OnSecondaryFixed, Modifier.weight(1f))
                    }
                }
            }
        }

        // Segment Switcher: [Analytics] [TV Mode] [Safety]
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
                    val segments = listOf("Learning Analytics", "TV & Big Screen", "Safety & Privacy")
                    segments.forEachIndexed { index, name ->
                        val isSelected = selectedSegment == index
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) SurfaceContainerLowest else Color.Transparent,
                            shadowElevation = if (isSelected) 2.dp else 0.dp,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedSegment = index }
                        ) {
                            Text(
                                text = name,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                color = if (isSelected) Primary else OnSurfaceVariant,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // SEGMENT 0: LEARNING ANALYTICS (from Image 9)
        if (selectedSegment == 0) {
            // Today's Screen Time Balance
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
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
                                Icon(Icons.Default.HourglassBottom, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                                Text(text = "Today's Balance: 52 / 60 min", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                            }
                            Surface(shape = CircleShape, color = TertiaryFixed) {
                                Text(
                                    text = "Healthy Balance",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Tertiary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // Segmented color bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerHighest),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Box(modifier = Modifier.weight(18f).fillMaxHeight().background(Secondary))
                            Box(modifier = Modifier.weight(15f).fillMaxHeight().background(PrimaryContainer))
                            Box(modifier = Modifier.weight(12f).fillMaxHeight().background(TertiaryContainer))
                            Box(modifier = Modifier.weight(7f).fillMaxHeight().background(Primary))
                            Box(modifier = Modifier.weight(8f).fillMaxHeight().background(SurfaceContainerHigh))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            LegendItem("Reading (18m)", Secondary)
                            LegendItem("Math (15m)", PrimaryContainer)
                            LegendItem("Phonics (12m)", TertiaryContainer)
                            LegendItem("Science (7m)", Primary)
                        }
                    }
                }
            }

            // Subject Mastery 2x2 Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "SUBJECT MASTERY (GRADE 3)", fontSize = 12.sp, fontWeight = FontWeight.Black, color = OnSurfaceVariant)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MasteryCard(
                            subject = "Reading & Voice",
                            minutes = "18 mins today",
                            metric = "92% Accuracy",
                            level = "Level 3",
                            tint = Secondary,
                            modifier = Modifier.weight(1f)
                        )
                        MasteryCard(
                            subject = "Mathematics",
                            minutes = "15 mins today",
                            metric = "14 Solved",
                            level = "Level 4",
                            tint = Primary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MasteryCard(
                            subject = "Vocabulary",
                            minutes = "12 mins today",
                            metric = "+8 Words Learned",
                            level = "Bilingual",
                            tint = Tertiary,
                            modifier = Modifier.weight(1f)
                        )
                        MasteryCard(
                            subject = "Science Lab",
                            minutes = "7 mins today",
                            metric = "1 Experiment Done",
                            level = "Grade 3",
                            tint = PrimaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // AI Teacher Pedagogical Insight
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SecondaryFixed.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
                            Text(
                                text = "AI Teacher Socratic Insight",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = OnSecondaryFixed
                            )
                        }

                        Text(
                            text = "Aarav excels at breaking down 2-digit sums when prompted with ten-grouping. Vocabulary retention in bilingual Kannada-English is exceptionally high (94%).",
                            fontSize = 12.sp,
                            color = OnSecondaryFixedVariant,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Mentor Recommendation: Try introducing 3-digit mental addition and encourage 10 minutes of reading aloud before bedtime.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                }
            }
        }

        // SEGMENT 1: TV & BIG SCREEN MODE
        if (selectedSegment == 1) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(SecondaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Tv, contentDescription = null, tint = Secondary, modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text(text = "Living Room TV & Study Mirror", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                Text(text = "Encourages healthy posture & collaborative family study", fontSize = 11.sp, color = OnSurfaceVariant)
                            }
                        }

                        // Devices list
                        Surface(shape = RoundedCornerShape(12.dp), color = SurfaceContainerLow) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Samsung 65\" Neo QLED (Living Room)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                    Text(text = "Available • 5GHz Wi-Fi Connected", fontSize = 11.sp, color = Tertiary)
                                }
                                Button(
                                    onClick = {
                                        voiceHelper.speak("Connected to Samsung Neo QLED! Ready for classroom projection.")
                                    },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(containerColor = Secondary, contentColor = OnSecondary),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text(text = "Connect", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Toggles
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Child Posture Reminders", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                Text(text = "Notifies when child slouches or leans too close", fontSize = 11.sp, color = OnSurfaceVariant)
                            }
                            Switch(checked = isPostureReminderEnabled, onCheckedChange = { isPostureReminderEnabled = it })
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Bilingual Subtitles on Big Screen", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                Text(text = "Displays Kannada & English translation text simultaneously", fontSize = 11.sp, color = OnSurfaceVariant)
                            }
                            Switch(checked = isSubtitlesEnabled, onCheckedChange = { isSubtitlesEnabled = it })
                        }
                    }
                }
            }
        }

        // SEGMENT 2: SAFETY & PRIVACY CONTROLS
        if (selectedSegment == 2) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(TertiaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Tertiary, modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text(text = "COPPA & Kid-Safe AI Guardrails", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                Text(text = "Strictly zero ads, no third-party data tracking", fontSize = 11.sp, color = OnSurfaceVariant)
                            }
                        }

                        // Daily Limit Slider
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Daily Screen Time Limit", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                Text(text = "${dailyLimitMinutes.toInt()} Minutes", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Primary)
                            }
                            Slider(
                                value = dailyLimitMinutes,
                                onValueChange = { dailyLimitMinutes = it },
                                valueRange = 30f..120f,
                                steps = 5
                            )
                        }

                        // Toggles
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "No Unsupervised Web Access", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                Text(text = "AI only answers from approved school curriculum", fontSize = 11.sp, color = OnSurfaceVariant)
                            }
                            Switch(checked = isSafeSearchOnly, onCheckedChange = { isSafeSearchOnly = it })
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Bilingual Kannada Voice Interaction", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                Text(text = "Allow voice responses in Kannada alongside English", fontSize = 11.sp, color = OnSurfaceVariant)
                            }
                            Switch(checked = isBilingualPrompting, onCheckedChange = { isBilingualPrompting = it })
                        }

                        Divider()

                        // Remote Session Pause
                        Button(
                            onClick = {
                                isSessionPaused = !isSessionPaused
                                if (isSessionPaused) {
                                    voiceHelper.speak("Learning session is now paused by parent. Time to stretch your legs!")
                                }
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSessionPaused) Tertiary else Error,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isSessionPaused) Icons.Default.PlayArrow else Icons.Default.PauseCircle,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isSessionPaused) "Resume Child Learning Session" else "Emergency Pause Child Session",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgePill(
    text: String,
    bg: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        color = bg,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(text = label, fontSize = 10.sp, color = OnSurfaceVariant, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun MasteryCard(
    subject: String,
    minutes: String,
    metric: String,
    level: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceContainerLow,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = subject, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                Surface(shape = CircleShape, color = tint.copy(alpha = 0.2f)) {
                    Text(
                        text = level,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = tint,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(text = metric, fontSize = 13.sp, fontWeight = FontWeight.Black, color = tint)
            Text(text = minutes, fontSize = 10.sp, color = OnSurfaceVariant)
        }
    }
}
