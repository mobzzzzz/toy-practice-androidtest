# 🚫 배포 스킵 기능 가이드

문서나 워크플로우 변경 시 불필요한 APK 빌드와 배포를 방지하는 기능입니다.

## 📋 스킵 조건

### 1. **자동 스킵 (paths-ignore)**
다음 파일들만 변경된 경우 자동으로 배포가 스킵됩니다:

```yaml
- 'docs/**'          # 문서 폴더
- '*.md'             # 마크다운 파일
- 'README*'          # README 파일들
- '.gitignore'       # Git 설정
- '.editorconfig'    # 에디터 설정
- '.cursor/**'       # Cursor 설정
```

### 2. **수동 스킵 (키워드)**
PR 제목이나 본문에 다음 키워드를 포함하면 배포가 스킵됩니다:

#### 🔧 CI/CD 스킵
- `[skip ci]` `[ci skip]` `[no ci]`
- `[skip actions]` `[actions skip]`

#### 🚀 배포 스킵
- `[skip deploy]` `[deploy skip]` `[no deploy]`
- `[skip build]` `[build skip]` `[no build]`

#### 📚 문서 작업
- `[docs only]` `[docs]` `[documentation]`

#### ⚙️ 워크플로우 작업
- `[workflow only]` `[workflow]` `[ci only]`

## 💡 사용 예시

### ✅ 배포 스킵되는 경우

```markdown
# PR 제목 예시
docs: API 문서 업데이트 [docs only]
fix: README 오타 수정 [skip ci]
workflow: GitHub Actions 개선 [workflow only]
```

```markdown
# PR 본문 예시
이 PR은 문서만 수정합니다.

[skip deploy] - 배포 불필요
```

### ❌ 배포 진행되는 경우

```markdown
# 일반적인 개발 작업
feat: 새로운 로그인 기능 추가
fix: 버그 수정 및 성능 개선
refactor: 코드 리팩토링
```

## 🔄 배포를 원하는 경우

스킵 조건에 해당하지만 배포가 필요한 경우:

### 방법 1: 키워드 제거
1. PR 제목/본문에서 스킵 키워드 제거
2. 새로운 커밋 추가
3. 다시 머지

### 방법 2: 수동 실행
1. GitHub Actions 탭으로 이동
2. 해당 워크플로우 선택
3. "Run workflow" 버튼 클릭

## 📊 실무 활용 팁

### 🎯 권장 사용 패턴

```bash
# 문서 작업
git commit -m "docs: API 가이드 추가 [docs only]"

# 워크플로우 수정
git commit -m "ci: 빌드 스크립트 개선 [workflow only]"

# README 수정
git commit -m "docs: README 업데이트 [skip ci]"

# 설정 파일 수정
git commit -m "config: .gitignore 업데이트 [no deploy]"
```

### ⚡ 효율성 개선

- **빌드 시간 절약**: 불필요한 APK 빌드 방지
- **리소스 절약**: GitHub Actions 크레딧 절약
- **명확한 의도**: 스킵 이유가 로그에 표시

## 🔍 스킵 상태 확인

### GitHub Actions에서 확인
1. Actions 탭에서 워크플로우 실행 확인
2. "Deployment Skipped" Job 확인
3. 스킵 이유 로그 확인

### 스킵 알림 예시
```
🚫 배포가 스킵되었습니다

스킵 이유: PR 제목에 '[docs only]' 키워드 발견

스킵 조건:
- PR 제목/본문에 스킵 키워드 포함
- 문서나 설정 파일만 변경 (paths-ignore)

배포를 원하는 경우:
1. PR 제목/본문에서 스킵 키워드 제거
2. 새로운 커밋 추가 후 다시 머지
3. 또는 수동으로 workflow_dispatch 실행
```

## 🚨 주의사항

1. **스킵된 Job은 "Success" 상태**로 표시됩니다
2. **Required checks**가 설정된 경우에도 머지가 가능합니다
3. **실제 코드 변경**이 있는 경우 스킵 키워드를 사용하지 마세요
4. **Hotfix 브랜치**에서는 신중하게 사용하세요

## 📈 모니터링

### 스킵 통계 확인
- GitHub Actions 사용량 대시보드
- 워크플로우 실행 히스토리
- 빌드 시간 비교

### 최적화 제안
- 자주 스킵되는 패턴 분석
- paths-ignore 규칙 조정
- 팀 컨벤션 정립 