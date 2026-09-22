package com.example.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

data class SpeechRecognitionState(
    val isListening: Boolean = false,
    val isAvailable: Boolean = true,
    val rmsLevel: Float = 0f, // 0.0f to 1.0f normalized sound level
    val partialText: String = "",
    val recognizedText: String = "",
    val errorMessage: String? = null,
    val statusText: String = "Tap microphone to speak",
    val selectedLanguageTag: String = "en-IN",
    val selectedLanguageName: String = "English (India)"
)

class SpeechRecognitionHelper(private val context: Context) : RecognitionListener {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var speechRecognizer: SpeechRecognizer? = null

    private val _state = MutableStateFlow(
        SpeechRecognitionState(
            isAvailable = SpeechRecognizer.isRecognitionAvailable(context)
        )
    )
    val state: StateFlow<SpeechRecognitionState> = _state.asStateFlow()

    var onFinalResult: ((String) -> Unit)? = null

    init {
        initializeRecognizer()
    }

    private fun initializeRecognizer() {
        mainHandler.post {
            try {
                if (SpeechRecognizer.isRecognitionAvailable(context)) {
                    speechRecognizer?.destroy()
                    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                        setRecognitionListener(this@SpeechRecognitionHelper)
                    }
                    _state.value = _state.value.copy(
                        isAvailable = true,
                        errorMessage = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        isAvailable = false,
                        errorMessage = "Speech recognition is not available on this device",
                        statusText = "Voice recognition unavailable"
                    )
                }
            } catch (e: Exception) {
                Log.e("SpeechHelper", "Error creating speech recognizer", e)
                _state.value = _state.value.copy(
                    isAvailable = false,
                    errorMessage = e.localizedMessage
                )
            }
        }
    }

    fun setLanguage(languageTag: String, languageDisplayName: String) {
        _state.value = _state.value.copy(
            selectedLanguageTag = languageTag,
            selectedLanguageName = languageDisplayName
        )
    }

    fun startListening(languageTag: String = _state.value.selectedLanguageTag) {
        mainHandler.post {
            try {
                if (speechRecognizer == null) {
                    initializeRecognizer()
                }

                _state.value = _state.value.copy(
                    isListening = true,
                    partialText = "",
                    errorMessage = null,
                    statusText = "Listening to you... Speak now!",
                    rmsLevel = 0f
                )

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageTag)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageTag)
                    putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false)
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                    putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
                    // Generous speech timeouts so children have time to formulate thoughts
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000L)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 2500L)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1500L)
                }

                speechRecognizer?.startListening(intent)
            } catch (e: Exception) {
                Log.e("SpeechHelper", "Error starting listening", e)
                _state.value = _state.value.copy(
                    isListening = false,
                    errorMessage = "Could not start microphone: ${e.localizedMessage}",
                    statusText = "Tap microphone to try again"
                )
            }
        }
    }

    fun stopListening() {
        mainHandler.post {
            try {
                speechRecognizer?.stopListening()
                _state.value = _state.value.copy(
                    statusText = "Processing your question..."
                )
            } catch (e: Exception) {
                Log.e("SpeechHelper", "Error stopping listening", e)
            }
        }
    }

    fun cancel() {
        mainHandler.post {
            try {
                speechRecognizer?.cancel()
                _state.value = _state.value.copy(
                    isListening = false,
                    rmsLevel = 0f,
                    statusText = "Tap microphone to speak"
                )
            } catch (e: Exception) {
                Log.e("SpeechHelper", "Error cancelling", e)
            }
        }
    }

    fun destroy() {
        mainHandler.post {
            try {
                speechRecognizer?.destroy()
                speechRecognizer = null
            } catch (e: Exception) {
                Log.e("SpeechHelper", "Error destroying speech recognizer", e)
            }
        }
    }

    // RecognitionListener Implementations

    override fun onReadyForSpeech(params: Bundle?) {
        _state.value = _state.value.copy(
            isListening = true,
            statusText = "Teacher Pip is listening! Speak clearly 🌟",
            errorMessage = null
        )
    }

    override fun onBeginningOfSpeech() {
        _state.value = _state.value.copy(
            statusText = "Hearing your voice...",
            isListening = true
        )
    }

    override fun onRmsChanged(rmsdB: Float) {
        // rmsdB typically ranges from -2.0 to 10.0 dB
        // Normalize to a pleasant 0.0f .. 1.0f curve for Compose animations
        val normalized = ((rmsdB + 2.0f) / 12.0f).coerceIn(0.05f, 1.0f)
        _state.value = _state.value.copy(
            rmsLevel = normalized
        )
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        // Audio stream buffer if needed
    }

    override fun onEndOfSpeech() {
        _state.value = _state.value.copy(
            isListening = false,
            rmsLevel = 0f,
            statusText = "Teacher Pip is thinking about your question..."
        )
    }

    override fun onError(errorCode: Int) {
        val message = when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording issue. Please check microphone."
            SpeechRecognizer.ERROR_CLIENT -> "Speech recognition client error."
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required."
            SpeechRecognizer.ERROR_NETWORK -> "Network issue while recognizing speech."
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network connection timed out."
            SpeechRecognizer.ERROR_NO_MATCH -> "Pip didn't catch that clearly. Let's try again!"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Microphone was busy, ready now."
            SpeechRecognizer.ERROR_SERVER -> "Voice server response error."
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech heard. Tap mic when you're ready!"
            else -> "Speech recognition paused."
        }

        Log.w("SpeechHelper", "SpeechRecognizer error: $errorCode -> $message")

        _state.value = _state.value.copy(
            isListening = false,
            rmsLevel = 0f,
            errorMessage = if (errorCode == SpeechRecognizer.ERROR_NO_MATCH || errorCode == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) null else message,
            statusText = if (errorCode == SpeechRecognizer.ERROR_NO_MATCH) "Didn't hear you clearly. Tap mic to try again!" else "Tap microphone to speak"
        )
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val text = matches?.firstOrNull()?.trim()

        if (!text.isNullOrBlank()) {
            _state.value = _state.value.copy(
                isListening = false,
                rmsLevel = 0f,
                recognizedText = text,
                partialText = text,
                statusText = "You asked: \"$text\""
            )
            onFinalResult?.invoke(text)
        } else {
            _state.value = _state.value.copy(
                isListening = false,
                rmsLevel = 0f,
                statusText = "Didn't catch that. Tap mic to ask again!"
            )
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val partial = matches?.firstOrNull()?.trim()
        if (!partial.isNullOrBlank()) {
            _state.value = _state.value.copy(
                partialText = partial,
                statusText = "Hearing: \"$partial\"..."
            )
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        // Custom events if any
    }
}
