plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
    id ("com.google.dagger.hilt.android") version "2.56.1"
}

android {
    namespace = "com.myjournalapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.myjournalapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // Rất quan trọng: Phiên bản này phải tương thích với phiên bản Kotlin bạn đang dùng
        kotlinCompilerExtensionVersion = "1.5.8" // Kiểm tra phiên bản tương thích tại docs của Compose
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Loại trừ các tệp license trùng lặp
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.datastore:datastore-preferences:1.1.4") // Hoặc phiên bản mới hơn
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Cần cho lifecycleScope

    implementation(platform("androidx.compose:compose-bom:2024.02.02")) // Ví dụ BOM
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation") // Chứa animation core
    implementation("androidx.core:core-splashscreen:1.0.1")

    // --- Core AndroidX ---
    implementation("androidx.core:core-ktx:1.12.0") // Core Kotlin extensions
    implementation("androidx.appcompat:appcompat:1.6.1") // Vẫn cần cho một số resource theme

    // --- Lifecycle (ViewModel, LiveData/StateFlow aware components) ---
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Lifecycle KTX
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // ViewModel KTX
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0") // Collect Flow safely in Compose

    // --- Jetpack Compose ---
    implementation(platform("androidx.compose:compose-bom:2024.02.02")) // BOM quản lý phiên bản Compose
    implementation("androidx.compose.ui:ui") // Core UI
    implementation("androidx.compose.ui:ui-graphics") // Graphics primitives
    implementation("androidx.compose.ui:ui-tooling-preview") // Preview Composable trong Android Studio
    implementation("androidx.compose.material3:material3") // Material Design 3 components
    implementation("androidx.compose.foundation:foundation") // Foundation components (Pager, Layouts...)
    implementation("androidx.activity:activity-compose:1.8.2") // Tích hợp Compose vào Activity
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") // `viewModel()` composable function

    // --- Navigation Compose ---
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // --- DataStore (Ví dụ dùng Preferences DataStore để lưu trạng thái onboarding) ---
    implementation("androidx.datastore:datastore-preferences:1.0.0") // Hoặc datastore-core nếu dùng Proto

    // --- Hilt (Nếu dùng Dependency Injection) ---
    implementation("com.google.dagger:hilt-android:2.51")
    ksp("com.google.dagger:hilt-compiler:2.51") // KSP processor cho Hilt
    // implementation("androidx.hilt:hilt-compiler:1.2.0") // KSP processor cho các thư viện androidx Hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // Hỗ trợ inject ViewModel vào Navigation Compose

    // --- Room (Nếu dùng database Room) ---
    // implementation("androidx.room:room-runtime:2.6.1")
    // implementation("androidx.room:room-ktx:2.6.1") // Coroutines support
    // ksp("androidx.room:room-compiler:2.6.1")

    // --- Coroutines ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Coroutines cho Android

    // --- Unit Testing ---
    testImplementation("junit:junit:4.13.2")

    // --- Android Testing ---
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.02")) // Dùng lại BOM
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4") // Compose UI tests

    // --- Debugging ---
    debugImplementation("androidx.compose.ui:ui-tooling") // Tools để inspect layout,...
    debugImplementation("androidx.compose.ui:ui-test-manifest") // Manifest cho tests
}

apply(plugin = "com.google.dagger.hilt.android")