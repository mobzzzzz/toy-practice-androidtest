# 🤖 Android Toy Practice

Android 개발 연습용 프로젝트입니다.

## 🚀 **CI/CD 워크플로우**

이 프로젝트는 자동화된 CI/CD 파이프라인을 사용합니다:

- **CI (Continuous Integration)**: PR 생성 시 코드 검사, 테스트, 빌드
- **CD (Continuous Deployment)**: PR 머지 시 자동 릴리즈 생성

### 📋 **워크플로우 구조**

```
PR 생성 → CI 실행 (코드 검사, 테스트, 빌드)
    ↓
PR 머지 → CD 실행 (릴리즈 생성)
    ↓
dev 머지 → 베타 릴리즈 생성
main 머지 → 정식 릴리즈 생성
```

## ⚙️ **GitHub 설정 요구사항**

CI/CD 워크플로우가 정상적으로 작동하려면 다음 설정이 필요합니다:

### 🔧 **필수 설정**

| 설정 타입 | 이름 | 설명 | 필수 여부 |
|-----------|------|------|-----------|
| Variable | `VERSION_CODE` | APK 버전 코드 | ✅ 필수 |
| Secret | `ADMIN_TOKEN` | GitHub Personal Access Token | ✅ 필수 |

### 📚 **상세 설정 가이드**

**🚨 워크플로우 실행 중 에러가 발생한다면:**

👉 **[GitHub 설정 가이드](docs/GITHUB_SETUP_GUIDE.md)** 를 확인하세요!

이 가이드에는 다음 내용이 포함되어 있습니다:
- 필수 설정 방법
- 자주 발생하는 에러와 해결 방법
- 권한 설정 가이드
- 문제 해결 방법

## 🏗️ **개발 환경 설정**

### 요구사항
- Android Studio Arctic Fox 이상
- JDK 17
- Android SDK 34

### 빌드 방법
```bash
# Debug 빌드
./gradlew assembleDebug

# Release 빌드
./gradlew assembleRelease
```

### 테스트 실행
```bash
# 단위 테스트
./gradlew testDebugUnitTest

# 코드 스타일 검사
./gradlew spotlessCheck

# Android Lint
./gradlew lintDebug
```

## 📱 **APK 다운로드**

- **베타 버전**: [GitHub Releases](../../releases) 에서 `v*-beta*` 태그
- **정식 버전**: [GitHub Releases](../../releases) 에서 `v*` 태그 (beta 제외)

## 🤝 **기여 방법**

1. 이 저장소를 Fork
2. 새 브랜치 생성 (`feat/새기능` 또는 `fix/버그수정`)
3. 변경사항 커밋
4. 브랜치에 Push
5. Pull Request 생성

## 📄 **라이선스**

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 