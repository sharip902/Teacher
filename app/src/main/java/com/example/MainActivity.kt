package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.components.BottomNavBar
import com.example.ui.components.NavDestination
import com.example.ui.components.TopHeaderBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.util.SpeechRecognitionHelper
import com.example.util.VoiceHelper

class MainActivity : ComponentActivity() {

    private lateinit var voiceHelper: VoiceHelper
    private lateinit var speechHelper: SpeechRecognitionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        voiceHelper = VoiceHelper(this)
        speechHelper = SpeechRecognitionHelper(this)

        setContent {
            MyApplicationTheme {
                HomeworkTeacherApp(
                    voiceHelper = voiceHelper,
                    speechHelper = speechHelper
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceHelper.shutdown()
        speechHelper.destroy()
    }
}

enum class ActiveScreen {
    HOME,
    TASKS,
    SCAN_AND_LEARN,
    GAMES,
    ASK_AI,
    PARENT_PORTAL,
    FACE_ID
}

@Composable
fun HomeworkTeacherApp(
    voiceHelper: VoiceHelper,
    speechHelper: SpeechRecognitionHelper
) {
    var activeScreen by remember { mutableStateOf(ActiveScreen.HOME) }
    var selectedLanguage by remember { mutableStateOf("ENG • ಕನ್ನಡ") }
    var currentStreak by remember { mutableIntStateOf(4) }
    var currentStars by remember { mutableIntStateOf(125) }

    val navDestination = when (activeScreen) {
        ActiveScreen.HOME -> NavDestination.HOME
        ActiveScreen.TASKS -> NavDestination.TASKS
        ActiveScreen.ASK_AI -> NavDestination.ASK_AI
        ActiveScreen.GAMES -> NavDestination.GAMES
        ActiveScreen.PARENT_PORTAL -> NavDestination.PROGRESS
        ActiveScreen.SCAN_AND_LEARN -> NavDestination.TASKS
        ActiveScreen.FACE_ID -> NavDestination.HOME
    }

    val screenTitle = when (activeScreen) {
        ActiveScreen.HOME -> "Home"
        ActiveScreen.TASKS -> "Tasks"
        ActiveScreen.SCAN_AND_LEARN -> "Scan and Learn"
        ActiveScreen.GAMES -> "Play & Practice"
        ActiveScreen.ASK_AI -> "Ask AI"
        ActiveScreen.PARENT_PORTAL -> "Parent Portal"
        ActiveScreen.FACE_ID -> "Kids Face ID & Greetings"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopHeaderBar(
                currentScreenTitle = screenTitle,
                streakDays = currentStreak,
                starsCount = currentStars,
                selectedLang = selectedLanguage,
                onToggleLang = {
                    selectedLanguage = when (selectedLanguage) {
                        "ENG • ಕನ್ನಡ" -> "ENG • हिंदी"
                        "ENG • हिंदी" -> "ENG Only"
                        else -> "ENG • ಕನ್ನಡ"
                    }
                    voiceHelper.speak("Language changed to $selectedLanguage")
                },
                onParentClick = {
                    activeScreen = ActiveScreen.PARENT_PORTAL
                },
                onProfileClick = {
                    activeScreen = ActiveScreen.FACE_ID
                },
                onBackClick = if (activeScreen != ActiveScreen.HOME) {
                    { activeScreen = ActiveScreen.HOME }
                } else null
            )
        },
        bottomBar = {
            BottomNavBar(
                currentDestination = navDestination,
                onNavigate = { destination ->
                    activeScreen = when (destination) {
                        NavDestination.HOME -> ActiveScreen.HOME
                        NavDestination.TASKS -> ActiveScreen.TASKS
                        NavDestination.ASK_AI -> ActiveScreen.ASK_AI
                        NavDestination.GAMES -> ActiveScreen.GAMES
                        NavDestination.PROGRESS -> ActiveScreen.PARENT_PORTAL
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeScreen) {
                ActiveScreen.HOME -> {
                    HomeScreen(
                        voiceHelper = voiceHelper,
                        onOpenSolver = { activeScreen = ActiveScreen.SCAN_AND_LEARN },
                        onOpenReading = { activeScreen = ActiveScreen.GAMES },
                        onOpenSpelling = { activeScreen = ActiveScreen.GAMES },
                        onOpenVoiceAssistant = { activeScreen = ActiveScreen.ASK_AI },
                        onOpenParentPortal = { activeScreen = ActiveScreen.PARENT_PORTAL },
                        onOpenFaceRecognition = { activeScreen = ActiveScreen.FACE_ID }
                    )
                }
                ActiveScreen.TASKS -> {
                    TasksScreen(
                        voiceHelper = voiceHelper,
                        onOpenSolver = { activeScreen = ActiveScreen.SCAN_AND_LEARN }
                    )
                }
                ActiveScreen.SCAN_AND_LEARN -> {
                    ScanAndLearnScreen(
                        voiceHelper = voiceHelper,
                        onBackClick = { activeScreen = ActiveScreen.HOME },
                        onOpenVoiceAssistant = { activeScreen = ActiveScreen.ASK_AI },
                        onOpenFaceRecognition = { activeScreen = ActiveScreen.FACE_ID }
                    )
                }
                ActiveScreen.GAMES -> {
                    PlayAndPracticeScreen(
                        voiceHelper = voiceHelper,
                        initialTab = 1
                    )
                }
                ActiveScreen.ASK_AI -> {
                    VoiceAssistantScreen(
                        voiceHelper = voiceHelper,
                        speechHelper = speechHelper,
                        onBackClick = { activeScreen = ActiveScreen.HOME }
                    )
                }
                ActiveScreen.PARENT_PORTAL -> {
                    ParentPortalScreen(
                        voiceHelper = voiceHelper,
                        onBackClick = { activeScreen = ActiveScreen.HOME }
                    )
                }
                ActiveScreen.FACE_ID -> {
                    KidFaceRecognitionScreen(
                        voiceHelper = voiceHelper,
                        onBackClick = { activeScreen = ActiveScreen.HOME }
                    )
                }
            }
        }
    }
}
