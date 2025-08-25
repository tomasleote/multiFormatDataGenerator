@echo off
echo Fixing Load Example issues...
echo.
echo Issues Fixed:
echo 1. DUPLICATE EVALUATORS: Fixed BSN and Complex examples creating extra evaluators
echo 2. COMPLEX INTERDEPENDENCIES: Enhanced Complex example with generator chains:
echo    - Gen 0: Base number (step 2) 
echo    - Gen 1: ASCII letters (A,B,C,D,E)
echo    - Gen 2: Calculation using Gen 0 (A * 3 + 50)
echo    - Gen 3: Sequential using Gen 2 as input
echo    - Gen 4: Complex calc using Gen 0, 2, and 3 (A + C + D)
echo    - Gen 5: ASCII using Gen 4 as input
echo    - Evaluator 1: Even numbers only (A %% 2 == 0)
echo    - Evaluator 2: Sum constraint (A + C + E ^< 1000)
echo.
echo Building project...
call gradlew clean build -x test --console=plain
if %ERRORLEVEL% neq 0 (
    echo Build failed! Check error output above.
    pause
    exit /b 1
)

echo.
echo Build successful! Testing fixed Load Examples...
echo.
echo Test Instructions:
echo 1. Click "Dutch BSN" - should show 1 generator + 1 evaluator (no duplicates)
echo 2. Click "License Plate" - should show 2 generators (letters + numbers)  
echo 3. Click "Complex Multi-Gen" - should show 6 interconnected generators + 2 evaluators
echo    * Notice how generators use previous generators as input
echo    * Complex formulas using multiple generator values
echo.
call gradlew run --console=plain
pause
