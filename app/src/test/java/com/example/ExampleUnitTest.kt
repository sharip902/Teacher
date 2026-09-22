package com.example

import com.example.util.PipAiTeacherBrain
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun pipAiTeacherBrain_handlesMultiplicationVerbally() {
    val response = PipAiTeacherBrain.generateAnswer("What is 8 times 7?")
    assertTrue(response.spokenText.contains("56"))
    assertTrue(response.displayText.contains("8 \\times 7 = 56"))
  }

  @Test
  fun pipAiTeacherBrain_handlesAdditionVerbally() {
    val response = PipAiTeacherBrain.generateAnswer("What is 24 plus 18?")
    assertTrue(response.spokenText.contains("42"))
    assertTrue(response.displayText.contains("42"))
  }

  @Test
  fun pipAiTeacherBrain_handlesKannadaButterflyVerbally() {
    val response = PipAiTeacherBrain.generateAnswer("How do I say butterfly in Kannada?")
    assertTrue(response.spokenText.contains("Chitte", ignoreCase = true))
    assertNotNull(response.bilingualVocab)
    assertEquals("ಚಿಟ್ಟೆ", response.bilingualVocab?.kannadaText)
  }

  @Test
  fun pipAiTeacherBrain_handlesSciencePhotosynthesisVerbally() {
    val response = PipAiTeacherBrain.generateAnswer("Explain photosynthesis")
    assertTrue(response.displayText.contains("Sunlight"))
    assertTrue(response.displayText.contains("Oxygen"))
  }

  @Test
  fun kidFaceRecognitionPrompts_generatesPersonalizedGreeting() {
    val kid = com.example.util.KidFaceRecognitionPrompts.PRESET_KIDS.first()
    val result = com.example.util.KidFaceRecognitionPrompts.createDetectionResult(kid)
    assertEquals(kid.id, result.detectedKid.id)
    assertTrue(result.fullSpokenPrompt.contains(kid.name))
    assertTrue(result.greetingHeadline.isNotBlank())
    assertTrue(result.motivationQuestion.isNotBlank())
    assertTrue(result.encouragingPhrase.isNotBlank())
    assertTrue(result.funnyLine.isNotBlank())
    assertTrue(result.confidencePercent > 90f)
  }
}

