@echo off
echo ================================================
echo  Testing Phase 2 Build Fix
echo ================================================
echo.

echo Cleaning previous build...
call gradlew.bat clean >nul 2>&1

echo.
echo Building with fixes...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo ✗ Build still failed. Checking for remaining issues...
    echo.
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo ✓ Build successful!
echo.
echo Running the application...
call gradlew.bat run

pause
