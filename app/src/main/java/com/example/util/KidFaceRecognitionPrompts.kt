package com.example.util

data class KidProfile(
    val id: String,
    val name: String,
    val grade: String = "Grade 3",
    val avatarUrl: String,
    val defaultGreeting: String,
    val superpower: String = "Curiosity & Kindness",
    val smileScore: Int = 100,
    val favoriteSubject: String = "Mathematics"
)

data class FaceDetectionResult(
    val detectedKid: KidProfile,
    val confidencePercent: Float = 99.4f,
    val greetingHeadline: String,
    val motivationQuestion: String,
    val encouragingPhrase: String,
    val funnyLine: String,
    val achievementBadge: String,
    val fullSpokenPrompt: String
)

object KidFaceRecognitionPrompts {

    val FUN_AND_CRAZY_GREETINGS = listOf(
        "Hey Superstar! 🌟",
        "Wow! Look who's here! 😃",
        "Hello Champion! 🏆",
        "Hey Rockstar! 🎸",
        "Awesome Buddy! 🚀",
        "Hello Little Genius! 🧠",
        "Hey Adventure Hero! 🌈",
        "Super Kid Alert! 🎉",
        "Welcome, Future Leader! 👑",
        "Hi Speedy Rocket! 🚀"
    )

    val DAILY_MOTIVATION_QUESTIONS = listOf(
        "How's your day today? 😊",
        "What fun thing did you do today?",
        "Did you help anyone today? ❤️",
        "What made you smile today?",
        "What new thing did you learn today?",
        "Did you make a new friend today?",
        "What was the best part of your day?",
        "Did you try something brave today?",
        "What are you proud of today?",
        "What good habit did you practice today?"
    )

    val ENCOURAGING_WORDS = listOf(
        "Keep Shining! ✨",
        "You Are Amazing! 🌟",
        "Believe In Yourself! 💪",
        "Dream Big! 🚀",
        "Never Give Up! 🔥",
        "You Can Do It! 🎯",
        "Stay Curious! 🧠",
        "Keep Learning! 📚",
        "Be Kind, Be Awesome! ❤️",
        "Every Day Is A New Adventure! 🌈"
    )

    val FUNNY_AND_CRAZY_LINES = listOf(
        "Warning! Genius Detected! 🤓",
        "Smile Level: 100% 😄",
        "Happiness Meter: Full! 🎉",
        "Future Scientist Found! 🔬",
        "Superhero Mode Activated! 🦸",
        "Magic Power Loading... ⚡",
        "Fun Master Online! 🎮",
        "Energy Level: Maximum! 🚀",
        "Cool Kid Identified! 😎",
        "Laugh Challenge Started! 😂"
    )

    val NAME_BASED_EXAMPLES = mapOf(
        "Aarav" to "Hello, Aarav! 🌟 How's your day today? What new thing did you learn? Keep shining, Superstar! 🚀",
        "Rahul" to "Hello, Rahul! Ready for an adventure? 🚀",
        "Ananya" to "Hi, Ananya! What amazing thing did you learn today? 🌟",
        "Arjun" to "Welcome back, Arjun! Let's make today awesome! 😃",
        "Priya" to "Hey, Priya! Your smile brightens the day! ☀️",
        "Rohan" to "Hello, Champion Rohan! Ready to shine? ✨"
    )

    val ACHIEVEMENT_MESSAGES = listOf(
        "Great Job Today! 🏅",
        "You Completed Another Day Of Learning! 📚",
        "Fantastic Effort! ⭐",
        "Keep Up The Good Work! 🎯",
        "You're Becoming Smarter Every Day! 🧠",
        "Success Is Coming Your Way! 🚀",
        "Excellent Progress! 🌟",
        "Today's Hero: You! 🏆"
    )

    val PRESET_KIDS = listOf(
        KidProfile(
            id = "aarav",
            name = "Aarav",
            grade = "Grade 3",
            avatarUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?w=200&fit=crop&q=80",
            defaultGreeting = "Hello, Aarav! 🌟 How's your day today? What new thing did you learn? Keep shining, Superstar! 🚀",
            superpower = "Math Whiz & Explorer",
            smileScore = 100,
            favoriteSubject = "Science & Math"
        ),
        KidProfile(
            id = "ananya",
            name = "Ananya",
            grade = "Grade 4",
            avatarUrl = "https://images.unsplash.com/photo-1595454223600-91fb57d33306?w=200&fit=crop&q=80",
            defaultGreeting = "Hi, Ananya! What amazing thing did you learn today? 🌟",
            superpower = "Creative Storyteller",
            smileScore = 98,
            favoriteSubject = "English & Kannada"
        ),
        KidProfile(
            id = "rahul",
            name = "Rahul",
            grade = "Grade 2",
            avatarUrl = "https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?w=200&fit=crop&q=80",
            defaultGreeting = "Hello, Rahul! Ready for an adventure? 🚀",
            superpower = "Quick Problem Solver",
            smileScore = 100,
            favoriteSubject = "Robotics & Puzzles"
        ),
        KidProfile(
            id = "arjun",
            name = "Arjun",
            grade = "Grade 3",
            avatarUrl = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=200&fit=crop&q=80",
            defaultGreeting = "Welcome back, Arjun! Let's make today awesome! 😃",
            superpower = "Nature Detective",
            smileScore = 95,
            favoriteSubject = "Environmental Science"
        ),
        KidProfile(
            id = "priya",
            name = "Priya",
            grade = "Grade 3",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200&fit=crop&q=80",
            defaultGreeting = "Hey, Priya! Your smile brightens the day! ☀️",
            superpower = "Empathetic Leader",
            smileScore = 100,
            favoriteSubject = "Art & Kannada"
        ),
        KidProfile(
            id = "rohan",
            name = "Rohan",
            grade = "Grade 5",
            avatarUrl = "https://images.unsplash.com/photo-1485546246426-74dc88dec4d9?w=200&fit=crop&q=80",
            defaultGreeting = "Hello, Champion Rohan! Ready to shine? ✨",
            superpower = "Critical Thinker",
            smileScore = 99,
            favoriteSubject = "Geometry & Coding"
        )
    )

    fun createDetectionResult(
        kid: KidProfile,
        questionIndex: Int? = null,
        encouragementIndex: Int? = null
    ): FaceDetectionResult {
        val greeting = FUN_AND_CRAZY_GREETINGS.random()
        val question = if (questionIndex != null && questionIndex in DAILY_MOTIVATION_QUESTIONS.indices) {
            DAILY_MOTIVATION_QUESTIONS[questionIndex]
        } else {
            DAILY_MOTIVATION_QUESTIONS.random()
        }
        val encouragement = if (encouragementIndex != null && encouragementIndex in ENCOURAGING_WORDS.indices) {
            ENCOURAGING_WORDS[encouragementIndex]
        } else {
            ENCOURAGING_WORDS.random()
        }
        val funny = FUNNY_AND_CRAZY_LINES.random()
        val achievement = ACHIEVEMENT_MESSAGES.random()

        // Name-based personalized composite
        val spoken = if (kid.name == "Aarav") {
            "Hello, Aarav! How is your day today? What new thing did you learn? Keep shining, Superstar!"
        } else {
            "Hello, ${kid.name}! $greeting $question $encouragement"
        }

        return FaceDetectionResult(
            detectedKid = kid,
            confidencePercent = (98.5f + (Math.random() * 1.4f).toFloat()),
            greetingHeadline = greeting,
            motivationQuestion = question,
            encouragingPhrase = encouragement,
            funnyLine = funny,
            achievementBadge = achievement,
            fullSpokenPrompt = spoken
        )
    }
}
