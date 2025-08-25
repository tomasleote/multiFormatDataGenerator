@echo off
echo Applying final UI polish fixes...
echo.
echo Changes made:
echo 1. Darker grey dropdown/resize buttons (RGB 160,160,160) to blend better
echo 2. More precise scrollable area calculation - no more oversized scroll area
echo 3. Dynamic container sizing based on actual number of generator rows
echo 4. Reduced initial container size, grows as needed
echo.
echo Building project...
call gradlew clean build -x test --console=plain
if %ERRORLEVEL% neq 0 (
    echo Build failed! Check for compilation errors.
    pause
    exit /b 1
)

echo.
echo Build successful! Testing UI improvements...
echo.
echo What to look for:
echo - Resize buttons should be darker grey and blend better
echo - Scrollbar area should be just slightly bigger than needed
echo - No more excessive white space in scroll area
echo - Try template like {0}{1}{2}{3}{4}{5} to test multiple rows
echo.
call gradlew run --console=plain
pause
