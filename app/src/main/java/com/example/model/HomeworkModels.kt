package com.example.model

data class ChildProfile(
    val name: String = "Aarav",
    val fullName: String = "Aarav Patel",
    val grade: String = "Grade 3",
    val age: Int = 8,
    val school: String = "St. Xavier's Academy",
    val city: String = "Bangalore",
    val currentStreakDays: Int = 4,
    val starsCount: Int = 125,
    val dailyGoalMinutes: Int = 60,
    val completedMinutes: Int = 35
)

data class MissionItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val isCompleted: Boolean = false,
    val inProgress: Boolean = false,
    val starsAwarded: Int = 25,
    val minutes: Int = 10
)

data class SubjectItem(
    val id: String,
    val name: String,
    val localizedName: String,
    val description: String,
    val statusLabel: String,
    val iconName: String,
    val colorCategory: String // "amber", "blue", "green", "purple"
)

data class HomeworkAssignment(
    val id: String,
    val title: String,
    val status: String,
    val dueDate: String,
    val progressSteps: String,
    val isCompleted: Boolean = false
)

data class VoiceCommandPrompt(
    val promptText: String,
    val responseText: String,
    val kannadaText: String? = null,
    val hindiText: String? = null,
    val icon: String = "chat"
)

data class GrammarFixItem(
    val originalWord: String,
    val correctedWord: String,
    val explanation: String,
    val ruleType: String,
    val isFixed: Boolean = false
)
