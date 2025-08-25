@echo off
echo ================================================
echo  Phase 2 Fallback Test (No FlatLaf)
echo ================================================
echo.

echo This will test Phase 2 enhancements without FlatLaf theme
echo to ensure basic functionality works.
echo.

echo Cleaning...
call gradlew.bat clean >nul 2>&1

echo Building...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo ✗ Build failed
    pause
    exit /b 1
)

echo ✓ Build successful!
echo.
echo Running with fallback main class...
java -cp "build/classes/java/main;build/libs/*" MainFallback

pause
