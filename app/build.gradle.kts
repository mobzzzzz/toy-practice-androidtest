import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "toy.practice.androidtest"
    compileSdk = 35

    defaultConfig {
        applicationId = "toy.practice.androidtest"
        minSdk = 24
        targetSdk = 35

        val versionProps =
            Properties().apply {
                val propsFile = rootProject.file("version.properties")
                if (propsFile.exists()) {
                    load(FileInputStream(propsFile))
                }
            }

        versionCode = (versionProps["VERSION_CODE"] as? String)?.toIntOrNull() ?: 1
        val vMajor = (versionProps["VERSION_MAJOR"] as? String)?.toIntOrNull() ?: 1
        val vMinor = (versionProps["VERSION_MINOR"] as? String)?.toIntOrNull() ?: 0
        val vPatch = (versionProps["VERSION_PATCH"] as? String)?.toIntOrNull() ?: 0

        versionName = "$vMajor.$vMinor.$vPatch"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    implementation("androidx.compose.material:material-icons-extended:1.6.1")

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
