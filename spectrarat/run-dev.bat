@echo off
echo Spectrarat Development Server
echo =============================
echo.

REM Kill any existing Java processes using port 8082
echo Checking for processes using port 8082...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8082 ^| findstr LISTENING') do (
    echo Found process %%a using port 8082, terminating...
    taskkill /PID %%a /F >nul 2>&1
    timeout /t 2 /nobreak >nul
)

echo Starting Spectrarat Application in Development Mode...
echo Port: 8082
echo Profile: dev (default)
echo.
echo Press Ctrl+C to stop the application gracefully
echo.
.\mvnw.cmd spring-boot:run