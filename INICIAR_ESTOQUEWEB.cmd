@echo off
setlocal
cd /d "%~dp0"
title EstoqueWeb
if not exist "%~dp0pom.xml" (
  echo ERRO: Extraia o ZIP completo antes de iniciar.
  pause
  exit /b 1
)
if not exist "%~dp0scripts\iniciar.ps1" (
  echo ERRO: Inicializador nao encontrado.
  pause
  exit /b 1
)
start "EstoqueWeb" powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\iniciar.ps1"
