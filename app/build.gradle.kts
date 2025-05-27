import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

fun loadConfigProperties(buildType: String): Properties {
    return Properties().apply {
        val propsFile = rootProject.file("config/$buildType.properties")
        if (propsFile.exists()) {
            load(FileInputStream(propsFile))
        }
    }
}

fun getVersionFromTag(): Triple<Int, Int, Int> {
    try {
        // CI/CD에서 생성한 베타 태그가 있는 경우 해당 버전 사용
        System.getenv("BETA_VERSION")?.let { betaVersion ->
            val version = betaVersion.split(".")
            return Triple(
                version.getOrNull(0)?.toIntOrNull() ?: 1,
                version.getOrNull(1)?.toIntOrNull() ?: 0,
                version.getOrNull(2)?.toIntOrNull() ?: 0,
            )
        }

        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "describe", "--tags", "--abbrev=0")
            standardOutput = stdout
        }
        val tag = stdout.toString().trim()
        val version = tag.removePrefix("v").split(".")
        return Triple(
            version.getOrNull(0)?.toIntOrNull() ?: 1,
            version.getOrNull(1)?.toIntOrNull() ?: 0,
            version.getOrNull(2)?.toIntOrNull() ?: 0,
        )
    } catch (e: Exception) {
        return Triple(1, 0, 0)
    }
}

android {
    namespace = "toy.practice.androidtest"
    compileSdk = 35

    defaultConfig {
        applicationId = "toy.practice.androidtest"
        minSdk = 24
        targetSdk = 35

        // 버전 코드는 환경변수나 프로젝트 속성에서 가져옴
        versionCode = System.getenv("VERSION_CODE")?.toIntOrNull() // CI/CD
            ?: project.findProperty("VERSION_CODE")?.toString()?.toIntOrNull() // 로컬
            ?: 1 // 기본값

        // 버전명은 Git 태그 기반
        val (major, minor, patch) = getVersionFromTag()
        versionName =
            buildString {
                append("$major.$minor.$patch")
                if (!project.hasProperty("release")) { // 릴리즈 빌드가 아닐 경우
                    append("-beta")
                    System.getenv("BUILD_TIMESTAMP")?.let { append(".$it") }
                }
            }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        val debugProps = loadConfigProperties("debug")
        val releaseProps = loadConfigProperties("release")

        debug {
            debugProps.forEach { (key, value) ->
                buildConfigField("String", key.toString(), "\"$value\"")
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            releaseProps.forEach { (key, value) ->
                buildConfigField("String", key.toString(), "\"$value\"")
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
        }
    }

    android.applicationVariants.all {
        val variant = this
        outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val appName = variant.applicationId.replace(".", "-")
                output.outputFileName = "$appName-${variant.versionName}-${variant.buildType.name}.apk"
            }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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

// Add task to print applicationId
tasks.register("printApplicationId") {
    doLast {
        println(android.defaultConfig.applicationId)
    }
}
