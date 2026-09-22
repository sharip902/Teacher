package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.ui.theme.*
import com.example.util.BilingualVocabulary
import com.example.util.PipAiTeacherBrain
import com.example.util.SpeechRecognitionHelper
import com.example.util.VoiceHelper
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatItem(
    val id: String = UUID.randomUUID().toString(),
    val sender: String, // "child" or "pip"
    val text: String,
    val spokenText: String = text,
    val bilingualVocab: BilingualVocabulary? = null,
    val suggestedFollowUps: List<String> = emptyList()
)

@Composable
fun VoiceAssistantScreen(
    voiceHelper: VoiceHelper,
    speechHelper: SpeechRecognitionHelper,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val speechState by speechHelper.state.collectAsState()

    var userPace by remember { mutableFloatStateOf(1.0f) }
    var customInputText by remember { mutableStateOf("") }

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
        if (isGranted) {
            speechHelper.startListening()
        }
    }

    var chatHistory by remember {
        mutableStateOf(
            listOf(
                ChatItem(
                    sender = "pip",
                    text = "Hello Aarav! 🌟 I am Teacher Pip AI, your voice study buddy! Tap the microphone and speak any question about your homework, math, science, or Kannada!",
                    spokenText = "Hello Aarav! I am Teacher Pip AI! Tap the microphone and speak any question you have!",
                    bilingualVocab = BilingualVocabulary(
                        english = "Teacher / Guide",
                        kannadaText = "ಗುರು / ಶಿಕ್ಷಕ",
                        transliteration = "Guru / Shikshaka",
                        meaning = "One who guides learning and wisdom"
                    ),
                    suggestedFollowUps = listOf(
                        "What is 8 times 7?",
                        "How do I say butterfly in Kannada?",
                        "Why is the sky blue?",
                        "Tell me a bedtime story"
                    )
                )
            )
        )
    }

    // Function to submit a question and get an intelligent AI teacher response
    fun submitQuestion(query: String) {
        val q = query.trim()
        if (q.isBlank()) return

        val teacherResponse = PipAiTeacherBrain.generateAnswer(q)

        val childItem = ChatItem(
            sender = "child",
            text = q
        )

        val pipItem = ChatItem(
            sender = "pip",
            text = teacherResponse.displayText,
            spokenText = teacherResponse.spokenText,
            bilingualVocab = teacherResponse.bilingualVocab,
            suggestedFollowUps = teacherResponse.suggestedFollowUps
        )

        chatHistory = chatHistory + childItem + pipItem

        // Automatically read aloud the answer
        voiceHelper.speak(teacherResponse.spokenText, rate = userPace)

        // Scroll to the latest message
        coroutineScope.launch {
            listState.animateScrollToItem(chatHistory.size + 3)
        }
    }

    // Register callback for SpeechRecognizer
    DisposableEffect(speechHelper) {
        speechHelper.onFinalResult = { recognizedText ->
            submitQuestion(recognizedText)
        }
        onDispose {
            speechHelper.cancel()
        }
    }

    // Continuous pulse animation for listening visualizer
    val infiniteTransition = rememberInfiniteTransition(label = "voice_rings")
    val ringScale1 by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring_scale_1"
    )
    val ringScale2 by infiniteTransition.animateFloat(
        initialValue = 1.15f,
        targetValue = 1.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring_scale_2"
    )

    // Dynamic RMS audio scaling (expands rings when child talks louder)
    val reactiveScale1 = ringScale1 + (speechState.rmsLevel * 0.35f)
    val reactiveScale2 = ringScale2 + (speechState.rmsLevel * 0.55f)

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // TOP STATUS BAR & CONTROLS
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(if (speechState.isListening) Tertiary else Outline)
                            )
                            Column {
                                Text(
                                    text = if (speechState.isListening) "Listening in Real-Time..." else "Voice Standby",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (speechState.isListening) Tertiary else OnSurface
                                )
                                Text(
                                    text = "Language: ${speechState.selectedLanguageName}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Secondary
                                )
                            }
                        }

                        // Pace Chip
                        Surface(
                            shape = CircleShape,
                            color = SecondaryFixed,
                            modifier = Modifier.clickable {
                                userPace = when (userPace) {
                                    1.0f -> 1.25f
                                    1.25f -> 0.75f
                                    else -> 1.0f
                                }
                                voiceHelper.setPace(userPace)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Speed, contentDescription = null, tint = OnSecondaryFixed, modifier = Modifier.size(13.dp))
                                Text(
                                    text = "${userPace}x",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnSecondaryFixed
                                )
                            }
                        }
                    }

                    // Language Selector Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val languages = listOf(
                            "English (India)" to "en-IN",
                            "English (US)" to "en-US",
                            "ಕನ್ನಡ (Kannada)" to "kn-IN",
                            "हिन्दी (Hindi)" to "hi-IN"
                        )
                        items(languages) { (name, tag) ->
                            val isSelected = speechState.selectedLanguageTag == tag
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) PrimaryContainer else SurfaceContainerLow,
                                border = if (isSelected) BorderStroke(1.dp, Primary) else null,
                                modifier = Modifier.clickable {
                                    speechHelper.setLanguage(tag, name)
                                    voiceHelper.speak("Voice language set to $name")
                                    if (speechState.isListening) {
                                        speechHelper.stopListening()
                                        speechHelper.startListening(tag)
                                    }
                                }
                            ) {
                                Text(
                                    text = name,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                    color = if (isSelected) OnPrimaryContainer else OnSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // PERMISSION REQUEST BANNER IF NOT GRANTED
        if (!hasAudioPermission) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryFixed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, tint = OnPrimary, modifier = Modifier.size(24.dp))
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Microphone Needed for Voice 🎙️",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = OnPrimaryFixed
                            )
                            Text(
                                text = "Allow mic access so Pip can listen to your homework questions in real-time!",
                                fontSize = 11.sp,
                                color = OnPrimaryFixedVariant,
                                lineHeight = 15.sp
                            )
                        }

                        Button(
                            onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = OnPrimary),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Allow", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // ANIMATED MASCOT CENTERPIECE WITH REACTIVE EQUALIZER
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.size(170.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Outer Pulsing Audio Ring 2
                        if (speechState.isListening) {
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .scale(reactiveScale2)
                                    .clip(CircleShape)
                                    .background(PrimaryContainer.copy(alpha = 0.15f))
                            )
                            // Outer Pulsing Audio Ring 1
                            Box(
                                modifier = Modifier
                                    .size(130.dp)
                                    .scale(reactiveScale1)
                                    .clip(CircleShape)
                                    .background(SecondaryContainer.copy(alpha = 0.25f))
                            )
                        }

                        // Central Mascot Avatar
                        Box(
                            modifier = Modifier
                                .size(105.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(PrimaryContainer, PrimaryFixedDim)
                                    )
                                )
                                .padding(4.dp),
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
                                    contentDescription = "Teacher Pip Voice",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bouncing equalizer bars synchronized to microphone RMS audio level!
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val baseHeights = listOf(14f, 26f, 38f, 20f, 32f, 18f, 28f, 16f)
                        baseHeights.forEachIndexed { i, h ->
                            val dynamicHeight = if (speechState.isListening) {
                                val boost = (speechState.rmsLevel * 30f) + (if (i % 2 == 0) ringScale1 * 8f else ringScale2 * 6f)
                                (h * 0.4f + boost).coerceIn(8f, 48f).dp
                            } else {
                                8.dp
                            }
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(dynamicHeight)
                                    .clip(CircleShape)
                                    .background(if (i % 2 == 0) Primary else Secondary)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = speechState.statusText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (speechState.isListening) Primary else OnSurfaceVariant
                    )
                }
            }
        }

        // REAL-TIME PARTIAL SPEECH TRANSCRIPTION PREVIEW
        item {
            AnimatedVisibility(
                visible = speechState.isListening,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PrimaryFixed,
                    border = BorderStroke(1.dp, PrimaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "REAL-TIME TRANSCRIPT (VOICE INPUT):",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Primary
                            )
                            Text(
                                text = if (speechState.partialText.isNotBlank()) {
                                    "\"${speechState.partialText}\""
                                } else {
                                    "Listening for your voice... Ask math, words, or stories!"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnPrimaryFixed
                            )
                        }
                    }
                }
            }
        }

        // CHAT CONVERSATION TRANSCRIPT
        items(chatHistory, key = { it.id }) { item ->
            if (item.sender == "child") {
                // Child Question Bubble (Right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        shape = RoundedCornerShape(18.dp, 4.dp, 18.dp, 18.dp),
                        color = PrimaryFixed,
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = "Aarav (You)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Primary
                                )
                                Text(
                                    text = item.text,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnPrimaryFixed
                                )
                            }
                        }
                    }
                }
            } else {
                // Pip AI Teacher Bubble (Left)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp),
                        color = SurfaceContainerLowest,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth(0.92f)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Teacher Pip AI",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Secondary
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        voiceHelper.speak(item.spokenText, rate = userPace)
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Read Aloud",
                                        tint = Secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Text(
                                text = item.text,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = OnSurface,
                                lineHeight = 20.sp
                            )

                            // Bilingual Vocabulary Card if present
                            item.bilingualVocab?.let { vocab ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = SecondaryFixed.copy(alpha = 0.35f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "${vocab.kannadaText} (${vocab.transliteration})",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Black,
                                                color = OnSecondaryFixed
                                            )
                                            Text(
                                                text = "${vocab.english}: ${vocab.meaning}",
                                                fontSize = 11.sp,
                                                color = OnSecondaryFixedVariant
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                voiceHelper.speak("${vocab.transliteration}! In Kannada, ${vocab.english} is ${vocab.transliteration}.")
                                            },
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(SecondaryContainer)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = "Pronounce",
                                                tint = OnSecondaryContainer,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Suggested Follow-up chips
                            if (item.suggestedFollowUps.isNotEmpty()) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "ASK PIP NEXT:",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = OnSurfaceVariant
                                    )
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        items(item.suggestedFollowUps) { prompt ->
                                            Surface(
                                                shape = CircleShape,
                                                color = SurfaceContainerLow,
                                                modifier = Modifier.clickable {
                                                    submitQuestion(prompt)
                                                }
                                            ) {
                                                Text(
                                                    text = prompt,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Primary,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // SUGGESTED QUICK VOICE PROMPTS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "TRY SPEAKING OR TAPPING THESE QUESTIONS:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = OnSurfaceVariant
                )

                val suggestions = listOf(
                    "🔢 \"What is 8 times 7?\"" to "What is 8 times 7?",
                    "🦋 \"How do I say butterfly in Kannada?\"" to "How do I say butterfly in Kannada?",
                    "🌿 \"How do plants make food in photosynthesis?\"" to "How do plants make food in photosynthesis?",
                    "🌤️ \"Why is the sky blue?\"" to "Why is the sky blue?",
                    "😄 \"Tell me a funny joke!\"" to "Tell me a funny joke",
                    "📖 \"Tell me a story about an animal\"" to "Tell me a story about an animal"
                )

                suggestions.forEach { (label, query) ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerLowest,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                submitQuestion(query)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }

        // REAL-TIME MICROPHONE CONTROLLER (BIG TACTILE BUTTON)
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Big glowing mic button with live state feedback
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .shadow(8.dp, CircleShape, spotColor = if (speechState.isListening) Tertiary else PrimaryContainer)
                            .clip(CircleShape)
                            .background(
                                if (speechState.isListening) Tertiary else PrimaryContainer
                            )
                            .clickable {
                                if (!hasAudioPermission) {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                } else {
                                    if (speechState.isListening) {
                                        speechHelper.stopListening()
                                    } else {
                                        voiceHelper.stop()
                                        speechHelper.startListening()
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (speechState.isListening) Icons.Default.Mic else Icons.Default.MicNone,
                            contentDescription = "Microphone",
                            tint = if (speechState.isListening) OnTertiary else OnPrimaryContainer,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = if (speechState.isListening) {
                            "Listening... Tap to Complete"
                        } else {
                            "Tap Mic to Ask a Question Verbally"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = if (speechState.isListening) Tertiary else Primary
                    )

                    // Error notice if any
                    speechState.errorMessage?.let { err ->
                        Text(
                            text = err,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Error
                        )
                    }

                    // Text fallback input for typing questions
                    OutlinedTextField(
                        value = customInputText,
                        onValueChange = { customInputText = it },
                        placeholder = { Text("Or type question here...", fontSize = 13.sp) },
                        trailingIcon = {
                            if (customInputText.isNotBlank()) {
                                IconButton(onClick = {
                                    val text = customInputText
                                    customInputText = ""
                                    submitQuestion(text)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = "Send",
                                        tint = Primary
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
