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
        minSdk = 25
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
    implementation (libs.androidx.core.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx.v231)
    implementation (libs.androidx.activity.compose.v131)
    implementation (libs.androidx.ui.v166)
    implementation (libs.androidx.ui.tooling.preview.v166)
    implementation (libs.material3)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.database)

    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit)
    androidTestImplementation (libs.androidx.espresso.core)
    androidTestImplementation (libs.androidx.ui.test.junit4.v166)
    debugImplementation (libs.androidx.ui.tooling.v166)
    debugImplementation (libs.androidx.ui.test.manifest.v166)
    implementation(libs.androidx.runtime.livedata.v166)
    implementation (libs.firebase.auth.ktx.v2231)
    implementation (libs.play.services.auth.v2041)

    implementation (libs.androidx.lifecycle.viewmodel.compose.v260)
    implementation (libs.androidx.lifecycle.runtime.compose.v260)
    implementation (libs.androidx.navigation.compose.v253)
    implementation (libs.coil.compose)
}