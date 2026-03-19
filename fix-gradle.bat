@echo off
echo Fixing Gradle issues...
echo.

echo 1. Killing any running Gradle processes...
taskkill /F /IM gradle.exe 2>nul
taskkill /F /IM java.exe 2>nul

echo 2. Clearing Gradle cache...
if exist "%USERPROFILE%\.gradle" (
    echo Clearing Gradle cache from %USERPROFILE%\.gradle
    rmdir /s /q "%USERPROFILE%\.gradle\caches" 2>nul
    rmdir /s /q "%USERPROFILE%\.gradle\wrapper" 2>nul
)

echo 3. Cleaning project...
call gradlew clean --no-daemon --refresh-dependencies

echo.
echo Gradle issues fixed! Now you can rebuild the project.
echo.
pause
