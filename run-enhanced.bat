@echo off
echo ================================================
echo  Multi-Format Data Generator - Enhanced Build
echo ================================================
echo.

echo Recent improvements:
echo ✓ Added Reset All button to clear everything
echo ✓ Fixed horizontal scrolling for generator panels
echo ✓ Fixed ASCII generator validation (AAA, BBB, etc.)
echo ✓ Enhanced input validation for all field types
echo.

echo 1. Cleaning...
call gradlew.bat clean >nul 2>&1

echo 2. Building...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo ✗ Build failed
    pause
    exit /b 1
)
echo ✓ Build successful!

echo.
echo 3. Starting enhanced application...
echo.
echo New Features to Test:
echo.
echo 🔄 Reset All Button:
echo    - Red button in controls section
echo    - Clears everything and returns to default state
echo    - Confirms before resetting
echo.
echo ⬅️➡️ Horizontal Scrolling:
echo    - Create 5+ generators and scroll horizontally
echo    - Both horizontal and vertical scrollbars available
echo.
echo ✅ Fixed ASCII Validation:
echo    - Try "AAA" in ASCII generator start field
echo    - Should show green border (valid)
echo    - Also accepts comma-separated lists like "A,B,C"
echo.

call gradlew.bat run

pause
