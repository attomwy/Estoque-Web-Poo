param([string]$Url = 'http://localhost:8080')
$ErrorActionPreference = 'SilentlyContinue'
for ($i = 0; $i -lt 120; $i++) {
    try {
        $r = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 2
        if ($r.StatusCode -ge 200 -and $r.StatusCode -lt 400) {
            Start-Process $Url
            exit 0
        }
    } catch { }
    Start-Sleep -Seconds 1
}
exit 1
