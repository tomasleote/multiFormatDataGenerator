@echo off
echo Applying UI fixes for generator panels...
echo.
echo Changes made:
echo 1. Fixed background color to match UI (grey instead of white)
echo 2. Made resize buttons more visible on each generator panel
echo 3. Improved scrolling to show all generators properly
echo 4. Enhanced panel height adjustment functionality
echo.
echo Building project...
call gradlew clean build -x test --console=plain
if %ERRORLEVEL% neq 0 (
    echo Build failed! Check for compilation errors.
    pause
    exit /b 1
)

echo.
echo Build successful! Starting UI to test fixes...
echo.
echo Instructions:
echo - Look for the up/down arrow buttons in the top-right of each generator panel
echo - Click them to expand/collapse individual panels
echo - Try creating 4+ generators to test the scrolling
echo - Background should now be grey like the rest of the UI
echo.
call gradlew run --console=plain
pause
