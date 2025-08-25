@echo off
echo Cleaning and rebuilding Multi-Format Data Generator...
echo.

echo 1. Cleaning previous build...
call gradlew.bat clean
if %errorlevel% neq 0 (
    echo ERROR: Clean failed
    pause
    exit /b 1
)
echo Clean successful!
echo.

echo 2. Building project...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo ERROR: Build failed
    echo Please check the error messages above
    pause
    exit /b 1
)
echo Build successful!
echo.

echo 3. Running application...
call gradlew.bat run

pause
