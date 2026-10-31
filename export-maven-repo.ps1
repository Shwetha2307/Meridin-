# Run on a machine where the project ALREADY builds. Produces maven-repo-offline.zip containing every
# dependency, so a machine without access to Maven Central can build offline.
# On the target machine, extract the zip into  %USERPROFILE%\.m2\repository
# then in IntelliJ enable: Settings > Build, Execution, Deployment > Build Tools > Maven > "Work offline".

$repo = Join-Path $env:USERPROFILE ".m2\repository"
$out  = Join-Path $PSScriptRoot "maven-repo-offline.zip"

Write-Host "Making sure every dependency and plugin is cached locally..." -ForegroundColor Cyan
& "$PSScriptRoot\mvnw.cmd" -q dependency:go-offline
if ($LASTEXITCODE -ne 0) { Write-Host "go-offline failed; zipping what is cached anyway." -ForegroundColor Yellow }

Get-ChildItem $repo -Recurse -Include *.lastUpdated -File -ErrorAction SilentlyContinue | Remove-Item -Force
if (Test-Path $out) { Remove-Item $out -Force }

Write-Host "Zipping $repo ..." -ForegroundColor Cyan
Compress-Archive -Path (Join-Path $repo "*") -DestinationPath $out -CompressionLevel Optimal
Write-Host ("Created {0} ({1:N0} MB)" -f $out, ((Get-Item $out).Length / 1MB)) -ForegroundColor Green
