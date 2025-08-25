@echo off
echo Fixing compilation errors in Load Example functionality...
echo.
echo Fixed Issues:
echo 1. Corrected complex array syntax in loadComplexExample method
echo 2. Simplified configuration approach for better Java compatibility
echo 3. Individual generator configuration instead of complex nested arrays
echo.
echo Building project...
call gradlew clean build -x test --console=plain
if %ERRORLEVEL% neq 0 (
    echo Build failed! Check error output above.
    pause
    exit /b 1
)

echo.
echo Build successful! Testing Load Example functionality...
echo.
echo Features to test:
echo 1. Dutch BSN - Single generator with validation formula
echo 2. License Plate - Two generators (letters + numbers)  
echo 3. Complex Multi-Gen - Six generators using all four types
echo 4. All examples should load instantly and be ready to generate
echo.
call gradlew run --console=plain
pause
