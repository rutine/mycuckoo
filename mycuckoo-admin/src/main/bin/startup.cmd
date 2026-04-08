@echo off
@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT"  setlocal

chcp 65001
title MyCuckoo

set ENV_PATH=.\
if "%OS%" == "Windows_NT" set ENV_PATH=%~dp0%

cd /d "%~dp0.."
set APP_HOME=%cd%
set CONF_DIR=%APP_HOME%
set CONF_DIR=%CONF_DIR%\config
set APP_JAR=%APP_HOME%\mycuckoo-admin.jar
set LOADER_PATH=%CONF_DIR%,%APP_HOME%\lib

where java >nul 2>nul
if errorlevel 1 (
  echo Cannot find Java. Please put Java ^(>=17^) in PATH.
  exit /b 1
)

set JAVA_MEM_OPTS= -Xms128m -Xmx512m -XX:MetaspaceSize=128m
set JAVA_OPTS_EXT= -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dapplication.codeset=UTF-8 -Dfile.encoding=UTF-8
set JAVA_DEBUG_OPT= -agentlib:jdwp=transport=dt_socket,address=9099,server=y,suspend=n
set MYCUCKOO_OPTS= -Dmycuckoo.config.location="%CONF_DIR%" -Dspring.config.location="%CONF_DIR%\application.yml"

set JAVA_OPTS= %JAVA_MEM_OPTS% %JAVA_OPTS_EXT% %JAVA_DEBUG_OPT% %MYCUCKOO_OPTS%

set CMD_STR= java %JAVA_OPTS% -Dloader.path="%LOADER_PATH%" -cp "%APP_JAR%" org.springframework.boot.loader.launch.PropertiesLauncher
echo start cmd : %CMD_STR%

java %JAVA_OPTS% -Dloader.path="%LOADER_PATH%" -cp "%APP_JAR%" org.springframework.boot.loader.launch.PropertiesLauncher
