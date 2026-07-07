param(
    [int]$BackendPort = 8080,
    [int]$FrontendPort = 5173
)

$ErrorActionPreference = "Stop"
$Root = $PSScriptRoot
$Frontend = Join-Path $Root "frontend"

if (-not (Test-Path (Join-Path $Frontend "node_modules"))) {
    Write-Host "Installing frontend dependencies..."
    Push-Location $Frontend
    npm install
    Pop-Location
}

Write-Host "Starting Spring Boot API on http://localhost:$BackendPort"
$backend = Start-Job -Name "flight-booking-api" -ScriptBlock {
    param($ProjectRoot)
    Set-Location $ProjectRoot
    .\mvnw.cmd spring-boot:run
} -ArgumentList $Root

Write-Host "Starting React app on http://localhost:$FrontendPort"
$frontend = Start-Job -Name "flight-booking-react" -ScriptBlock {
    param($FrontendRoot, $ApiBaseUrl, $Port)
    Set-Location $FrontendRoot
    $env:VITE_API_BASE_URL = $ApiBaseUrl
    npm run dev -- --port $Port
} -ArgumentList $Frontend, "http://localhost:$BackendPort", $FrontendPort

try {
    Write-Host ""
    Write-Host "Both servers are starting. Open http://localhost:$FrontendPort"
    Write-Host "Press Ctrl+C to stop both servers."
    while ($true) {
        Receive-Job $backend, $frontend
        Start-Sleep -Seconds 1
    }
}
finally {
    Stop-Job $backend, $frontend -ErrorAction SilentlyContinue
    Remove-Job $backend, $frontend -Force -ErrorAction SilentlyContinue
}
