package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*
import com.example.util.VoiceHelper

@Composable
fun PlayAndPracticeScreen(
    voiceHelper: VoiceHelper,
    initialTab: Int = 1 // 0: Story & Grammar, 1: Spell It, 2: Read With Me, 3: Phonics Lab
) {
    var selectedTab by remember { mutableStateOf(initialTab) }

    // Spelling State
    var slot3Letter by remember { mutableStateOf<String?>(null) }
    var slot6Letter by remember { mutableStateOf<String?>(null) }
    val isSpellingComplete = slot3Letter == "T" && slot6Letter == "F"

    // Grammar Story Studio State
    var storyText by remember {
        mutableStateOf("teh litel puppy likes to run under the sunny blue sky.")
    }
    var isTehFixed by remember { mutableStateOf(false) }
    var isLitelFixed by remember { mutableStateOf(false) }
    var studioPace by remember { mutableStateOf(1.0f) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mascot Top Cheer Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(PrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuApdNAFyBnjSWNPz_VNF6lq82A7mlmUZn5q0UeUdHoto5OUJx5iwWTplcXV0HLmMDP_gFnumzQgace4rfHMQKnVXOfhI5ZqpOVHfnLMg-XiTSEVSTC_KQM8Y46i_FHrIHYT1KT1WZJBRv1CqXo_AFRGpVdbwNh_tqmg1p9mOIzM3WJ1AUDdkt5wZb_6VPGWYEKN_IVQM3kgzv546IKpCbXqLVWMIpUQgx18Fu65FSdc0HkZNqHFixQBew",
                                contentDescription = "Pip",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column {
                            Text(
                                text = "Hi Little Explorer! ✨",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = OnSurface
                            )
                            Text(
                                text = "Level 2 Phonics & Spelling Adventure",
                                fontSize = 12.sp,
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = TertiaryFixed
                    ) {
                        Text(
                            text = "+15 Pts ⭐",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Tertiary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Activity Selector Tabs
        item {
            val tabs = listOf("Story & Grammar", "Spell It!", "Read With Me", "Phonics Lab")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(tabs.indices.toList()) { index ->
                    val isSelected = selectedTab == index
                    Surface(
                        shape = CircleShape,
                        color = if (isSelected) Primary else SurfaceContainerLow,
                        modifier = Modifier.clickable { selectedTab = index }
                    ) {
                        Text(
                            text = tabs[index],
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                            color = if (isSelected) OnPrimary else OnSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // TAB 1: SPELL IT! (from Image 7)
        if (selectedTab == 1 || selectedTab == 3) {
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
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = PrimaryFixed
                            ) {
                                Text(
                                    text = "CHALLENGE #4 • NATURE 🌸",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Text(
                                text = "Word 3 of 5",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceVariant
                            )
                        }

                        // Tutor prompt
                        Text(
                            text = "Spell the word: BUTTERFLY 🦋",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = OnSurface
                        )

                        // Sound buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    voiceHelper.speak("Butterfly! A colorful butterfly fluttering in the garden.")
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SecondaryContainer,
                                    contentColor = OnSecondaryContainer
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Listen Closely", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Button(
                                onClick = {
                                    voiceHelper.speak("But... ter... fly!", rate = 0.6f)
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SurfaceContainerLow,
                                    contentColor = OnSurface
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.SlowMotionVideo, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Slow: 'But-ter-fly'", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Bilingual Discovery Cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = SurfaceContainerLow,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "English", fontSize = 10.sp, color = OnSurfaceVariant, fontWeight = FontWeight.Bold)
                                    Text(text = "Butterfly", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = OnSurface)
                                    Text(text = "A winged insect with colorful wings", fontSize = 10.sp, color = OnSurfaceVariant)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = SecondaryFixed.copy(alpha = 0.4f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        voiceHelper.speak("Chitte! In Kannada, a butterfly is Chitte!")
                                    }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "ಕನ್ನಡ (Kannada)", fontSize = 10.sp, color = Secondary, fontWeight = FontWeight.Bold)
                                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Secondary, modifier = Modifier.size(14.dp))
                                    }
                                    Text(text = "ಚಿಟ್ಟೆ (Chitte)", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = OnSecondaryFixed)
                                    Text(text = "Pronounced: Chith-they", fontSize = 10.sp, color = OnSecondaryFixedVariant)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = TertiaryFixed.copy(alpha = 0.4f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        voiceHelper.speak("Titli! In Hindi, a butterfly is Titli!")
                                    }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "हिंदी (Hindi)", fontSize = 10.sp, color = Tertiary, fontWeight = FontWeight.Bold)
                                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Tertiary, modifier = Modifier.size(14.dp))
                                    }
                                    Text(text = "तितली (Titli)", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = OnTertiaryFixed)
                                    Text(text = "Pronounced: Tit-lee", fontSize = 10.sp, color = OnTertiaryFixedVariant)
                                }
                            }
                        }

                        // Target Tray: B U T [?] E R [?] L Y
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSpellingComplete) TertiaryFixed.copy(alpha = 0.35f) else SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isSpellingComplete) "Solved! 🎉" else "Drag or Tap Letters into Tray:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSpellingComplete) Tertiary else OnSurfaceVariant
                                    )
                                    if (slot3Letter != null || slot6Letter != null) {
                                        Text(
                                            text = "Clear Tray",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Error,
                                            modifier = Modifier.clickable {
                                                slot3Letter = null
                                                slot6Letter = null
                                            }
                                        )
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Letters in BUTTERFLY
                                    LetterSlot("B", filled = true)
                                    LetterSlot("U", filled = true)
                                    LetterSlot("T", filled = true)
                                    LetterSlot(
                                        letter = slot3Letter ?: "_",
                                        filled = slot3Letter != null,
                                        isTargetSlot = true,
                                        isCorrect = slot3Letter == "T"
                                    )
                                    LetterSlot("E", filled = true)
                                    LetterSlot("R", filled = true)
                                    LetterSlot(
                                        letter = slot6Letter ?: "_",
                                        filled = slot6Letter != null,
                                        isTargetSlot = true,
                                        isCorrect = slot6Letter == "F"
                                    )
                                    LetterSlot("L", filled = true)
                                    LetterSlot("Y", filled = true)
                                }

                                if (isSpellingComplete) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Tertiary
                                    ) {
                                        Text(
                                            text = "🌟 Brilliant! B-U-T-T-E-R-F-L-Y! You won 15 stars!",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Letter Tiles Bank to Tap
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "AVAILABLE LETTER TILES:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = OnSurfaceVariant
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val tiles = listOf("T", "F", "P", "A", "S")
                                tiles.forEach { letter ->
                                    Button(
                                        onClick = {
                                            if (slot3Letter == null) {
                                                slot3Letter = letter
                                                if (letter == "T") {
                                                    voiceHelper.speak("Good job! T goes into the fourth slot.")
                                                } else {
                                                    voiceHelper.speak("Try again! Listen to But-ter...")
                                                }
                                            } else if (slot6Letter == null) {
                                                slot6Letter = letter
                                                if (letter == "F") {
                                                    voiceHelper.speak("Awesome! F is for fly! Butterfly is complete!")
                                                } else {
                                                    voiceHelper.speak("Almost there! Listen to fly...")
                                                }
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = SurfaceContainerHighest,
                                            contentColor = OnSurface
                                        ),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                    ) {
                                        Text(text = letter, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                    }
                                }
                            }
                        }

                        // Hint Card
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimaryFixed.copy(alpha = 0.4f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "💡", fontSize = 18.sp)
                                Text(
                                    text = "Hint: 'Butterfly' is a compound word: BUTTER + FLY! Both words combine into one insect.",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = OnPrimaryFixedVariant,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            // Phonics & Rhyme Lab Card
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
                                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = Secondary, modifier = Modifier.size(18.dp))
                                Text(text = "Phonics & Rhyme Lab", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = OnSurface)
                            }
                            Surface(shape = CircleShape, color = SecondaryFixed) {
                                Text(
                                    text = "-AT Family",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSecondaryFixed,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "Sound: /k/ + /æ/ + /t/ = CAT 🐱",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )

                        Text(
                            text = "Question: What sound does a cat make?",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    voiceHelper.speak("Meow! Correct! Cats say meow!")
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TertiaryFixed,
                                    contentColor = OnTertiaryFixedVariant
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "🐾 Meow!", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    voiceHelper.speak("Woof is for dogs! Try again!")
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SurfaceContainerLow,
                                    contentColor = OnSurface
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "🐶 Woof!", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Rhyming words unlocked
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RhymeChip(
                                word = "BAT 🦇",
                                onClick = { voiceHelper.speak("Bat! B-A-T!") },
                                modifier = Modifier.weight(1f)
                            )
                            RhymeChip(
                                word = "HAT 🎩",
                                onClick = { voiceHelper.speak("Hat! H-A-T!") },
                                modifier = Modifier.weight(1f)
                            )
                            RhymeChip(
                                word = "MAT 🧘",
                                onClick = { voiceHelper.speak("Mat! M-A-T!") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // TAB 0: STORY & GRAMMAR STUDIO (from Image 12)
        if (selectedTab == 0) {
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
                        // AI Listening & Pace control
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Hearing, contentDescription = null, tint = Tertiary, modifier = Modifier.size(18.dp))
                                Text(text = "AI Listening & Pace", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                PacePill("🐢 0.75x", studioPace == 0.75f) {
                                    studioPace = 0.75f
                                    voiceHelper.setPace(0.75f)
                                }
                                PacePill("Pip 1.0x", studioPace == 1.0f) {
                                    studioPace = 1.0f
                                    voiceHelper.setPace(1.0f)
                                }
                                PacePill("⚡ 1.25x", studioPace == 1.25f) {
                                    studioPace = 1.25f
                                    voiceHelper.setPace(1.25f)
                                }
                            }
                        }

                        // Child Story & Sentence Studio Box
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
                                    Text(
                                        text = "CHILD STORY & SENTENCE STUDIO",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = OnSurfaceVariant
                                    )
                                    Text(
                                        text = "11 Words • 2 slips to polish",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Secondary
                                    )
                                }

                                Text(
                                    text = if (isTehFixed && isLitelFixed)
                                        "The little puppy likes to run under the sunny blue sky."
                                    else if (isTehFixed)
                                        "The litel puppy likes to run under the sunny blue sky."
                                    else if (isLitelFixed)
                                        "teh little puppy likes to run under the sunny blue sky."
                                    else
                                        storyText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface,
                                    lineHeight = 22.sp
                                )

                                // Action Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    StudioAction(
                                        icon = Icons.Default.Spellcheck,
                                        label = "Ready Check",
                                        tint = Primary,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            voiceHelper.speak("Checking story! You wrote 11 great words. Let's fix teh and litel!")
                                        }
                                    )
                                    StudioAction(
                                        icon = Icons.Default.VolumeUp,
                                        label = "Read Aloud",
                                        tint = Secondary,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            voiceHelper.speak(
                                                if (isTehFixed && isLitelFixed)
                                                    "The little puppy likes to run under the sunny blue sky."
                                                else storyText,
                                                rate = studioPace
                                            )
                                        }
                                    )
                                    StudioAction(
                                        icon = Icons.Default.Translate,
                                        label = "Explain Word",
                                        tint = Tertiary,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            voiceHelper.speak("Puppy means a young baby dog. In Kannada it is Naayi mari!")
                                        }
                                    )
                                    StudioAction(
                                        icon = Icons.Default.MusicNote,
                                        label = "Add Rhyme",
                                        tint = PrimaryContainer,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            voiceHelper.speak("Sunny rhymes with bunny and funny!")
                                        }
                                    )
                                }
                            }
                        }

                        // Ready Check Spelling & Grammar Corrections
                        Text(
                            text = "READY CHECK: SPELLING & GRAMMAR",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = OnSurfaceVariant
                        )

                        // Slip 1: teh -> the
                        GrammarSlipCard(
                            wrong = "teh",
                            fixed = "The",
                            rule = "Transposed Letters (Rule of 'TH')",
                            tip = "English sight words start with 'th'. Tap to fix!",
                            isFixed = isTehFixed,
                            onFix = {
                                isTehFixed = true
                                voiceHelper.speak("Fixed! 't-e-h' becomes 'The' with a capital T!")
                            }
                        )

                        // Slip 2: litel -> little
                        GrammarSlipCard(
                            wrong = "litel",
                            fixed = "little",
                            rule = "Double 'tt' sound & 'le' ending",
                            tip = "Short /i/ vowel is followed by double consonant 'tt'. Tap to fix!",
                            isFixed = isLitelFixed,
                            onFix = {
                                isLitelFixed = true
                                voiceHelper.speak("Fixed! 'litel' becomes 'little' with double t!")
                            }
                        )

                        // Teacher Pip's Coaching Corner
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = PrimaryFixed.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = OnPrimary, modifier = Modifier.size(20.dp))
                                }
                                Column {
                                    Text(
                                        text = "Teacher Pip's Coaching Corner",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = OnPrimaryFixed
                                    )
                                    Text(
                                        text = "Remember: Every sentence starts with a big capital letter! You are writing like a real author.",
                                        fontSize = 11.sp,
                                        color = OnPrimaryFixedVariant,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 2: READ WITH ME (KARAOKE PRONUNCIATION)
        if (selectedTab == 2) {
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
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(shape = CircleShape, color = TertiaryFixed) {
                                Text(
                                    text = "READING ACCURACY • 94% 🌟",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Tertiary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Text(text = "48 WPM", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Secondary)
                        }

                        Text(
                            text = "Story: The Brave Little Turtle",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = OnSurface
                        )

                        // Reading passage card
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "\"The little bird hopped onto the green branch. It chirped a happy morning song to the sun.\"",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface,
                                    lineHeight = 24.sp
                                )
                            }
                        }

                        // Action buttons
                        Button(
                            onClick = {
                                voiceHelper.speak("The little bird hopped onto the green branch. It chirped a happy morning song to the sun.")
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer, contentColor = OnPrimaryContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "AI Reads First (Karaoke Mode)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                voiceHelper.speak("Listening to you read! Go ahead Aarav, speak clearly!")
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary, contentColor = OnSecondary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Hold to Read Aloud (Child Voice)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LetterSlot(
    letter: String,
    filled: Boolean,
    isTargetSlot: Boolean = false,
    isCorrect: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isCorrect) TertiaryFixed
                else if (isTargetSlot) SecondaryFixed.copy(alpha = 0.5f)
                else SurfaceContainerHighest
            )
            .border(
                width = if (isTargetSlot) 2.dp else 1.dp,
                color = if (isCorrect) Tertiary else if (isTargetSlot) Secondary else OutlineVariant,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = if (isCorrect) OnTertiaryFixedVariant else if (filled) OnSurface else OnSurfaceVariant
        )
    }
}

@Composable
private fun RhymeChip(
    word: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = SurfaceContainerLow,
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Secondary, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = word, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurface)
        }
    }
}

@Composable
private fun PacePill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = if (isSelected) Secondary else SurfaceContainerLow,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) OnSecondary else OnSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun StudioAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 1.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GrammarSlipCard(
    wrong: String,
    fixed: String,
    rule: String,
    tip: String,
    isFixed: Boolean,
    onFix: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isFixed) TertiaryFixed.copy(alpha = 0.3f) else SurfaceContainerLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = wrong,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isFixed) Outline else Error
                    )
                    Text(text = "➔", fontSize = 12.sp, color = OnSurfaceVariant)
                    Text(
                        text = fixed,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Tertiary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = rule, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                Text(text = tip, fontSize = 10.sp, color = OnSurfaceVariant)
            }

            Button(
                onClick = onFix,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFixed) Tertiary else Secondary,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text(
                    text = if (isFixed) "Fixed ✓" else "Tap to Fix",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
