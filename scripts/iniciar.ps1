$ErrorActionPreference = 'Stop'
$raiz = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$ferramentas = Join-Path $raiz '.ferramentas'
$versaoMaven = '3.10.0'
$modo = 'mysql'

function Mensagem([string]$texto) { Write-Host "[EstoqueWeb] $texto" -ForegroundColor Cyan }
function Parar([string]$texto) { throw $texto }
function PortaAberta([int]$porta) {
    $cliente = New-Object Net.Sockets.TcpClient
    try {
        $tentativa = $cliente.BeginConnect('127.0.0.1', $porta, $null, $null)
        if (-not $tentativa.AsyncWaitHandle.WaitOne(750)) { return $false }
        $cliente.EndConnect($tentativa)
        return $true
    } catch { return $false }
    finally { $cliente.Dispose() }
}
function JdkValido([string]$caminho) {
    if (-not $caminho) { return $false }
    $javac = Join-Path $caminho 'bin\javac.exe'
    $java = Join-Path $caminho 'bin\java.exe'
    if (-not (Test-Path $javac) -or -not (Test-Path $java)) { return $false }
    try {
        $saida = (& $javac -version 2>&1 | Out-String)
        return ($saida -match 'javac\s+(\d+)' -and [int]$matches[1] -ge 17)
    } catch { return $false }
}
function BuscarJdk {
    $pastas = New-Object System.Collections.Generic.List[string]
    if ($env:JAVA_HOME) { $pastas.Add($env:JAVA_HOME) }
    if (Test-Path $ferramentas) {
        Get-ChildItem $ferramentas -Directory -ErrorAction SilentlyContinue | Where-Object { $_.Name -like 'jdk-*' } | ForEach-Object { $pastas.Add($_.FullName) }
    }
    foreach ($candidato in @("$env:ProgramFiles\Eclipse Adoptium", "$env:ProgramFiles\Java", "$env:LOCALAPPDATA\Programs\Eclipse Adoptium", "$env:USERPROFILE\.jdks")) {
        if ($candidato -and (Test-Path $candidato)) {
            Get-ChildItem $candidato -Directory -ErrorAction SilentlyContinue | ForEach-Object { $pastas.Add($_.FullName) }
        }
    }
    $comando = Get-Command javac.exe -ErrorAction SilentlyContinue
    if ($comando) { $pastas.Add((Split-Path (Split-Path $comando.Source -Parent) -Parent)) }
    foreach ($pasta in ($pastas | Select-Object -Unique)) { if (JdkValido $pasta) { return $pasta } }
    return $null
}
function Baixar([string]$url, [string]$destino) {
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $url -OutFile $destino -UseBasicParsing -MaximumRedirection 10
}
function PrepararJava {
    Mensagem 'Verificando JDK 17 ou superior...'
    $jdk = BuscarJdk
    if (-not $jdk) {
        Mensagem 'JDK nao encontrado. Tentando obter uma copia portatil...'
        New-Item -ItemType Directory -Path $ferramentas -Force | Out-Null
        $endpoint = 'https://api.adoptium.net/v3/assets/latest/17/hotspot?architecture=x64&image_type=jdk&os=windows&vendor=eclipse'
        $pacotes = @(Invoke-RestMethod -Uri $endpoint -Method Get)
        if ($pacotes.Count -lt 1 -or -not $pacotes[0].binary.package.link -or -not $pacotes[0].binary.package.checksum) { Parar 'Nao foi possivel obter os dados para baixar o JDK 17.' }
        $pacote = $pacotes[0].binary.package
        $zip = Join-Path $ferramentas 'jdk17.zip'
        Baixar $pacote.link $zip
        if ((Get-FileHash $zip -Algorithm SHA256).Hash -ne $pacote.checksum.ToUpperInvariant()) { Remove-Item $zip -Force; Parar 'Falha na verificacao SHA256 do Java.' }
        Expand-Archive -LiteralPath $zip -DestinationPath $ferramentas -Force
        Remove-Item $zip -Force
        $jdk = BuscarJdk
    }
    if (-not $jdk) { Parar 'JDK 17 nao localizado. Configure o Java manualmente.' }
    $env:JAVA_HOME = $jdk
    $env:Path = (Join-Path $jdk 'bin') + ';' + $env:Path
    Mensagem "Java: $jdk"
}
function BuscarMaven {
    $comando = Get-Command mvn.cmd -ErrorAction SilentlyContinue
    if ($comando) { return $comando.Source }
    foreach ($pasta in @((Join-Path $ferramentas "apache-maven-$versaoMaven\bin\mvn.cmd"), "C:\apache-maven-$versaoMaven\bin\mvn.cmd", "E:\apache-maven-$versaoMaven\bin\mvn.cmd")) {
        if (Test-Path $pasta) { return $pasta }
    }
    return $null
}
function PrepararMaven {
    Mensagem 'Verificando Maven...'
    $maven = BuscarMaven
    if (-not $maven) {
        Mensagem 'Maven nao encontrado. Tentando obter uma copia portatil...'
        New-Item -ItemType Directory -Path $ferramentas -Force | Out-Null
        $nome = "apache-maven-$versaoMaven-bin.zip"
        $url = "https://archive.apache.org/dist/maven/maven-3/$versaoMaven/binaries/$nome"
        $zip = Join-Path $ferramentas $nome
        $arquivoHash = "$zip.sha512"
        Baixar $url $zip
        Baixar "$url.sha512" $arquivoHash
        $textoHash = Get-Content -Raw $arquivoHash
        if ($textoHash -notmatch '([a-fA-F0-9]{128})') { Parar 'Checksum do Maven nao encontrado.' }
        $esperado = $matches[1].ToUpperInvariant()
        if ((Get-FileHash $zip -Algorithm SHA512).Hash -ne $esperado) { Parar 'Falha na verificacao SHA512 do Maven.' }
        Expand-Archive -LiteralPath $zip -DestinationPath $ferramentas -Force
        Remove-Item $zip, $arquivoHash -Force
        $maven = BuscarMaven
    }
    if (-not $maven) { Parar 'Maven nao encontrado.' }
    Mensagem "Maven: $maven"
    return $maven
}
function SolicitarSenha {
    $segura = Read-Host 'Senha do usuario MySQL' -AsSecureString
    $endereco = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($segura)
    try { return [Runtime.InteropServices.Marshal]::PtrToStringBSTR($endereco) }
    finally { [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($endereco) }
}
try {
    Set-Location $raiz
    Write-Host '============================================' -ForegroundColor DarkCyan
    Write-Host '                 ESTOQUEWEB' -ForegroundColor DarkCyan
    Write-Host '============================================' -ForegroundColor DarkCyan
    if (-not (Test-Path (Join-Path $raiz 'pom.xml'))) { Parar 'pom.xml nao encontrado. Extraia o ZIP completo.' }
    if (PortaAberta 8080) { Parar 'Porta 8080 ocupada. Encerre o processo que ja utiliza esta porta.' }
    if ($modo -eq 'mysql') {
        Mensagem 'Conferindo MySQL em 127.0.0.1:3306...'
        if (-not (PortaAberta 3306)) { Parar 'MySQL indisponivel em 127.0.0.1:3306. Inicie o servidor MySQL antes de executar.' }
        $usuario = Read-Host 'Usuario MySQL [root]'
        if ([string]::IsNullOrWhiteSpace($usuario)) { $usuario = 'root' }
        $env:DB_USER = $usuario
        $env:DB_PASSWORD = SolicitarSenha
    } else {
        New-Item -ItemType Directory -Path (Join-Path $raiz 'dados') -Force | Out-Null
        Mensagem 'Banco H2 em arquivo: dados\estoqueweb.mv.db'
    }
    $env:SPRING_PROFILES_ACTIVE = $modo
    PrepararJava
    $mvn = PrepararMaven
    Mensagem 'Iniciando aplicacao...'
    Write-Host 'Endereco: http://localhost:8080' -ForegroundColor Green
    Write-Host 'Para encerrar: Ctrl+C nesta janela.' -ForegroundColor Yellow
    Write-Host 'A janela continua aberta durante o uso. Isto e normal.' -ForegroundColor Yellow
    $abridor = Join-Path $PSScriptRoot 'abrir-navegador.ps1'
    try {
        Start-Process -FilePath 'powershell.exe' -ArgumentList @('-NoProfile', '-ExecutionPolicy', 'Bypass', '-WindowStyle', 'Hidden', '-File', "`"$abridor`"") -WindowStyle Hidden | Out-Null
    } catch { Write-Host 'Abra o navegador manualmente em http://localhost:8080.' -ForegroundColor Yellow }
    & $mvn 'spring-boot:run'
    if ($LASTEXITCODE -ne 0) { Parar 'Falha ao iniciar a aplicacao. Consulte as mensagens acima.' }
} catch {
    Write-Host "`nERRO: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host 'Consulte README.md para resolucao de problemas.' -ForegroundColor Yellow
} finally {
    Read-Host 'Pressione Enter para fechar' | Out-Null
}
