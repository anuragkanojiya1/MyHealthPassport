//plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.jetbrains.kotlin.android)
//    id("kotlin-parcelize")
//    alias(libs.plugins.google.gms.google.services)
//    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
//}
//
//android {
//    namespace = "com.example.myhealthpassport"
//    compileSdk = 34
//
//    defaultConfig {
//        applicationId = "com.example.myhealthpassport"
//        minSdk = 24
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary = true
//        }
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//    buildFeatures {
//        compose = true
//        buildConfig = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.1"
//    }
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
//}
//
//buildscript {
//    dependencies {
//        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
//    }
//}
//
//dependencies {
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    implementation("com.google.firebase:firebase-auth:21.0.1")
//    implementation(libs.firebase.storage)
//    val nav_version = "2.7.7"
//    implementation("androidx.navigation:navigation-compose:$nav_version")
//
//    // AI dependency
//    implementation(libs.generativeai)
//
//    // Testing dependencies
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
//
//    implementation("com.airbnb.android:lottie-compose:6.4.0")
//    implementation("androidx.core:core-splashscreen:1.0.1")
//
//    // Import the BoM for the Firebase platform
//    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
//
//    // Declare the dependency for the Cloud Firestore library
//    // When using the BoM, you don't specify versions in Firebase library dependencies
//    implementation("com.google.firebase:firebase-firestore")
//
//    implementation("androidx.compose.runtime:runtime-livedata:1.6.8")
//
//    implementation("io.coil-kt:coil-compose:2.7.0")
//
//    val retrofitVersion = "2.11.0"
//    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
//    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
//
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
//    implementation("com.squareup.okhttp3:okhttp:4.9.1")
//
//    //MongoDB driver
//   // implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.1.2")
//}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.google.gms.google.services)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.example.myhealthpassport"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myhealthpassport"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)
    val nav_version = "2.7.7"
    implementation(libs.androidx.navigation.compose)

    // AI dependency
    implementation(libs.generativeai)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.lottie.compose)
    implementation(libs.androidx.core.splashscreen)

    // Import the BoM for the Firebase platform
    implementation(libs.firebase.bom)

    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.firestore)

    implementation(libs.androidx.runtime.livedata)

    implementation(libs.coil.compose)

    val retrofitVersion = "2.11.0"
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.okhttp)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    //MongoDB driver
    // implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.1.2")
}

