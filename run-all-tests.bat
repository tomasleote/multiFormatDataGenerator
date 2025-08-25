@echo off
echo ================================================
echo  Multi-Format Data Generator - Test Suite
echo ================================================
echo.

echo Running comprehensive test suite...
echo This will test both UI components and core functionality.
echo.

echo 1. Cleaning previous build...
call gradlew.bat clean >nul 2>&1
echo    ✓ Clean completed

echo.
echo 2. Building project...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo    ✗ Build failed - check errors above
    pause
    exit /b 1
)
echo    ✓ Build successful

echo.
echo 3. Running UI tests...
echo ================================================
call gradlew.bat test --tests "com.view.GeneratorUITest"
if %errorlevel% neq 0 (
    echo    ✗ UI tests failed
) else (
    echo    ✓ UI tests passed
)

echo.
echo 4. Running integration tests...
echo ================================================
call gradlew.bat test --tests "com.controller.generators.MainGeneratorIntegrationTest"
if %errorlevel% neq 0 (
    echo    ✗ Integration tests failed
) else (
    echo    ✓ Integration tests passed
)

echo.
echo 5. Running all existing tests...
echo ================================================
call gradlew.bat test
if %errorlevel% neq 0 (
    echo    ✗ Some tests failed
) else (
    echo    ✓ All tests passed
)

echo.
echo ================================================
echo  Test Suite Complete
echo ================================================
echo.
echo Check the detailed output above for test results.
echo If all tests passed, your Phase 1 implementation is working correctly!
echo.

pause
