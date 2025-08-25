@echo off
echo Testing Multi-Format Data Generator Setup...
echo.

echo 1. Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java not found or not properly installed
    echo Please install Java JDK 11 or higher
    pause
    exit /b 1
)
echo Java OK!
echo.

echo 2. Checking Gradle wrapper...
if exist gradlew.bat (
    echo Gradle wrapper found
) else (
    echo ERROR: Gradle wrapper not found
    pause
    exit /b 1
)
echo.

echo 3. Building project...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo ERROR: Build failed
    echo Please check the error messages above
    pause
    exit /b 1
)
echo Build successful!
echo.

echo 4. Running application...
call gradlew.bat run

pause
