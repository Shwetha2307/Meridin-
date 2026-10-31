# Diagnoses Maven connectivity, clears cached download failures, and re-downloads all dependencies.
# Usage (from the project folder):  powershell -ExecutionPolicy Bypass -File .\fix-maven.ps1

$ErrorActionPreference = "Continue"
$repo = Join-Path $env:USERPROFILE ".m2\repository"

Write-Host "`n[1/3] Checking connection to Maven Central..." -ForegroundColor Cyan
try {
    Resolve-DnsName repo.maven.apache.org -ErrorAction Stop | Out-Null
    $resp = Invoke-WebRequest "https://repo.maven.apache.org/maven2/" -UseBasicParsing -TimeoutSec 15
    Write-Host "      OK - Maven Central reachable (HTTP $($resp.StatusCode))" -ForegroundColor Green
} catch {
    Write-Host "      FAILED - $($_.Exception.Message)" -ForegroundColor Red
    Write-Host @"

      This machine cannot reach https://repo.maven.apache.org. Fix ONE of:
        a) Connect to the internet / disconnect a VPN that blocks it, or switch networks (e.g. mobile hotspot).
        b) Corporate network: configure your proxy in IntelliJ
             File > Settings > Appearance & Behavior > System Settings > HTTP Proxy
           and copy maven-settings-template.xml to $env:USERPROFILE\.m2\settings.xml with your proxy details.
        c) No internet at all: on a working machine run .\export-maven-repo.ps1, copy the zip here,
           and extract it to $env:USERPROFILE\.m2\repository

"@ -ForegroundColor Yellow
    exit 1
}

Write-Host "[2/3] Clearing cached download failures (*.lastUpdated)..." -ForegroundColor Cyan
if (Test-Path $repo) {
    $stale = Get-ChildItem $repo -Recurse -Include *.lastUpdated, _remote.repositories.lastUpdated -File -ErrorAction SilentlyContinue
    $stale | Remove-Item -Force -ErrorAction SilentlyContinue
    Write-Host "      Removed $($stale.Count) cached failure marker(s)" -ForegroundColor Green
} else {
    Write-Host "      No local repository yet - nothing to clear" -ForegroundColor Green
}

Write-Host "[3/3] Downloading all dependencies (forced update)..." -ForegroundColor Cyan
& "$PSScriptRoot\mvnw.cmd" -U -q dependency:resolve dependency:resolve-plugins
if ($LASTEXITCODE -eq 0) {
    Write-Host "`nDone. In IntelliJ open the Maven tool window and click 'Reload All Maven Projects'." -ForegroundColor Green
} else {
    Write-Host "`nDependency download failed - see output above." -ForegroundColor Red
}
