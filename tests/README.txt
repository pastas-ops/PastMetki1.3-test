UI-тесты (Playwright + заглушка Firebase). Запуск из папки tests:
  npm i -D playwright && npx playwright install chromium && node run.js
Для версии 1.0.6 есть ещё run106.js (node run106.js). Тесты не проверяют Android-часть (виджеты, иконку, сборку APK): они проверяются шагами GitHub Actions.
