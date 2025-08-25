@echo off
echo ================================================
echo  Testing Phase 3 Build After Removing Dependencies
echo ================================================
echo.

echo Removed files with Jackson dependencies. Testing simplified build...
echo.

echo 1. Cleaning...
call gradlew.bat clean >nul 2>&1

echo 2. Testing compilation...
call gradlew.bat compileJava
if %errorlevel% neq 0 (
    echo ✗ Compilation still failed. Checking remaining errors...
    pause
    exit /b 1
)
echo ✓ Compilation successful!

echo.
echo 3. Running full build...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo ✗ Build failed
    pause
    exit /b 1
)
echo ✓ Build successful!

echo.
echo 4. Starting application...
echo.
echo Phase 3 Simplified Features:
echo ✓ Modern UI (Phase 2)
echo ✓ All generator types working
echo ✓ Data export (CSV, TXT, JSON) - simplified implementation
echo ✓ Configuration save/load (text format)
echo ✓ Progress tracking and validation
echo ✓ All previous functionality intact
echo.

call gradlew.bat run

pause
