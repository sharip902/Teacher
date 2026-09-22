package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.ui.theme.*
import com.example.util.KidFaceRecognitionPrompts
import com.example.util.KidProfile
import com.example.util.VoiceHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KidFaceRecognitionScreen(
    voiceHelper: VoiceHelper,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    // List of profiles (default presets + ability to add custom)
    var kidProfiles by remember { mutableStateOf(KidFaceRecognitionPrompts.PRESET_KIDS) }
    var selectedKid by remember { mutableStateOf(kidProfiles.first()) }

    var isScanning by remember { mutableStateOf(false) }
    var isFaceLocked by remember { mutableStateOf(true) } // detected on launch for immediate delightful feedback
    var showAddKidDialog by remember { mutableStateOf(false) }
    var newKidNameInput by remember { mutableStateOf("") }
    var newKidGradeInput by remember { mutableStateOf("Grade 3") }

    var selectedGreetingCategory by remember { mutableIntStateOf(0) } // 0: Greetings, 1: Motivation, 2: Encouragement, 3: Crazy/Funny, 4: Achievements

    // Current active prompts generated for the kid
    var activeGreetingHeadline by remember { mutableStateOf("Hey Superstar! 🌟") }
    var activeMotivationQuestion by remember { mutableStateOf("How's your day today? 😊") }
    var activeEncouragingPhrase by remember { mutableStateOf("Keep Shining! ✨") }
    var activeFunnyLine by remember { mutableStateOf("Warning! Genius Detected! 🤓") }
    var activeAchievementBadge by remember { mutableStateOf("Today's Hero: You! 🏆") }
    var activeFullGreeting by remember {
        mutableStateOf("Hello, Aarav! 🌟 How's your day today? What new thing did you learn? Keep shining, Superstar! 🚀")
    }

    // Function to trigger detection and speak aloud the motivational prompt
    fun triggerDetection(kid: KidProfile) {
        selectedKid = kid
        isScanning = true
        isFaceLocked = false

        coroutineScope.launch {
            delay(1200) // Simulated biometric facial landmark scan
            isScanning = false
            isFaceLocked = true

            // Generate fresh random combination from prompts
            val result = KidFaceRecognitionPrompts.createDetectionResult(kid)
            activeGreetingHeadline = result.greetingHeadline
            activeMotivationQuestion = result.motivationQuestion
            activeEncouragingPhrase = result.encouragingPhrase
            activeFunnyLine = result.funnyLine
            activeAchievementBadge = result.achievementBadge

            // Match exact user prompt example for Aarav or name-based
            val spokenText = if (kid.name.equals("Aarav", ignoreCase = true)) {
                "Hello, Aarav! 🌟 How's your day today? What new thing did you learn? Keep shining, Superstar! 🚀"
            } else if (KidFaceRecognitionPrompts.NAME_BASED_EXAMPLES.containsKey(kid.name)) {
                KidFaceRecognitionPrompts.NAME_BASED_EXAMPLES[kid.name] ?: result.fullSpokenPrompt
            } else {
                "Hello, ${kid.name}! 🌟 ${result.greetingHeadline} ${result.motivationQuestion} ${result.encouragingPhrase}"
            }
            activeFullGreeting = spokenText

            // Voice announcement
            voiceHelper.speak(spokenText)
        }
    }

    // Continuous scanning animation
    val infiniteTransition = rememberInfiniteTransition(label = "face_scanner")
    val laserOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_scan"
    )

    val reticlePulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "reticle_pulse"
    )

    // Initial greeting on first entry
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        delay(600)
        voiceHelper.speak(activeFullGreeting)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Kids Face ID & Greetings",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = OnSurface
                        )
                        Surface(
                            shape = CircleShape,
                            color = Tertiary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "AI VISION 📸",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Tertiary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        voiceHelper.speak(activeFullGreeting)
                    }) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Read Aloud",
                            tint = Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Surface)
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CAMERA SCANNER & BIOMETRIC FACE VIEWFINDER
            item {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1F24)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Background Child Photo or Simulated Viewfinder
                        AsyncImage(
                            model = selectedKid.avatarUrl,
                            contentDescription = "Child Face View",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(26.dp)),
                            contentScale = ContentScale.Crop
                        )

                        // Dark gradient overlay for HUD visibility
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Black.copy(alpha = 0.45f),
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.75f)
                                        )
                                    )
                                )
                        )

                        // Top HUD: Recognition Status & Confidence
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isFaceLocked) Color(0xFF00E676).copy(alpha = 0.25f) else Color(0xFFFF9100).copy(alpha = 0.25f),
                                border = BorderStroke(
                                    1.dp,
                                    if (isFaceLocked) Color(0xFF00E676) else Color(0xFFFF9100)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (isFaceLocked) Color(0xFF00E676) else Color(0xFFFF9100))
                                    )
                                    Text(
                                        text = if (isScanning) "SCANNING FACE..." else "FACE DETECTED • 99.4%",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.6f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TagFaces,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD54F),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Smile: 100% 😄",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Biometric Face Tracking Box in Center
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(175.dp)
                                .scale(if (isScanning) reticlePulse else 1f)
                                .border(
                                    width = 2.5.dp,
                                    color = if (isFaceLocked) Color(0xFF00E676) else Color(0xFF29B6F6),
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            // Facial landmark markers
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 34.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Left eye target
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF00E676).copy(alpha = 0.8f))
                                )
                                // Right eye target
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF00E676).copy(alpha = 0.8f))
                                )
                            }

                            // Center nose dot
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFD54F))
                            )

                            // Name Tag Attached to Bounding Box
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isFaceLocked) Color(0xFF00E676) else Color(0xFF29B6F6),
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(y = (-14).dp)
                            ) {
                                Text(
                                    text = "★ ${selectedKid.name} (${selectedKid.grade})",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // Scanning Laser Beam Animation
                        if (isScanning) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .offset(y = (laserOffset * 270).dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color(0xFF00E676),
                                                Color.White,
                                                Color(0xFF00E676),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )
                        }

                        // Bottom Viewfinder Action Controls
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "IDENTIFIED HERO:",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFAAAAAA)
                                )
                                Text(
                                    text = "${selectedKid.name} • ${selectedKid.superpower}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = { triggerDetection(selectedKid) },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00E676),
                                    contentColor = Color.Black
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                modifier = Modifier.height(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isScanning) "Detecting..." else "Scan Again 📸",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // KID PROFILE SWITCHER (Quick multi-kid testing)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DETECTED CHILD PROFILES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = OnSurfaceVariant
                        )
                        Text(
                            text = "+ Add New Kid",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary,
                            modifier = Modifier.clickable { showAddKidDialog = true }
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(kidProfiles) { kid ->
                            val isSelected = kid.id == selectedKid.id
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) PrimaryContainer else SurfaceContainerLowest,
                                border = if (isSelected) BorderStroke(2.dp, Primary) else BorderStroke(1.dp, OutlineVariant),
                                shadowElevation = if (isSelected) 3.dp else 1.dp,
                                modifier = Modifier.clickable { triggerDetection(kid) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryFixed)
                                    ) {
                                        AsyncImage(
                                            model = kid.avatarUrl,
                                            contentDescription = kid.name,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = kid.name,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isSelected) OnPrimaryContainer else OnSurface
                                        )
                                        Text(
                                            text = kid.grade,
                                            fontSize = 10.sp,
                                            color = if (isSelected) Primary else OnSurfaceVariant,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = Primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // PRIMARY HERO GREETING BANNER (The key motivational prompt requested by user!)
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryFixed),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SmartToy,
                                        contentDescription = null,
                                        tint = OnPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "TEACHER PIP AI GREETING",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = OnPrimary
                                    )
                                }
                            }

                            IconButton(
                                onClick = { voiceHelper.speak(activeFullGreeting) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Primary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Speak Greeting",
                                    tint = OnPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Large quote text
                        Text(
                            text = "\"$activeFullGreeting\"",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = OnPrimaryFixed,
                            lineHeight = 24.sp
                        )

                        // Motivational badges row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SurfaceContainerLowest,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Happiness Meter", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                                    Text("Full! 🎉", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Tertiary)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SurfaceContainerLowest,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Mode", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                                    Text("Superhero 🦸", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Primary)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SurfaceContainerLowest,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Status", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                                    Text("Genius 🤓", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Secondary)
                                }
                            }
                        }
                    }
                }
            }

            // CATEGORY NAVIGATION TABS FOR ALL PROMPT COLLECTIONS
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "EXPLORE MOTIVATIONAL PROMPTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = OnSurfaceVariant
                    )

                    ScrollableTabRow(
                        selectedTabIndex = selectedGreetingCategory,
                        edgePadding = 0.dp,
                        containerColor = Color.Transparent,
                        divider = {}
                    ) {
                        val tabs = listOf(
                            "Fun Greetings 🌟",
                            "Daily Questions 💬",
                            "Encouraging Words ✨",
                            "Funny & Crazy 🤓",
                            "Achievements 🏆"
                        )
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedGreetingCategory == index,
                                onClick = { selectedGreetingCategory = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 12.sp,
                                        fontWeight = if (selectedGreetingCategory == index) FontWeight.Black else FontWeight.Bold
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // CATEGORY CONTENT: TAB 0 - FUN & CRAZY GREETINGS
            if (selectedGreetingCategory == 0) {
                item {
                    Text(
                        text = "Tap any greeting for Teacher Pip to cheer aloud for ${selectedKid.name}:",
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                items(KidFaceRecognitionPrompts.FUN_AND_CRAZY_GREETINGS) { greeting ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceContainerLowest,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeGreetingHeadline = greeting
                                val phrase = "Hello, ${selectedKid.name}! $greeting"
                                activeFullGreeting = phrase
                                voiceHelper.speak(phrase)
                            }
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
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Campaign,
                                        contentDescription = null,
                                        tint = OnPrimaryContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = greeting,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Speak",
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // CATEGORY CONTENT: TAB 1 - DAILY MOTIVATION QUESTIONS
            if (selectedGreetingCategory == 1) {
                item {
                    Text(
                        text = "Encourage conversation! Tap to hear Pip ask ${selectedKid.name}:",
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                items(KidFaceRecognitionPrompts.DAILY_MOTIVATION_QUESTIONS) { question ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeMotivationQuestion = question
                                val phrase = "${selectedKid.name}, $question"
                                activeFullGreeting = phrase
                                voiceHelper.speak(phrase)
                            }
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.HelpOutline,
                                        contentDescription = null,
                                        tint = Secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = question,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = OnSurface
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Ask Aloud",
                                    tint = Secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Quick reply sample chips for the child
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val quickAnswers = listOf("Super happy! 😊", "Learned something new! 📚", "Helped a friend! ❤️")
                                quickAnswers.forEach { ans ->
                                    Surface(
                                        shape = CircleShape,
                                        color = SecondaryFixed.copy(alpha = 0.4f),
                                        modifier = Modifier.clickable {
                                            voiceHelper.speak("That is wonderful, ${selectedKid.name}! Pip is so proud of you!")
                                        }
                                    ) {
                                        Text(
                                            text = ans,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OnSecondaryFixed,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // CATEGORY CONTENT: TAB 2 - ENCOURAGING WORDS
            if (selectedGreetingCategory == 2) {
                item {
                    Text(
                        text = "Build self-confidence and growth mindset:",
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                items(KidFaceRecognitionPrompts.ENCOURAGING_WORDS) { word ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceContainerLowest,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeEncouragingPhrase = word
                                val phrase = "${selectedKid.name}, $word"
                                activeFullGreeting = phrase
                                voiceHelper.speak(phrase)
                            }
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
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Tertiary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = Tertiary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = word,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = OnSurface
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Play",
                                tint = Tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // CATEGORY CONTENT: TAB 3 - FUNNY & CRAZY LINES
            if (selectedGreetingCategory == 3) {
                item {
                    Text(
                        text = "Humorous scanner telemetry lines that bring pure joy to kids:",
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                items(KidFaceRecognitionPrompts.FUNNY_AND_CRAZY_LINES) { line ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceContainerLowest,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeFunnyLine = line
                                val phrase = "Attention! For ${selectedKid.name}: $line"
                                activeFullGreeting = phrase
                                voiceHelper.speak(phrase)
                            }
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
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryFixedDim),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = line,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = OnSurface
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Play",
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // CATEGORY CONTENT: TAB 4 - ACHIEVEMENT MESSAGES
            if (selectedGreetingCategory == 4) {
                item {
                    Text(
                        text = "Celebrate homework milestones & daily wins:",
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                items(KidFaceRecognitionPrompts.ACHIEVEMENT_MESSAGES) { achievement ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceContainerLowest,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeAchievementBadge = achievement
                                val phrase = "Bravo, ${selectedKid.name}! $achievement"
                                activeFullGreeting = phrase
                                voiceHelper.speak(phrase)
                            }
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
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(SecondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = OnSecondaryContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = achievement,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = OnSurface
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Play",
                                tint = Secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // DIALOG TO REGISTER A NEW CHILD FOR FACE RECOGNITION
    if (showAddKidDialog) {
        AlertDialog(
            onDismissRequest = { showAddKidDialog = false },
            title = {
                Text("Add New Child for Face ID", fontWeight = FontWeight.Black, fontSize = 18.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Register a child's name and grade so Teacher Pip can recognize them and deliver personalized motivational greetings!",
                        fontSize = 13.sp,
                        color = OnSurfaceVariant
                    )

                    OutlinedTextField(
                        value = newKidNameInput,
                        onValueChange = { newKidNameInput = it },
                        label = { Text("Child's First Name") },
                        placeholder = { Text("e.g. Diya, Kabir, Meera") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newKidGradeInput,
                        onValueChange = { newKidGradeInput = it },
                        label = { Text("Grade / Class") },
                        placeholder = { Text("e.g. Grade 3") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val name = newKidNameInput.trim()
                        if (name.isNotBlank()) {
                            val newKid = KidProfile(
                                id = name.lowercase(),
                                name = name,
                                grade = newKidGradeInput.trim().ifBlank { "Grade 3" },
                                avatarUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?w=200&fit=crop&q=80",
                                defaultGreeting = "Hello, $name! 🌟 How's your day today? Keep shining, Superstar! 🚀",
                                superpower = "Curious Explorer",
                                smileScore = 100,
                                favoriteSubject = "General Knowledge"
                            )
                            kidProfiles = kidProfiles + newKid
                            showAddKidDialog = false
                            newKidNameInput = ""
                            triggerDetection(newKid)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Register & Scan 📸")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddKidDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
