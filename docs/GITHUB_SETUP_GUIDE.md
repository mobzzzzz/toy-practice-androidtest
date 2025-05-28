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
| `ADMIN_TOKEN` | GitHub Personal Access Token | `repo`, `actions:write` | ✅ 필수 |

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

### ❌ **Error 2: ADMIN_TOKEN 권한 부족**

```
Error: Resource not accessible by integration
Error: 403 Forbidden
```

**원인**: `ADMIN_TOKEN`의 권한이 부족하거나 토큰이 없음

**해결 방법**:
1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. "Generate new token (classic)" 클릭
3. 다음 권한 선택:
   - ✅ `repo` (Full control of private repositories)
   - ✅ `workflow` (Update GitHub Action workflows)
   - ✅ `write:packages` (Upload packages to GitHub Package Registry)
4. 생성된 토큰을 복사
5. Repository → Settings → Secrets and variables → Actions → Secrets
6. "New repository secret" 클릭
7. Name: `ADMIN_TOKEN`, Secret: 복사한 토큰 입력

### ❌ **Error 3: 태그 생성/삭제 권한 없음**

```
Error: refusing to allow a GitHub App to create or update workflow
Error: Permission denied (publickey)
```

**원인**: 기본 `GITHUB_TOKEN`으로는 워크플로우 수정 불가

**해결 방법**: `ADMIN_TOKEN` 사용 (위 Error 2 해결 방법 참조)

### ❌ **Error 4: 릴리즈 생성 실패**

```
Error: Not Found
Error: Validation Failed
```

**원인**: 
- 릴리즈 권한 부족
- 중복된 태그명
- 잘못된 태그 형식

**해결 방법**:
1. `ADMIN_TOKEN` 권한 확인 (Error 2 참조)
2. 기존 태그/릴리즈 확인 및 정리
3. 태그 형식 확인 (`v1.0.0`, `v1.0.0-beta.20231201120000`)

### ❌ **Error 5: API Rate Limit 초과**

```
Error: API rate limit exceeded
Error: 429 Too Many Requests
```

**원인**: GitHub API 호출 한도 초과

**해결 방법**:
1. 잠시 후 워크플로우 재실행
2. Personal Access Token 사용 시 한도가 더 높음
3. 필요시 GitHub Support에 문의

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

### 3. **토큰 권한 확인**
```bash
# GitHub CLI로 확인
gh auth status

# API로 확인
curl -H "Authorization: token YOUR_TOKEN" https://api.github.com/user
```

## 📚 **추가 참고 자료**

- [GitHub Actions Variables](https://docs.github.com/en/actions/learn-github-actions/variables)
- [GitHub Actions Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [Personal Access Tokens](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
- [GitHub API Rate Limiting](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#rate-limiting)

## 🆘 **문제 해결이 안 될 때**

1. **워크플로우 로그 확인**: Actions 탭에서 실패한 작업의 상세 로그 확인
2. **설정 재확인**: Variables와 Secrets가 정확히 설정되었는지 확인
3. **권한 재확인**: Personal Access Token의 권한이 충분한지 확인
4. **GitHub Status 확인**: [GitHub Status](https://www.githubstatus.com/)에서 서비스 장애 여부 확인
5. **Issue 생성**: 위 방법으로도 해결되지 않으면 Repository에 Issue 생성 