#!/bin/bash
# Проверка готового APK: иконка, версия, виджеты, разрешения.
set -euo pipefail
APK=android/app/build/outputs/apk/debug/app-debug.apk
test -f "$APK"
AAPT=$(ls "$ANDROID_HOME"/build-tools/*/aapt2 | sort -V | tail -1)
"$AAPT" dump badging "$APK" | grep -E "^package|application-label:|versionName" | head -5
"$AAPT" dump badging "$APK" | grep -q "application-icon" || { echo "::error::нет иконки приложения в APK"; exit 1; }
unzip -l "$APK" | grep -q "ic_launcher" || { echo "::error::нет ресурсов ic_launcher в APK"; exit 1; }
"$AAPT" dump xmltree --file AndroidManifest.xml "$APK" > /tmp/manifest.txt
for w in ChatsWidget NotesWidget DeadlineWidget QuickWidget ChatsService NotesService DeadlinesService appwidget.provider POST_NOTIFICATIONS; do
  grep -q "$w" /tmp/manifest.txt || { echo "::error::в манифесте APK нет $w"; exit 1; }
done
echo "APK проверен: иконка, версия, 4 виджета, разрешения"
