@echo off
echo ================================================
echo  Quick Build Test - Phase 2 Fixes
echo ================================================
echo.

echo Cleaning...
call gradlew.bat clean >nul 2>&1

echo Testing compilation...
call gradlew.bat compileJava
if %errorlevel% neq 0 (
    echo.
    echo ✗ Compilation still failed. Let's check what's wrong:
    echo.
    echo Possible issues:
    echo 1. FlatLaf dependency not downloaded (network issue)
    echo 2. Java version compatibility
    echo 3. Missing imports
    echo.
    echo Let's try a minimal build first...
    pause
    exit /b 1
)

echo ✓ Compilation successful!
echo.
echo Running full build...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo ✗ Build failed
    pause
    exit /b 1
)

echo ✓ Build successful!
echo.
echo Starting application...
call gradlew.bat run

pause
