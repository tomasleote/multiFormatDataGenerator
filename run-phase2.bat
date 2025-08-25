@echo off
echo ================================================
echo  Multi-Format Data Generator - Phase 2 Enhanced UI
echo ================================================
echo.

echo This will build and run the enhanced Phase 2 UI with:
echo - Modern FlatLaf Dark Theme
echo - Enhanced Layout and Components
echo - Real-time Preview
echo - Progress Indicators
echo - Input Validation
echo - Tooltips and Help System
echo.

echo 1. Cleaning previous build...
call gradlew.bat clean >nul 2>&1
echo    ✓ Clean completed

echo.
echo 2. Building project with new dependencies...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo    ✗ Build failed - check errors above
    echo.
    echo Common issues:
    echo - Network connectivity (downloading FlatLaf dependency)
    echo - Java version compatibility
    pause
    exit /b 1
)
echo    ✓ Build successful

echo.
echo 3. Running Phase 2 Enhanced UI...
echo ================================================
echo.
echo Expected enhancements:
echo ✓ Modern dark theme
echo ✓ Larger, better organized window
echo ✓ Real-time template preview
echo ✓ Progress bars and status indicators
echo ✓ Enhanced input validation with color feedback
echo ✓ Tooltips and help system
echo ✓ Save/Load configuration buttons
echo ✓ Batch size control
echo ✓ Preview generation feature
echo.

call gradlew.bat run

echo.
echo ================================================
echo  Phase 2 UI Session Complete
echo ================================================
pause
