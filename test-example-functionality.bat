@echo off
echo Adding Load Example functionality...
echo.
echo New Features Added:
echo 1. Load Example area next to Export Options
echo 2. Three example buttons:
echo    - Dutch BSN: Generates valid Dutch BSN numbers with validation
echo    - License Plate: Generates ABC-123 format license plates  
echo    - Complex Multi-Gen: Uses all 4 generator types with 6 generators
echo 3. One-click loading of pre-configured examples
echo 4. Ready to generate immediately after loading
echo.
echo Building project...
call gradlew clean build -x test --console=plain
if %ERRORLEVEL% neq 0 (
    echo Build failed! Check for compilation errors.
    pause
    exit /b 1
)

echo.
echo Build successful! Testing Load Example functionality...
echo.
echo How to test:
echo 1. Look for "Load Example" panel next to "Export Options"
echo 2. Click "Dutch BSN" - should load BSN generator with validation
echo 3. Click "License Plate" - should load 2 generators (letters + numbers)
echo 4. Click "Complex Multi-Gen" - should load 6 generators using all types
echo 5. Each example should be ready to generate immediately
echo.
call gradlew run --console=plain
pause
