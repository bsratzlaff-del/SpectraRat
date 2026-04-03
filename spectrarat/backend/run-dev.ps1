# Spectrarat Development Runner
Write-Host "Spectrarat Development Server" -ForegroundColor Cyan
Write-Host "=============================" -ForegroundColor Cyan
Write-Host ""

# Kill any existing processes using port 8082
Write-Host "Checking for processes using port 8082..." -ForegroundColor Yellow
$processes = netstat -ano | findstr ":8082" | findstr "LISTENING"
if ($processes) {
    $processes | ForEach-Object {
        $pid = ($_ -split '\s+')[-1]
        Write-Host "Found process $pid using port 8082, terminating..." -ForegroundColor Red
        taskkill /PID $pid /F >$null 2>&1
        Start-Sleep -Seconds 2
    }
}

Write-Host "Starting Spectrarat Application in Development Mode..." -ForegroundColor Green
Write-Host "Port: 8082" -ForegroundColor Yellow
Write-Host "Profile: dev (default)" -ForegroundColor Yellow
Write-Host ""
Write-Host "Press Ctrl+C to stop the application gracefully" -ForegroundColor Cyan
Write-Host ""

# Run the application (dev profile is default)
.\mvnw.cmd spring-boot:run