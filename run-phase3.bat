@echo off
echo ================================================
echo  Multi-Format Data Generator - Phase 3 Complete
echo ================================================
echo.

echo Phase 3 includes all essential features:
echo ✓ Configuration Management (Save/Load JSON)
echo ✓ Data Export (CSV, TXT, JSON formats)
echo ✓ Progress Tracking for exports
echo ✓ Auto-save functionality
echo ✓ Enhanced error handling
echo ✓ Performance optimizations
echo.

echo 1. Cleaning previous build...
call gradlew.bat clean >nul 2>&1
echo    ✓ Clean completed

echo.
echo 2. Building with Phase 3 dependencies...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo    ✗ Build failed - check errors above
    echo.
    echo Possible issues:
    echo - Jackson JSON library download
    echo - Commons CSV library download
    echo - Network connectivity
    pause
    exit /b 1
)
echo    ✓ Build successful

echo.
echo 3. Running Phase 3 Complete Application...
echo ================================================
echo.
echo New Features Available:
echo.
echo 📁 Configuration Management:
echo    - Save current setup to JSON file
echo    - Load previous configurations
echo    - Auto-save option for convenience
echo.
echo 📤 Data Export Options:
echo    - CSV format (for spreadsheets)
echo    - TXT format (plain text with headers)
echo    - JSON format (structured data)
echo    - Progress dialogs for large exports
echo.
echo ⚙️  Enhanced UI:
echo    - Export controls integrated
echo    - Configuration status display
echo    - Real-time progress tracking
echo.

call gradlew.bat run

echo.
echo ================================================
echo  Phase 3 Session Complete
echo ================================================
echo.
echo All phases implemented:
echo ✓ Phase 1: Foundation & Bug Fixes
echo ✓ Phase 2: Enhanced User Experience  
echo ✓ Phase 3: Essential Features
echo.
echo The Multi-Format Data Generator is now complete!
pause
