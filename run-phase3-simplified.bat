@echo off
echo ================================================
echo  Phase 3 Simplified Build (No External Dependencies)
echo ================================================
echo.

echo This version uses simplified services without Jackson/CSV dependencies
echo to ensure compatibility and easy building.
echo.

echo 1. Cleaning...
call gradlew.bat clean >nul 2>&1

echo 2. Testing compilation...
call gradlew.bat compileJava
if %errorlevel% neq 0 (
    echo ✗ Compilation failed
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
echo 4. Starting Phase 3 Simplified Application...
echo.
echo Features available:
echo ✓ Modern UI with Phase 2 enhancements
echo ✓ Data generation with all generator types
echo ✓ Export to CSV, TXT, and JSON (simplified)
echo ✓ Configuration save/load (text format)
echo ✓ All Phase 1 & 2 functionality
echo.

call gradlew.bat run

pause
