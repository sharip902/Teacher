package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class VoiceHelper(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("VoiceHelper", "US English language not supported or missing data")
            } else {
                isInitialized = true
                tts?.setPitch(1.15f) // Warm, friendly child-teacher pitch
                tts?.setSpeechRate(0.95f) // Clear, slightly measured cadence
            }
        } else {
            Log.e("VoiceHelper", "TTS Initialization failed")
        }
    }

    fun speak(text: String, rate: Float = 0.95f) {
        if (isInitialized && tts != null) {
            tts?.setSpeechRate(rate)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "UTTERANCE_ID_${System.currentTimeMillis()}")
        }
    }

    fun setPace(rateMultiplier: Float) {
        if (isInitialized && tts != null) {
            tts?.setSpeechRate(0.95f * rateMultiplier)
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
