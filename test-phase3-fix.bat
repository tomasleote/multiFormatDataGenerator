@echo off
echo ================================================
echo  Phase 3 Build Fix Test
echo ================================================
echo.

echo Testing compilation fixes...
echo.

echo 1. Cleaning...
call gradlew.bat clean >nul 2>&1

echo 2. Testing compilation...
call gradlew.bat compileJava
if %errorlevel% neq 0 (
    echo.
    echo ✗ Compilation failed. Please check errors above.
    echo.
    echo Common remaining issues might be:
    echo - Missing dependencies (Jackson, Commons CSV)
    echo - Network connectivity issues
    echo - Java version compatibility
    echo.
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

echo 4. Starting Phase 3 application...
call gradlew.bat run

pause
