# 🔧 워크플로우 유지보수 가이드

> 새로운 Android 배포 담당자를 위한 완전 가이드

## 📋 목차
1. [빠른 시작](#빠른-시작)
2. [워크플로우 구조 이해](#워크플로우-구조-이해)
3. [일반적인 문제 해결](#일반적인-문제-해결)
4. [안전한 수정 방법](#안전한-수정-방법)
5. [비상 상황 대응](#비상-상황-대응)

## 🚀 빠른 시작

### 새 담당자가 첫 날에 해야 할 일

1. **권한 확인**
   ```bash
   # Repository Settings → Actions → General
   # "Allow GitHub Actions to create and approve pull requests" 체크
   ```

2. **필수 설정 확인**
   ```bash
   # Settings → Secrets and variables → Actions
   ADMIN_TOKEN: [GitHub Personal Access Token]
   VERSION_CODE: [현재 버전 코드 숫자]
   ```

3. **테스트 실행**
   ```bash
   # 간단한 PR 생성해서 CI 작동 확인
   git checkout -b test/workflow-check
   echo "# Test" >> README.md
   git commit -am "test: 워크플로우 동작 확인"
   # PR 생성 후 CI 성공 확인
   ```

## 🏗️ 워크플로우 구조 이해

### 핵심 원칙: "건드리지 말아야 할 것 vs 수정해도 되는 것"

#### ✅ 안전하게 수정 가능한 것들
```yaml
# 1. 알림 메시지
echo "🎉 릴리즈 완료!"

# 2. 타임아웃 설정
timeout-minutes: 30

# 3. 아티팩트 보관 기간
retention-days: 30

# 4. 빌드 환경
java-version: '17'
```

#### ⚠️ 주의해서 수정해야 할 것들
```yaml
# 1. 브랜치 이름
branches: ["dev", "main"]

# 2. 버전 계산 로직
if [[ "$branch_name" =~ ^feat/ ]]; then
  minor=$((minor + 1))
```

#### 🚨 절대 건드리면 안 되는 것들
```yaml
# 1. 토큰 사용
token: ${{ secrets.ADMIN_TOKEN }}

# 2. 워크플로우 간 의존성
needs: [validate-config, generate-timestamp]

# 3. 브랜치 보호 우회 로직
git push --force-with-lease
```

### 워크플로우 파일별 역할

| 파일 | 복잡도 | 수정 빈도 | 주요 역할 |
|------|--------|-----------|-----------|
| `ci.yml` | ⭐⭐ | 높음 | PR 검증 (안전하게 수정 가능) |
| `cd-dev.yml` | ⭐⭐⭐⭐ | 낮음 | 베타 릴리즈 (신중하게 수정) |
| `cd-main.yml` | ⭐⭐⭐ | 낮음 | 정식 릴리즈 (신중하게 수정) |
| `sync-main-to-dev.yml` | ⭐⭐⭐⭐⭐ | 매우 낮음 | 브랜치 동기화 (건드리지 말 것) |
| `build-apk.yml` | ⭐⭐ | 보통 | APK 빌드 (안전하게 수정 가능) |
| `create-release.yml` | ⭐⭐⭐⭐ | 낮음 | 릴리즈 생성 (신중하게 수정) |
| `validate-github-config.yml` | ⭐⭐⭐ | 낮음 | 설정 검증 (거의 수정 안 함) |

## 🔧 일반적인 문제 해결

### 문제 1: "VERSION_CODE가 없다는 오류"
```bash
# 해결 방법
# Settings → Secrets and variables → Actions → Variables
# Name: VERSION_CODE, Value: 1 (또는 현재 버전)
```

### 문제 2: "Permission denied" 오류
```bash
# 원인: ADMIN_TOKEN 권한 부족
# 해결: Personal Access Token 재생성
# 필요 권한: repo, workflow, admin:repo_hook
```

### 문제 3: "Workflow 실행이 안 됨"
```bash
# 체크리스트
1. Repository Settings → Actions → General → "Allow all actions" 확인
2. Branch protection rules 확인
3. ADMIN_TOKEN 만료 여부 확인
```

### 문제 4: "베타 릴리즈가 생성되지 않음"
```bash
# 디버깅 순서
1. Actions 탭에서 실패한 워크플로우 확인
2. "validate-config" 단계 로그 확인
3. VERSION_CODE 설정 확인
4. dev 브랜치 PR이 실제로 머지되었는지 확인
```

## 🛠️ 안전한 수정 방법

### 1단계: 테스트 브랜치에서 검증
```bash
# 절대 main/dev에서 직접 수정하지 말 것!
git checkout -b test/workflow-modification
# 워크플로우 파일 수정
git commit -am "test: 워크플로우 수정 테스트"
# PR 생성 후 CI 확인
```

### 2단계: 점진적 배포
```bash
# 한 번에 여러 파일 수정하지 말 것
# 하나씩 수정하고 테스트 후 다음 단계 진행
```

### 3단계: 롤백 계획 준비
```bash
# 수정 전 현재 상태 백업
git tag backup/before-workflow-change-$(date +%Y%m%d)
git push origin backup/before-workflow-change-$(date +%Y%m%d)
```

## 🚨 비상 상황 대응

### 상황 1: 모든 워크플로우가 실패하는 경우
```bash
# 임시 해결책: 수동 릴리즈
1. GitHub Releases 페이지에서 "Create a new release"
2. Tag: v[버전] (예: v1.3.0)
3. 수동으로 APK 업로드
4. 워크플로우 수정 후 정상화
```

### 상황 2: 잘못된 버전이 릴리즈된 경우
```bash
# 긴급 대응
1. GitHub Releases에서 해당 릴리즈 삭제
2. Git 태그 삭제: git push origin :refs/tags/v[잘못된버전]
3. VERSION_CODE 원복
4. 올바른 버전으로 재릴리즈
```

### 상황 3: 브랜치 동기화가 깨진 경우
```bash
# 수동 동기화
git checkout dev
git fetch origin
git reset --hard origin/main  # 주의: dev의 변경사항 손실됨
git push --force-with-lease origin dev
```

## 📚 추가 학습 자료

### GitHub Actions 기초
- [GitHub Actions 공식 문서](https://docs.github.com/en/actions)
- [YAML 문법 가이드](https://yaml.org/spec/1.2/spec.html)

### Android CI/CD
- [Android 빌드 최적화](https://developer.android.com/studio/build/optimize-your-build)
- [Gradle 빌드 캐시](https://docs.gradle.org/current/userguide/build_cache.html)

## 🆘 도움 요청 방법

1. **GitHub Issues**: 워크플로우 관련 문제 보고
2. **Actions 로그**: 실패한 워크플로우의 상세 로그 첨부
3. **환경 정보**: Android Studio 버전, JDK 버전 등

---

> 💡 **기억하세요**: 확실하지 않으면 건드리지 마세요. 대부분의 경우 기존 설정이 최적화되어 있습니다. 