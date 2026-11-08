UI-тесты (Playwright + заглушка Firebase). Запуск из папки tests:
  npm i -D playwright && npx playwright install chromium && node run.js
Для новых версий есть ещё run106.js ... run109.js (node run109.js). Запускайте по одному. Тесты не проверяют Android-часть (виджеты, иконку, сборку APK): они проверяются шагами GitHub Actions.
