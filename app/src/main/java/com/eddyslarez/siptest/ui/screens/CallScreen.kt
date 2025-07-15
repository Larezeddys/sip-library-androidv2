@@ .. @@
 @Composable
 fun CallScreen(
     sipViewModel: SipViewModel,
     onNavigateBack: () -> Unit
 ) {
     val uiState by sipViewModel.uiState.collectAsState()

     // OPTIMIZADO: Estados unificados
     val callState by sipViewModel.callState.collectAsState()
     val callDuration by sipViewModel.callDuration.collectAsState()
+    
+    // Estados de traducción
+    val isTranslationEnabled by sipViewModel.isTranslationEnabled.collectAsState()
+    val translationInfo by sipViewModel.translationInfo.collectAsState()

     val currentCall = uiState.currentCall
@@ .. @@
             onHold = { sipViewModel.holdCall() },
             onResume = { sipViewModel.resumeCall() },
             onMute = { sipViewModel.toggleMute() },
-            onDtmf = { digit -> sipViewModel.sendDtmf(digit) }
+            onDtmf = { digit -> sipViewModel.sendDtmf(digit) },
+            // Controles de traducción
+            isTranslationEnabled = isTranslationEnabled,
+            translationInfo = translationInfo,
+            onToggleTranslation = { enabled -> 
+                sipViewModel.setTranslationEnabled(enabled) 
+            },
+            onChangeTranslationLanguage = { language ->
+                sipViewModel.setTranslationLanguage(language)
+            }
         )
     }
 }
@@ .. @@
             hasError = uiState.hasCallError,
             errorReason = uiState.errorReason
         )

         // OPTIMIZADO: Controles de llamada con estados unificados
@@ .. @@
             onHold = { sipViewModel.holdCall() },
             onResume = { sipViewModel.resumeCall() },
             onMute = { sipViewModel.toggleMute() },
-            onDtmf = { digit -> sipViewModel.sendDtmf(digit) }
+            onDtmf = { digit -> sipViewModel.sendDtmf(digit) },
+            // Controles de traducción
+            isTranslationEnabled = isTranslationEnabled,
+            translationInfo = translationInfo,
+            onToggleTranslation = { enabled -> 
+                sipViewModel.setTranslationEnabled(enabled) 
+            },
+            onChangeTranslationLanguage = { language ->
+                sipViewModel.setTranslationLanguage(language)
+            }
         )
     }
 }
@@ .. @@
             // Información de traducción
             if (translationInfo != null) {
                 Spacer(modifier = Modifier.height(8.dp))
                 TranslationInfoCard(
                     translationInfo = translationInfo,
                     isActive = isTranslationEnabled
                 )
             }
         }

 }
@@ .. @@
     onHold: () -> Unit,
     onResume: () -> Unit,
     onMute: () -> Unit,
-    onDtmf: (Char) -> Unit
+    onDtmf: (Char) -> Unit,
+    // Parámetros de traducción
+    isTranslationEnabled: Boolean,
+    translationInfo: CallTranslationInfo?,
+    onToggleTranslation: (Boolean) -> Unit,
+    onChangeTranslationLanguage: (String) -> Unit
 ) {
     when (callState.state) {
@@ .. @@
                 // Teclado DTMF solo si está en streams running
                 if (callState.state == CallState.STREAMS_RUNNING) {
                     DtmfKeypad(onDtmf = onDtmf)
+                    
+                    Spacer(modifier = Modifier.height(16.dp))
+                    
+                    // Controles de traducción
+                    TranslationControls(
+                        isEnabled = isTranslationEnabled,
+                        translationInfo = translationInfo,
+                        onToggle = onToggleTranslation,
+                        onChangeLanguage = onChangeTranslationLanguage
+                    )
                 }
             }
         }
@@ .. @@
     }
 }

+// NUEVO: Card de información de traducción
+@Composable
+fun TranslationInfoCard(
+    translationInfo: CallTranslationInfo,
+    isActive: Boolean
+) {
+    Card(
+        colors = CardDefaults.cardColors(
+            containerColor = if (isActive) 
+                MaterialTheme.colorScheme.primaryContainer 
+            else 
+                MaterialTheme.colorScheme.surfaceVariant
+        ),
+        modifier = Modifier.fillMaxWidth()
+    ) {
+        Row(
+            modifier = Modifier.padding(12.dp),
+            verticalAlignment = Alignment.CenterVertically
+        ) {
+            Text(
+                text = if (isActive) "🌐" else "🔇",
+                style = MaterialTheme.typography.bodyLarge
+            )
+            Spacer(modifier = Modifier.width(8.dp))
+            
+            Column(modifier = Modifier.weight(1f)) {
+                Text(
+                    text = if (isActive) "Traducción activa" else "Traducción disponible",
+                    style = MaterialTheme.typography.bodyMedium,
+                    fontWeight = FontWeight.Medium
+                )
+                Text(
+                    text = "${translationInfo.localLanguage} ↔ ${translationInfo.remoteLanguage}",
+                    style = MaterialTheme.typography.bodySmall,
+                    color = MaterialTheme.colorScheme.onSurfaceVariant
+                )
+            }
+            
+            if (isActive) {
+                Icon(
+                    Icons.Default.VolumeUp,
+                    contentDescription = "Translation Active",
+                    tint = MaterialTheme.colorScheme.primary,
+                    modifier = Modifier.size(20.dp)
+                )
+            }
+        }
+    }
+}
+
+// NUEVO: Controles de traducción
+@Composable
+fun TranslationControls(
+    isEnabled: Boolean,
+    translationInfo: CallTranslationInfo?,
+    onToggle: (Boolean) -> Unit,
+    onChangeLanguage: (String) -> Unit
+) {
+    Card(
+        modifier = Modifier.fillMaxWidth()
+    ) {
+        Column(
+            modifier = Modifier.padding(16.dp)
+        ) {
+            Text(
+                text = "🌐 Traducción Simultánea",
+                style = MaterialTheme.typography.titleMedium,
+                fontWeight = FontWeight.Medium,
+                modifier = Modifier.padding(bottom = 12.dp)
+            )
+            
+            // Switch para habilitar/deshabilitar
+            Row(
+                modifier = Modifier.fillMaxWidth(),
+                horizontalArrangement = Arrangement.SpaceBetween,
+                verticalAlignment = Alignment.CenterVertically
+            ) {
+                Text(
+                    text = "Activar traducción",
+                    style = MaterialTheme.typography.bodyMedium
+                )
+                
+                Switch(
+                    checked = isEnabled,
+                    onCheckedChange = onToggle
+                )
+            }
+            
+            if (translationInfo != null) {
+                Spacer(modifier = Modifier.height(8.dp))
+                
+                // Información de idiomas
+                Row(
+                    modifier = Modifier.fillMaxWidth(),
+                    horizontalArrangement = Arrangement.SpaceBetween
+                ) {
+                    Column(
+                        horizontalAlignment = Alignment.CenterHorizontally,
+                        modifier = Modifier.weight(1f)
+                    ) {
+                        Text(
+                            text = "Local",
+                            style = MaterialTheme.typography.labelSmall,
+                            color = MaterialTheme.colorScheme.onSurfaceVariant
+                        )
+                        Text(
+                            text = translationInfo.localLanguage.uppercase(),
+                            style = MaterialTheme.typography.bodyMedium,
+                            fontWeight = FontWeight.Bold
+                        )
+                    }
+                    
+                    Icon(
+                        Icons.Default.SwapHoriz,
+                        contentDescription = "Translation Direction",
+                        modifier = Modifier.padding(horizontal = 16.dp)
+                    )
+                    
+                    Column(
+                        horizontalAlignment = Alignment.CenterHorizontally,
+                        modifier = Modifier.weight(1f)
+                    ) {
+                        Text(
+                            text = "Remoto",
+                            style = MaterialTheme.typography.labelSmall,
+                            color = MaterialTheme.colorScheme.onSurfaceVariant
+                        )
+                        Text(
+                            text = translationInfo.remoteLanguage.uppercase(),
+                            style = MaterialTheme.typography.bodyMedium,
+                            fontWeight = FontWeight.Bold
+                        )
+                    }
+                }
+                
+                // Botones de idioma rápido
+                if (isEnabled) {
+                    Spacer(modifier = Modifier.height(12.dp))
+                    
+                    Text(
+                        text = "Cambiar idioma local:",
+                        style = MaterialTheme.typography.labelMedium,
+                        modifier = Modifier.padding(bottom = 4.dp)
+                    )
+                    
+                    Row(
+                        horizontalArrangement = Arrangement.spacedBy(8.dp)
+                    ) {
+                        listOf("es", "en", "fr", "de").forEach { lang ->
+                            FilterChip(
+                                onClick = { onChangeLanguage(lang) },
+                                label = { Text(lang.uppercase()) },
+                                selected = translationInfo.localLanguage == lang,
+                                modifier = Modifier.weight(1f)
+                            )
+                        }
+                    }
+                }
+            }
+        }
+    }
+}

 @Composable
@@ .. @@
 private fun formatDuration(durationMs: Long): String {
     val seconds = (durationMs / 1000) % 60
     val minutes = (durationMs / (1000 * 60)) % 60
     val hours = (durationMs / (1000 * 60 * 60))

     return if (hours > 0) {
         String.format("%d:%02d:%02d", hours, minutes, seconds)
     } else {
         String.format("%d:%02d", minutes, seconds)
     }
 }