#!/bin/zsh

#!/bin/zsh

check_device() {
  local devices=$(adb devices | sed '1d' | awk '$2 == "device" || $2 == "emulator"')
  
  if [ -z "$devices" ]; then
    echo "\n연결된 안드로이드 디바이스나 에뮬레이터가 없습니다.\n"
    echo "사용 가능한 에뮬레이터 목록:"
    echo "----------------------------------------"
    $HOME/Library/Android/sdk/emulator/emulator -list-avds
    echo "----------------------------------------\n"
    echo "다음 방법 중 하나를 선택하세요:\n"
    echo "[에뮬레이터 실행 방법]"
    echo "터미널에서 원하는 기기를 선택해 다음 명령어 중 하나를 실행:"
    
    # 각 에뮬레이터에 대한 실행 명령어 출력
    while IFS= read -r avd_name; do
      echo "\$HOME/Library/Android/sdk/emulator/emulator -avd $avd_name"
    done < <($HOME/Library/Android/sdk/emulator/emulator -list-avds)
    echo ""
    
    echo "[실제 디바이스 연결 방법]"
    echo "1. USB 디버깅이 활성화된 안드로이드 기기를 USB로 연결"
    echo "2. \"이 컴퓨터에서 USB 디버깅을 허용\" 메시지가 표시되면 허용\n"
    return 1
  fi
  return 0
}

get_application_id() {
  # app/build.gradle.kts에서 applicationId를 추출
  local app_id=$(grep "applicationId" app/build.gradle.kts | awk -F '"' '{print $2}')
  echo "$app_id"
}

main() {
  check_device || exit 1

  # applicationId 가져오기
  local app_id=$(get_application_id)
  
  echo '디바이스가 연결되어 있습니다. 빌드를 진행합니다.'
  ./gradlew assembleDebug && \
  adb install -r app/build/outputs/apk/debug/app-debug.apk && \
  adb shell monkey -p "$app_id" -c android.intent.category.LAUNCHER 1
}

main "$@"