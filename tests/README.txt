UI-тесты (Playwright + заглушка Firebase). Запуск из папки tests:
  npm i -D playwright && npx playwright install chromium && node run.js
Тесты не проверяют Android-часть (виджеты, иконку, сборку APK): они проверяются шагами GitHub Actions.
