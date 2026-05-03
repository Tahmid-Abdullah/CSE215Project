$ErrorActionPreference = "Stop"

$workspace = (Resolve-Path -LiteralPath ".").Path
$outputDir = Join-Path $workspace ".vscode\bin"
$resourceOutputDir = Join-Path $outputDir "transfermarket\UI"
$javaFxLib = Join-Path $workspace "javafx-lib"
$javac = "C:\Program Files\Java\jdk-24\bin\javac.exe"

if (-not (Test-Path -LiteralPath $javac)) {
    throw "javac was not found at $javac"
}

if (-not (Test-Path -LiteralPath $javaFxLib)) {
    throw "JavaFX library folder was not found at $javaFxLib"
}

$javaFiles = Get-ChildItem -Path (Join-Path $workspace "transfermarket") -Recurse -File -Filter "*.java" |
    ForEach-Object { $_.FullName }

if (-not $javaFiles -or $javaFiles.Count -eq 0) {
    throw "No Java source files were found under transfermarket"
}

New-Item -ItemType Directory -Path $outputDir -Force | Out-Null

& $javac `
    --module-path $javaFxLib `
    --add-modules javafx.controls,javafx.fxml `
    -d $outputDir `
    $javaFiles

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

New-Item -ItemType Directory -Path $resourceOutputDir -Force | Out-Null
Copy-Item -Path (Join-Path $workspace "transfermarket\UI\*.fxml") -Destination $resourceOutputDir -Force
