# 🚫 CI/CD 스킵 기능 가이드

문서나 워크플로우 변경 시 불필요한 CI 실행과 APK 빌드/배포를 방지하는 기능입니다.

## 📋 스킵 조건

### 1. **PR 라벨 스킵 (최우선)**
PR에 `skip-ci` 라벨이 있으면 CI와 배포가 모두 스킵됩니다.

### 2. **커밋 메시지 키워드**
PR 제목에 `[skip ci]` 키워드를 포함하면 CI와 배포가 모두 스킵됩니다.

## 🎯 스킵되는 작업

### CI 워크플로우
- **Validate Code**: Code Style Check, Android Lint
- **Test and Build**: Unit Tests, Debug APK Build
- **CI Status Check**: 전체 CI 상태 확인

### CD 워크플로우 (Dev/Main)
- **APK 빌드**: Release/Debug APK 생성
- **버전 관리**: 태그 생성, 버전 코드 업데이트
- **릴리즈 생성**: GitHub Release 생성

## 💡 사용 예시

### ✅ CI/CD 스킵되는 경우

#### 1. **PR 라벨 사용 (권장)**
```markdown
# PR 생성 시 skip-ci 라벨 추가
gh pr create --title "docs: API 가이드 추가" --label "skip-ci"
```

#### 2. **PR 제목 키워드**
```markdown
# PR 제목 예시
docs: API 문서 업데이트 [skip ci]
fix: README 오타 수정 [skip ci]
workflow: GitHub Actions 개선 [skip ci]
```

### ❌ CI/CD 진행되는 경우

```markdown
# 일반적인 개발 작업
feat: 새로운 로그인 기능 추가
fix: 버그 수정 및 성능 개선
refactor: 코드 리팩토링
```

## 🔄 CI/CD를 원하는 경우

스킵 조건에 해당하지만 CI/CD가 필요한 경우:

### 방법 1: 라벨 제거 (가장 간단)
1. PR 페이지에서 `skip-ci` 라벨 제거
2. 워크플로우가 자동으로 다시 실행됨

### 방법 2: 키워드 제거
1. PR 제목에서 `[skip ci]` 제거
2. 새로운 커밋 추가
3. 다시 머지

### 방법 3: 수동 실행
1. GitHub Actions 탭으로 이동
2. 해당 워크플로우 선택
3. "Run workflow" 버튼 클릭

## 📊 실무 활용 팁

### 🎯 권장 사용 패턴

#### 1. **PR 라벨 사용 (가장 효율적)**
```bash
# GitHub CLI로 라벨과 함께 PR 생성
gh pr create --title "docs: API 가이드 추가" --label "skip-ci"
gh pr create --title "ci: 워크플로우 개선" --label "skip-ci"
gh pr create --title "fix: README 수정" --label "skip-ci"
```

#### 2. **커밋 메시지 키워드**
```bash
# 문서 작업
git commit -m "docs: API 가이드 추가 [skip ci]"

# 워크플로우 수정
git commit -m "ci: 빌드 스크립트 개선 [skip ci]"

# README 수정
git commit -m "docs: README 업데이트 [skip ci]"
```

### ⚡ 효율성 개선

- **CI 시간 절약**: 불필요한 테스트/빌드 방지 (3-5분 절약)
- **CD 시간 절약**: 불필요한 APK 빌드/배포 방지 (5-10분 절약)
- **리소스 절약**: GitHub Actions 크레딧 절약
- **명확한 의도**: 스킵 이유가 로그에 표시

## 🔍 스킵 상태 확인

### GitHub Actions에서 확인
1. Actions 탭에서 워크플로우 실행 확인
2. "Check Skip CI" Job에서 스킵 여부 확인
3. 스킵된 Job들이 성공 상태로 표시됨

### CI 스킵 알림 예시
```
🚫 CI가 스킵되었습니다

스킵 이유: PR 라벨에 'skip-ci' 발견

스킵 조건:
- PR 라벨에 'skip-ci' 포함
- PR 제목에 '[skip ci]' 키워드 포함

✅ Required Checks 통과 (모든 CI 작업 스킵됨)
PR 머지가 가능합니다.
```

### CD 스킵 알림 예시
```
🚫 배포가 스킵되었습니다

스킵 이유: PR 라벨에 'skip-ci' 발견

스킵 조건:
- PR 라벨에 'skip-ci' 포함
- PR 제목에 '[skip ci]' 키워드 포함

배포를 원하는 경우:
1. PR 라벨에서 'skip-ci' 제거
2. PR 제목에서 '[skip ci]' 제거
3. 새로운 커밋 추가 후 다시 머지
```

## 🚨 주의사항

1. **스킵된 Job은 "Success" 상태**로 표시됩니다
2. **Required checks**가 모두 통과한 것으로 인식됩니다
3. **실제 코드 변경**이 있는 경우 스킵 키워드를 사용하지 마세요
4. **Hotfix 브랜치**에서는 신중하게 사용하세요
5. **CI 스킵 시에도 PR 머지가 가능**합니다

## 📈 모니터링

### 스킵 통계 확인
- GitHub Actions 사용량 대시보드
- 워크플로우 실행 히스토리
- 빌드 시간 비교

### 최적화 제안
- 자주 스킵되는 패턴 분석
- 팀 컨벤션 정립 