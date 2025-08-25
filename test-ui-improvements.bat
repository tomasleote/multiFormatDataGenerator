@echo off
echo Testing UI Improvements...
echo.
echo Building project...
call gradlew clean build -x test --console=plain
if %ERRORLEVEL% neq 0 (
    echo Build failed! Check for compilation errors.
    pause
    exit /b 1
)

echo.
echo Build successful! Starting UI...
call gradlew run --console=plain
pause
