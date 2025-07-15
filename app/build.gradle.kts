@@ .. @@
 dependencies {
     implementation("androidx.core:core-ktx:1.16.0")
     implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
     implementation("androidx.activity:activity-compose:1.9.3")
     implementation(platform("androidx.compose:compose-bom:2024.12.01"))
     implementation("androidx.compose.ui:ui")
     implementation("androidx.compose.ui:ui-graphics")
     implementation("androidx.compose.ui:ui-tooling-preview")
     implementation("androidx.compose.material3:material3")
     implementation("androidx.navigation:navigation-compose:2.8.5")
     implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")
     implementation("androidx.compose.material:material-icons-extended:1.7.6")
     implementation("androidx.core:core-permissions:1.0.0-alpha02")
+    
+    // Dependencias adicionales para traducción
+    implementation("androidx.compose.material3:material3-adaptive:1.0.0")
+    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")
 
     // Proyecto local
     implementation(project(":siplibrary"))