@@ .. @@
         item {
             // Configuraciones de audio
             AudioSettingsCard()
         }
+        
+        item {
+            // Configuraciones de traducción
+            TranslationSettingsCard(sipViewModel = sipViewModel)
+        }

         item {
@@ .. @@
     }
 }

+@Composable
+fun TranslationSettingsCard(
+    sipViewModel: SipViewModel
+) {
+    val isTranslationEnabled by sipViewModel.isTranslationEnabled.collectAsState()
+    val translationInfo by sipViewModel.translationInfo.collectAsState()
+    
+    var selectedLanguage by remember { mutableStateOf("es") }
+
+    Card(
+        modifier = Modifier.fillMaxWidth()
+    ) {
+        Column(
+            modifier = Modifier.padding(16.dp)
+        ) {
+            Text(
+                text = "Translation Settings",
+                style = MaterialTheme.typography.headlineSmall,
+                fontWeight = FontWeight.Medium,
+                modifier = Modifier.padding(bottom = 16.dp)
+            )
+
+            SettingSwitchItem(
+                label = "Enable Translation",
+                description = "Enable real-time call translation",
+                checked = isTranslationEnabled,
+                onCheckedChange = { sipViewModel.setTranslationEnabled(it) },
+                icon = Icons.Default.Translate
+            )
+            
+            Spacer(modifier = Modifier.height(16.dp))
+            
+            Text(
+                text = "Default Language",
+                style = MaterialTheme.typography.bodyMedium,
+                fontWeight = FontWeight.Medium,
+                modifier = Modifier.padding(bottom = 8.dp)
+            )
+            
+            // Selector de idioma
+            Row(
+                horizontalArrangement = Arrangement.spacedBy(8.dp),
+                modifier = Modifier.fillMaxWidth()
+            ) {
+                val languages = mapOf(
+                    "es" to "Español",
+                    "en" to "English", 
+                    "fr" to "Français",
+                    "de" to "Deutsch"
+                )
+                
+                languages.forEach { (code, name) ->
+                    FilterChip(
+                        onClick = { 
+                            selectedLanguage = code
+                            sipViewModel.setTranslationLanguage(code)
+                        },
+                        label = { Text(name) },
+                        selected = selectedLanguage == code,
+                        modifier = Modifier.weight(1f)
+                    )
+                }
+            }
+            
+            if (translationInfo != null) {
+                Spacer(modifier = Modifier.height(12.dp))
+                
+                Card(
+                    colors = CardDefaults.cardColors(
+                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
+                    )
+                ) {
+                    Column(
+                        modifier = Modifier.padding(12.dp)
+                    ) {
+                        Text(
+                            text = "Current Translation",
+                            style = MaterialTheme.typography.labelMedium,
+                            fontWeight = FontWeight.Bold
+                        )
+                        Text(
+                            text = "${translationInfo.localLanguage} ↔ ${translationInfo.remoteLanguage}",
+                            style = MaterialTheme.typography.bodySmall
+                        )
+                        Text(
+                            text = "Direction: ${translationInfo.translationDirection}",
+                            style = MaterialTheme.typography.bodySmall
+                        )
+                    }
+                }
+            }
+            
+            Spacer(modifier = Modifier.height(12.dp))
+            
+            OutlinedButton(
+                onClick = {
+                    Log.d("TranslationDiagnostic", sipViewModel.getTranslationDiagnostic())
+                },
+                modifier = Modifier.fillMaxWidth()
+            ) {
+                Icon(Icons.Default.BugReport, contentDescription = null)
+                Spacer(modifier = Modifier.width(8.dp))
+                Text("Translation Diagnostic")
+            }
+        }
+    }
+}
+
 @Composable
 fun LibraryInfoCard() {
@@ .. @@
             InfoItem(
                 label = "Version",
-                value = "1.1.0",
+                value = "1.5.0 with Translation",
                 icon = Icons.Default.Info
             )

             InfoItem(
@@ .. @@
             InfoItem(
                 label = "Protocol",
-                value = "SIP/WebRTC",
+                value = "SIP/WebRTC + AI Translation",
                 icon = Icons.Default.Wifi
             )
         }
@@ .. @@
                 Text("Test Call (*123)")
             }

             Spacer(modifier = Modifier.height(8.dp))
+            
+            Button(
+                onClick = {
+                    // Test translation
+                    sipViewModel.setTranslationEnabled(!sipViewModel.isTranslationEnabled.value)
+                },
+                modifier = Modifier.fillMaxWidth(),
+                colors = ButtonDefaults.buttonColors(
+                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
+                )
+            ) {
+                Icon(Icons.Default.Translate, contentDescription = null)
+                Spacer(modifier = Modifier.width(8.dp))
+                Text("Toggle Translation")
+            }

+            Spacer(modifier = Modifier.height(8.dp))

             Button(
@@ .. @@
             OutlinedButton(
                 onClick = {
                     // Mostrar información de diagnóstico
+                    Log.d("SipDiagnostic", sipViewModel.getSystemDiagnostic())
                 },
                 modifier = Modifier.fillMaxWidth()
             ) {