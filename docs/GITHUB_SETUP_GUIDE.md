# 🔧 GitHub 설정 가이드

이 프로젝트의 CI/CD 워크플로우가 정상적으로 작동하려면 다음 GitHub 설정들이 필요합니다.

## 📋 **필수 설정 목록**

### 1. **Repository Variables**

GitHub Repository → Settings → Secrets and variables → Actions → Variables 탭에서 설정:

| 변수명 | 설명 | 예시 값 | 필수 여부 |
|--------|------|---------|-----------|
| `VERSION_CODE` | APK 버전 코드 | `1` | ✅ 필수 |

### 2. **Repository Secrets**

GitHub Repository → Settings → Secrets and variables → Actions → Secrets 탭에서 설정:

| 시크릿명 | 설명 | 권한 요구사항 | 필수 여부 |
|----------|------|---------------|-----------|
| `APP_ID` | GitHub App ID | GitHub App 생성 필요 | ✅ 필수 |
| `APP_PRIVATE_KEY` | GitHub App Private Key | GitHub App 생성 필요 | ✅ 필수 |

## 🤖 **GitHub App 생성 및 설정**

### 1. **GitHub App 생성**

1. GitHub → Settings → Developer settings → GitHub Apps
2. "New GitHub App" 클릭
3. 다음 정보 입력:
   - **App name**: `[프로젝트명]-ci-cd` (예: `toy-android-practice-ci-cd`)
   - **Homepage URL**: 리포지토리 URL
   - **Webhook**: 체크 해제 (필요 없음)

### 2. **권한 설정**

다음 권한들을 설정해주세요:

#### Repository permissions:
- ✅ **Actions**: Write (워크플로우 트리거)
- ✅ **Contents**: Write (코드 푸시, 태그 생성)
- ✅ **Metadata**: Read (기본 정보 읽기)
- ✅ **Variables**: Write (VERSION_CODE 업데이트)
- ✅ **Pull requests**: Write (PR 생성)

### 3. **Private Key 생성**

1. 생성된 GitHub App 설정 페이지에서 "Generate a private key" 클릭
2. 다운로드된 `.pem` 파일 내용을 복사

### 4. **GitHub App 설치**

1. GitHub App 설정 페이지에서 "Install App" 클릭
2. 대상 리포지토리 선택하여 설치

### 5. **Repository Secrets 설정**

1. Repository → Settings → Secrets and variables → Actions → Secrets
2. "New repository secret" 클릭하여 다음 2개 추가:
   - **Name**: `APP_ID`, **Secret**: GitHub App ID (숫자)
   - **Name**: `APP_PRIVATE_KEY`, **Secret**: `.pem` 파일 전체 내용

## 🚨 **자주 발생하는 에러와 해결 방법**

### ❌ **Error 1: VERSION_CODE 변수 없음**

```
Error: Variable VERSION_CODE not found
```

**원인**: Repository Variables에 `VERSION_CODE`가 설정되지 않음

**해결 방법**:
1. GitHub Repository → Settings → Secrets and variables → Actions
2. Variables 탭 클릭
3. "New repository variable" 클릭
4. Name: `VERSION_CODE`, Value: `1` 입력
5. "Add variable" 클릭

### ❌ **Error 2: GitHub App 설정 오류**

```
Error: APP_ID 시크릿이 설정되지 않았습니다
Error: APP_PRIVATE_KEY 시크릿이 설정되지 않았습니다
```

**원인**: GitHub App 관련 Secrets가 설정되지 않음

**해결 방법**: 위의 "GitHub App 생성 및 설정" 섹션 참조

### ❌ **Error 3: GitHub App 권한 부족**

```
Error: Resource not accessible by integration
Error: 403 Forbidden
```

**원인**: GitHub App의 권한이 부족하거나 설치되지 않음

**해결 방법**:
1. GitHub App 설정에서 권한 확인 (위의 권한 설정 참조)
2. GitHub App이 리포지토리에 설치되어 있는지 확인
3. 권한 변경 후 앱을 리포지토리에 재설치

### ❌ **Error 4: GitHub App 토큰 생성 실패**

```
Error: GitHub App 토큰이 유효하지 않습니다
```

**원인**: 
- APP_ID와 APP_PRIVATE_KEY가 올바르지 않음
- Private key가 만료됨
- GitHub App이 삭제됨

**해결 방법**:
1. APP_ID가 올바른지 확인
2. APP_PRIVATE_KEY가 완전한 .pem 파일 내용인지 확인
3. GitHub App이 여전히 존재하는지 확인
4. 필요시 새로운 Private key 생성

### ❌ **Error 5: 릴리즈 생성 실패**

```
Error: Not Found
Error: Validation Failed
```

**원인**: 
- 릴리즈 권한 부족
- 중복된 태그명
- 잘못된 태그 형식

**해결 방법**:
1. GitHub App 권한 확인 (Error 3 참조)
2. 기존 태그/릴리즈 확인 및 정리
3. 태그 형식 확인 (`v1.0.0`, `v1.0.0-beta.20231201120000`)

## 🔍 **설정 확인 방법**

### 1. **Variables 확인**
```bash
# Repository Settings에서 확인
GitHub Repository → Settings → Secrets and variables → Actions → Variables
```

### 2. **Secrets 확인**
```bash
# Repository Settings에서 확인 (값은 보이지 않음)
GitHub Repository → Settings → Secrets and variables → Actions → Secrets
```

### 3. **GitHub App 확인**
```bash
# GitHub App 설정 페이지에서 확인
GitHub → Settings → Developer settings → GitHub Apps
```

### 4. **GitHub App 설치 확인**
```bash
# Repository Settings에서 확인
GitHub Repository → Settings → Integrations → GitHub Apps
```

## 🆚 **GitHub App vs Personal Access Token**

| 항목 | GitHub App | Personal Access Token |
|------|------------|----------------------|
| **보안** | ✅ 높음 (세밀한 권한 제어) | ⚠️ 낮음 (광범위한 권한) |
| **만료** | ✅ 자동 갱신 (1시간) | ❌ 수동 갱신 필요 |
| **감사** | ✅ 상세한 활동 로그 | ⚠️ 제한적 로그 |
| **관리** | ✅ 중앙 집중식 | ❌ 개별 관리 |
| **권한** | ✅ 리포지토리별 세밀 제어 | ❌ 계정 전체 권한 |

## 📚 **추가 참고 자료**

- [GitHub Apps Documentation](https://docs.github.com/en/apps)
- [GitHub Actions Variables](https://docs.github.com/en/actions/learn-github-actions/variables)
- [GitHub Actions Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [Creating GitHub Apps](https://docs.github.com/en/apps/creating-github-apps)

## 🆘 **문제 해결이 안 될 때**

1. **워크플로우 로그 확인**: Actions 탭에서 실패한 작업의 상세 로그 확인
2. **설정 재확인**: Variables와 Secrets가 정확히 설정되었는지 확인
3. **GitHub App 재확인**: App 권한과 설치 상태 확인
4. **GitHub Status 확인**: [GitHub Status](https://www.githubstatus.com/)에서 서비스 장애 여부 확인
5. **Issue 생성**: 위 방법으로도 해결되지 않으면 Repository에 Issue 생성 