UI-тесты (Playwright + заглушка Firebase). Запуск из папки tests:
  npm i -D playwright && npx playwright install chromium && node run.js
Для новых версий есть ещё run106.js, run107.js и run108.js (node run108.js). Тесты не проверяют Android-часть (виджеты, иконку, сборку APK): они проверяются шагами GitHub Actions.
