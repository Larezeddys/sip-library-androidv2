@@ .. @@
     // Timer de duración de llamada
     private var callDurationTimer: Job? = null
     private val _callDuration = MutableStateFlow(0L)
     val callDuration: StateFlow<Long> = _callDuration.asStateFlow()
+    
+    // Estados de traducción
+    private val _isTranslationEnabled = MutableStateFlow(false)
+    val isTranslationEnabled: StateFlow<Boolean> = _isTranslationEnabled.asStateFlow()
+    
+    private val _translationInfo = MutableStateFlow<CallTranslationInfo?>(null)
+    val translationInfo: StateFlow<CallTranslationInfo?> = _translationInfo.asStateFlow()

     private fun startCallDurationTimer() {
@@ .. @@
     fun clearCallStateHistory() {
         sipLibrary.clearCallStateHistory()
     }
+    
+    // === MÉTODOS DE TRADUCCIÓN ===
+    
+    /**
+     * Habilitar/deshabilitar traducción
+     */
+    fun setTranslationEnabled(enabled: Boolean) {
+        viewModelScope.launch {
+            try {
+                sipLibrary.setTranslationEnabled(enabled)
+                _isTranslationEnabled.value = enabled
+                
+                if (enabled && callState.value.state.isCallActive()) {
+                    // Si hay una llamada activa, iniciar traducción
+                    val success = sipLibrary.startTranslationForCurrentCall()
+                    if (success) {
+                        _translationInfo.value = sipLibrary.getCurrentTranslationInfo()
+                        _uiState.update { 
+                            it.copy(callMessage = "Traducción activada") 
+                        }
+                    }
+                } else if (!enabled) {
+                    // Detener traducción
+                    sipLibrary.stopTranslationForCurrentCall()
+                    _translationInfo.value = null
+                    _uiState.update { 
+                        it.copy(callMessage = "Traducción desactivada") 
+                    }
+                }
+                
+            } catch (e: Exception) {
+                _uiState.update { 
+                    it.copy(callMessage = "Error en traducción: ${e.message}") 
+                }
+            }
+        }
+    }
+    
+    /**
+     * Configurar idioma de traducción
+     */
+    fun setTranslationLanguage(language: String) {
+        viewModelScope.launch {
+            try {
+                sipLibrary.setTranslationLanguage(language)
+                _uiState.update { 
+                    it.copy(callMessage = "Idioma de traducción: $language") 
+                }
+            } catch (e: Exception) {
+                _uiState.update { 
+                    it.copy(callMessage = "Error configurando idioma: ${e.message}") 
+                }
+            }
+        }
+    }
+    
+    /**
+     * Verificar si la traducción está disponible
+     */
+    fun isTranslationAvailable(): Boolean {
+        return sipLibrary.isTranslationAvailable()
+    }
+    
+    /**
+     * Obtener diagnóstico de traducción
+     */
+    fun getTranslationDiagnostic(): String {
+        return sipLibrary.getTranslationDiagnostic()
+    }

     fun getSystemDiagnostic(): String {
@@ .. @@
             appendLine("=== SYSTEM DIAGNOSTIC ===")
             appendLine(sipLibrary.diagnoseListeners())
             appendLine("\n=== CALL STATE HISTORY ===")
             getCallStateHistory().takeLast(10).forEach { state ->
                 appendLine("${state.timestamp}: ${state.previousState} -> ${state.state}")
                 if (state.hasError()) {
                     appendLine("  Error: ${state.errorReason} (${state.sipCode})")
                 }
             }
+            appendLine("\n=== TRANSLATION DIAGNOSTIC ===")
+            appendLine(getTranslationDiagnostic())
         }
     }