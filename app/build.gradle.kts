plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.pizzasatpovo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pizzasatpovo"
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

dependencies {

    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation ("androidx.activity:activity-compose:1.3.1")
    implementation ("androidx.compose.ui:ui:1.6.6")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.6.6")
    implementation ("androidx.compose.material3:material3:1.2.0")
    implementation(libs.firebase.auth)
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.6.6")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.6.6")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.6.6")

    implementation ("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation ("com.google.android.gms:play-services-auth:20.4.1")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    implementation ("androidx.navigation:navigation-compose:2.5.3")
    implementation ("io.coil-kt:coil-compose:2.2.2")
}