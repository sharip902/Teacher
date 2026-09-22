package com.example.util

data class TeacherResponse(
    val spokenText: String,
    val displayText: String,
    val bilingualVocab: BilingualVocabulary? = null,
    val suggestedFollowUps: List<String> = emptyList()
)

data class BilingualVocabulary(
    val english: String,
    val kannadaText: String,
    val transliteration: String,
    val meaning: String
)

object PipAiTeacherBrain {

    fun generateAnswer(userQuestion: String): TeacherResponse {
        val q = userQuestion.lowercase().trim()

        return when {
            // MATH: MULTIPLICATION
            q.contains("times") || q.contains("multiply") || q.contains("×") || Regex("\\b[x*]\\b").containsMatchIn(q) || Regex("\\d+\\s*[x*]\\s*\\d+").containsMatchIn(q) -> {
                handleMathMultiplication(q)
            }

            // MATH: ADDITION
            q.contains("plus") || q.contains("add") || Regex("\\b[+]\\b").containsMatchIn(q) || Regex("\\d+\\s*[+]\\s*\\d+").containsMatchIn(q) -> {
                handleMathAddition(q)
            }

            // MATH: GENERAL
            q.contains("math") || q.contains("number") || q.contains("fraction") || q.contains("subtraction") || q.contains("minus") -> {
                TeacherResponse(
                    spokenText = "Math is like a treasure hunt with numbers! What equation or problem are you working on today? Let us break it down step-by-step together!",
                    displayText = "Math is like a treasure hunt with numbers! 📐 What problem are you working on? Tell me any numbers and we will solve it step-by-step!",
                    bilingualVocab = BilingualVocabulary(
                        english = "Mathematics",
                        kannadaText = "ಗಣಿತ",
                        transliteration = "Ganitha",
                        meaning = "The study of numbers and shapes"
                    ),
                    suggestedFollowUps = listOf("What is 15 + 27?", "What is 8 × 7?", "Teach me fractions")
                )
            }

            // KANNADA LANGUAGE QUERIES
            q.contains("kannada") || q.contains("butterfly") || q.contains("chitte") || q.contains("namaskara") || q.contains("how do you say") -> {
                when {
                    q.contains("butterfly") -> TeacherResponse(
                        spokenText = "In Kannada, a butterfly is called Chitte! Repeat after me: Chit-tey. Beautiful, colorful wings!",
                        displayText = "In Kannada, a butterfly is called **Chitte (ಚಿಟ್ಟೆ)**! 🦋\n\nPhonetic sound: *Chit-tey*\nLet's flutter our wings together!",
                        bilingualVocab = BilingualVocabulary(
                            english = "Butterfly",
                            kannadaText = "ಚಿಟ್ಟೆ",
                            transliteration = "Chitte",
                            meaning = "A colorful winged insect"
                        ),
                        suggestedFollowUps = listOf("What is flower in Kannada?", "How do you say water?", "Tell me a Kannada number")
                    )
                    q.contains("thank") -> TeacherResponse(
                        spokenText = "In Kannada, thank you is Dhanyavadagalu! Or simply Dhanyavada. It is a polite way to appreciate someone!",
                        displayText = "Thank you in Kannada is **Dhanyavadagalu (ಧನ್ಯವಾದಗಳು)**! 🙏\n\nPronounced: *Dhahn-ya-vaa-dha-ga-loo*",
                        bilingualVocab = BilingualVocabulary(
                            english = "Thank you",
                            kannadaText = "ಧನ್ಯವಾದಗಳು",
                            transliteration = "Dhanyavadagalu",
                            meaning = "Expressing gratitude"
                        ),
                        suggestedFollowUps = listOf("How to say Hello in Kannada?", "How to say Good Morning?", "How to say Friend?")
                    )
                    q.contains("hello") || q.contains("hi") || q.contains("namaste") -> TeacherResponse(
                        spokenText = "Namaskara! In Kannada, we greet friends with Namaskara! It means I respect the light within you.",
                        displayText = "Hello in Kannada is **Namaskara (ನಮಸ್ಕಾರ)**! 🙏\n\nSay it with a warm smile: *Nah-mas-kaa-rah*!",
                        bilingualVocab = BilingualVocabulary(
                            english = "Hello / Greetings",
                            kannadaText = "ನಮಸ್ಕಾರ",
                            transliteration = "Namaskara",
                            meaning = "Traditional greeting with folded hands"
                        ),
                        suggestedFollowUps = listOf("How do you say friend?", "Teach me a Kannada song", "How to say Teacher?")
                    )
                    q.contains("water") -> TeacherResponse(
                        spokenText = "Water in Kannada is Neeru! Staying hydrated helps your brilliant brain focus on homework!",
                        displayText = "Water in Kannada is **Neeru (ನೀರು)**! 💧\n\nPronounced: *Nee-roo*",
                        bilingualVocab = BilingualVocabulary(
                            english = "Water",
                            kannadaText = "ನೀರು",
                            transliteration = "Neeru",
                            meaning = "Essential liquid for all living things"
                        ),
                        suggestedFollowUps = listOf("What is food in Kannada?", "What is rain in Kannada?")
                    )
                    else -> TeacherResponse(
                        spokenText = "I love Kannada! Kannada has 49 beautiful letters called Aksharamale. What word would you like to translate today?",
                        displayText = "Kannada (ಕನ್ನಡ) is a rich, melodious classical language! 🌺 What word or phrase would you like to learn today?",
                        bilingualVocab = BilingualVocabulary(
                            english = "Language",
                            kannadaText = "ಭಾಷೆ",
                            transliteration = "Bhashe",
                            meaning = "A system of communication"
                        ),
                        suggestedFollowUps = listOf("What is Rainbow in Kannada?", "How to say School in Kannada?", "Teach me Kannada numbers 1 to 5")
                    )
                }
            }

            // SCIENCE: PHOTOSYNTHESIS & PLANTS
            q.contains("photosynthesis") || q.contains("plant") || q.contains("leaf") || q.contains("leaves") -> {
                TeacherResponse(
                    spokenText = "Plants are nature's amazing little chefs! In photosynthesis, green leaves use sunlight, water, and air to cook tasty sugar food, and they give us clean oxygen to breathe! Isn't that miraculous?",
                    displayText = "🌿 **How Plants Cook Their Food (Photosynthesis)**:\n\n1. ☀️ **Sunlight** provides warm solar energy.\n2. 💧 **Roots** drink water from the soil.\n3. 🌬️ **Leaves** breathe in Carbon Dioxide from the air.\n\n**Result:** Delicious plant energy + fresh clean Oxygen for you and me! 🌬️",
                    bilingualVocab = BilingualVocabulary(
                        english = "Tree / Plant",
                        kannadaText = "ಮರ / ಗಿಡ",
                        transliteration = "Mara / Gida",
                        meaning = "Living plant life"
                    ),
                    suggestedFollowUps = listOf("Why are leaves green?", "What is chlorophyll?", "How do seeds sprout?")
                )
            }

            // SCIENCE: SKY & RAINBOW
            q.contains("sky") && q.contains("blue") -> {
                TeacherResponse(
                    spokenText = "Sunlight looks white, but it is actually a rainbow of colors! When sunlight hits Earth's atmosphere, the tiny air molecules scatter the short blue light waves in all directions, painting our sky blue!",
                    displayText = "🌤️ **Why is the Sky Blue?**\n\nSunlight contains all colors of the rainbow! Blue light travels in short, smaller waves and scatters in all directions when it hits tiny gas particles in our atmosphere. This is called **Rayleigh Scattering**!",
                    suggestedFollowUps = listOf("Why are sunsets red?", "How is a rainbow made?", "What are clouds made of?")
                )
            }

            q.contains("rainbow") -> {
                TeacherResponse(
                    spokenText = "A rainbow forms when sunlight shines through raindrops in the air! Each raindrop acts like a tiny glass prism, bending the light into Violet, Indigo, Blue, Green, Yellow, Orange, and Red!",
                    displayText = "🌈 **The Magic of Rainbows (VIBGYOR)**:\n\n• **V** - Violet\n• **I** - Indigo\n• **B** - Blue\n• **G** - Green\n• **Y** - Yellow\n• **O** - Orange\n• **R** - Red\n\nIn Kannada, a rainbow is called **Kaamana Billu (ಕಾಮನಬಿಲ್ಲು)**!",
                    bilingualVocab = BilingualVocabulary(
                        english = "Rainbow",
                        kannadaText = "ಕಾಮನಬಿಲ್ಲು",
                        transliteration = "Kaamana Billu",
                        meaning = "Arch of spectral colors in the sky"
                    ),
                    suggestedFollowUps = listOf("Why does it rain?", "What is lightning?", "Can you touch a rainbow?")
                )
            }

            // SCIENCE: SPACE, PLANETS, SOLAR SYSTEM
            q.contains("planet") || q.contains("solar system") || q.contains("space") || q.contains("jupiter") || q.contains("moon") || q.contains("sun") -> {
                TeacherResponse(
                    spokenText = "Our solar system has eight planets revolving around our fiery sun! Jupiter is the biggest giant planet, and Earth is our cozy blue home where life flourishes. Would you like to name the planets together?",
                    displayText = "🪐 **Our Cosmic Neighborhood**:\n\n1. 🪨 Mercury (closest to Sun)\n2. 🟡 Venus (hottest planet)\n3. 🌍 Earth (our green & blue home)\n4. 🔴 Mars (the red planet)\n5. 👑 Jupiter (biggest giant with 95 moons!)\n6. 🪐 Saturn (spectacular ice rings)\n7. ❄️ Uranus (tilted ice giant)\n8. 🌊 Neptune (windy deep blue planet)",
                    suggestedFollowUps = listOf("How far is the Moon?", "Why does Saturn have rings?", "What is a shooting star?")
                )
            }

            // SPELLING & PHONICS
            q.contains("spell") || q.contains("spelling") -> {
                val targetWord = extractWordAfter(q, "spell") ?: "elephant"
                val spelledOut = targetWord.uppercase().map { "$it" }.joinToString("-")
                TeacherResponse(
                    spokenText = "Let us spell $targetWord together! $spelledOut! Let us sound out each syllable carefully!",
                    displayText = "🔤 **Spelling Challenge: $targetWord**\n\nLetters: **$spelledOut**\n\nPractice tip: Break it into rhythmic syllables and write it twice on your paper!",
                    suggestedFollowUps = listOf("Spell 'knowledge'", "Spell 'beautiful'", "Spell 'friend'")
                )
            }

            // JOKES & HUMOR
            q.contains("joke") || q.contains("funny") -> {
                val jokes = listOf(
                    "Why was the math book sad? Because it had too many problems! But do not worry, Pip is here to help you solve them!",
                    "Why do birds fly south in the winter? Because it is much too far to walk!",
                    "What did the zero say to the eight? Nice belt!",
                    "Why did the teacher wear sunglasses to school? Because her students were so bright!"
                )
                val joke = jokes.random()
                TeacherResponse(
                    spokenText = joke,
                    displayText = "😄 **Teacher Pip's Chuckle Corner**:\n\n$joke 🎉",
                    suggestedFollowUps = listOf("Tell me another joke!", "Tell me a riddle!", "Why was six afraid of seven?")
                )
            }

            // STORIES
            q.contains("story") || q.contains("tale") -> {
                TeacherResponse(
                    spokenText = "Once upon a time in a whispering green forest, a tiny clever squirrel named Kavi wanted to build a bridge across a silver brook. While the bigger animals laughed, Kavi tied strong vine ropes step-by-step. By sundown, every creature could cross safely! The moral: small, patient steps achieve mighty wonders!",
                    displayText = "📖 **The Tale of Kavi the Clever Squirrel**\n\nDeep in the Western Ghats, little Kavi built a bridge across a rushing brook not with giant rocks, but with patient, persistent knotting of forest vines.\n\n🌟 **Lesson**: No task is too big when broken into small, cheerful steps!",
                    suggestedFollowUps = listOf("Tell me an animal fable", "Tell me a space adventure", "Tell me a bedtime story")
                )
            }

            // WHO ARE YOU / IDENTITY
            q.contains("who are you") || q.contains("your name") || q.contains("what are you") -> {
                TeacherResponse(
                    spokenText = "I am Teacher Pip, your friendly AI homework mentor! I am here to help you with math, science, reading, spelling, and Kannada, one happy step at a time!",
                    displayText = "🦉 **Hello! I am Teacher Pip AI**\n\n• Your 24/7 patient homework companion\n• Socratic guide for Grade 1 to Grade 5\n• Fluent in English & Kannada (ಕನ್ನಡ)\n• Always safe, encouraging, and ad-free!",
                    suggestedFollowUps = listOf("How can you help with homework?", "What grade are you best for?", "Teach me something fun")
                )
            }

            // GENERAL DEFAULT SOCRATIC ENCOURAGEMENT
            else -> {
                TeacherResponse(
                    spokenText = "That is a wonderful question! You have such a curious scientific mind. When we explore '$userQuestion', what do you think is the very first clue we should look for?",
                    displayText = "✨ **Great Question**: \"$userQuestion\"\n\nTeacher Pip loves your curiosity! Let's explore this together:\n\n• Think about what you already know about this topic.\n• Can you tell me which chapter or subject this comes from in your textbook?\n\nTap the microphone or a suggestion below to dive deeper!",
                    suggestedFollowUps = listOf("Explain it simply", "Give me an example", "How do I write this in my notebook?")
                )
            }
        }
    }

    private fun handleMathMultiplication(q: String): TeacherResponse {
        val numbers = Regex("\\d+").findAll(q).map { it.value.toInt() }.toList()
        if (numbers.size >= 2) {
            val a = numbers[0]
            val b = numbers[1]
            val prod = a * b
            return TeacherResponse(
                spokenText = "$a times $b equals $prod! You can think of it as $a groups of $b! Fantastic math work!",
                displayText = "🔢 **Multiplication Solution**:\n\n$$\\mathbf{$a \\times $b = $prod}$$\n\n💡 **Thinking Strategy**: Think of it as **$a** equal groups of **$b**!\n$$" + (1..a.coerceAtMost(5)).joinToString(" + ") { "$b" } + (if (a > 5) " + ... = $prod" else " = $prod") + "$$",
                suggestedFollowUps = listOf("What is $prod divided by $a?", "What is ${a + 1} × $b?", "Test me on multiplication!")
            )
        }
        return TeacherResponse(
            spokenText = "Multiplication is repeated addition! For example, 4 times 3 is just 3 plus 3 plus 3 plus 3, which equals 12! Give me two numbers to multiply!",
            displayText = "📐 **Multiplication Strategy**:\n\nMultiplication means adding groups of equal sizes!\n\nFor example:\n$4 \\times 3 = 3 + 3 + 3 + 3 = 12$\n\nSay two numbers like: *\"What is 7 times 8?\"*",
            suggestedFollowUps = listOf("What is 6 × 9?", "What is 12 × 12?", "What is 7 × 8?")
        )
    }

    private fun handleMathAddition(q: String): TeacherResponse {
        val numbers = Regex("\\d+").findAll(q).map { it.value.toInt() }.toList()
        if (numbers.size >= 2) {
            val a = numbers[0]
            val b = numbers[1]
            val sum = a + b
            return TeacherResponse(
                spokenText = "$a plus $b is equal to $sum! Excellent calculation!",
                displayText = "➕ **Addition Solution**:\n\n$$\\mathbf{$a + $b = $sum}$$\n\n🌟 **Step-by-step Tip**:\nSplit into tens and ones:\n• Tens: ${(a / 10) * 10} + ${(b / 10) * 10} = ${((a / 10) + (b / 10)) * 10}\n• Ones: ${a % 10} + ${b % 10} = ${(a % 10) + (b % 10)}\nTotal = **$sum**!",
                suggestedFollowUps = listOf("What is $sum - $b?", "Add 10 more to $sum", "Solve another addition")
            )
        }
        return TeacherResponse(
            spokenText = "Addition puts numbers together into a bigger total! What numbers would you like to add today?",
            displayText = "➕ **Addition Helper**:\n\nTell me: *\"What is 24 plus 18?\"* and I will guide you through carrying over the tens!",
            suggestedFollowUps = listOf("What is 45 + 35?", "What is 99 + 1?", "What is 120 + 80?")
        )
    }

    private fun extractWordAfter(text: String, keyword: String): String? {
        val idx = text.indexOf(keyword)
        if (idx != -1) {
            val after = text.substring(idx + keyword.length).trim()
            val clean = after.replace("the word", "")
                .replace("how do you", "")
                .replace("how to", "")
                .replace("please", "")
                .replace("?", "")
                .replace("\"", "")
                .trim()
            return clean.split(" ").firstOrNull { it.isNotBlank() }
        }
        return null
    }
}
