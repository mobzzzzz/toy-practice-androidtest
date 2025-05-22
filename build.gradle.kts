// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.diffplug.spotless") version "6.25.0"
}

// Spotless 공통 설정
spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt", "**/bin/**/*.kt")
        ktlint()
            .setEditorConfigPath("${rootProject.projectDir}/.editorconfig")
        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
    }

    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
    }

    java {
        target("**/*.java")
        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
    }
}

// Git Hooks 설정
tasks.register<Copy>("updateGitHooks") {
    doFirst {
        // hooks 디렉토리 경로 확인 및 생성
        val hooksDir = rootProject.file(".git/hooks")
        if (!hooksDir.exists()) {
            logger.lifecycle("Creating hooks directory: ${hooksDir.absolutePath}")
            hooksDir.mkdirs()
        }
    }

    from(rootProject.file("scripts/pre-commit"))
    into(rootProject.file(".git/hooks"))

    doLast {
        // 파일 존재 여부 확인
        val preCommitFile = rootProject.file(".git/hooks/pre-commit")
        if (!preCommitFile.exists()) {
            throw GradleException("Failed to copy pre-commit hook to ${preCommitFile.absolutePath}")
        }

        // 실행 권한 부여
        exec {
            commandLine("chmod", "+x", preCommitFile.absolutePath)
        }

        logger.lifecycle("Successfully installed git hook: ${preCommitFile.absolutePath}")
    }
}

// 모든 서브프로젝트에 spotless 적용
subprojects {
    apply(plugin = "com.diffplug.spotless")

    tasks.matching { it.name == "preBuild" }.configureEach {
        dependsOn(":updateGitHooks")
    }
}
