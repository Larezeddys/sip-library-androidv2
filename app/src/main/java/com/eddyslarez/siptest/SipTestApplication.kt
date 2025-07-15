@@ .. @@
     private fun initializeSipLibrary() {
         val config = EddysSipLibrary.SipConfig(
             defaultDomain = "mcn.ru",
             webSocketUrl = "wss://webrtc.mcn.ru:35060/",
             userAgent = "SipTestApp/1.0",
             enableLogs = true,
             enableAutoReconnect = true,
-            pingIntervalMs = 30000L
+            pingIntervalMs = 30000L,
+            // Configuración de traducción
+            enableTranslation = true,
+            openAiApiKey = "tu-api-key-aqui", // Reemplaza con tu API key real
+            defaultLanguage = "es"
         )

         sipLibrary.initialize(
@@ .. @@
         sipLibrary.setRegistrationListener(object : EddysSipLibrary.RegistrationListener {
             override fun onRegistrationSuccessful(username: String, domain: String) {
                 Log.d(TAG, "✅ Registro exitoso: $username@$domain")

                 // Verificar si es la cuenta actual que estamos registrando
                 val currentAccount = getCurrentAccount()
                 if (currentAccount?.username == username && currentAccount.domain == domain) {
                     proceedToNextAccount()
                 }
             }