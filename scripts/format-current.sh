#!/bin/bash

# 현재 파일의 절대 경로를 저장
current_file="$1"

# 현재 파일만 포맷팅
./gradlew spotlessApply -PspotlessTarget="$current_file"