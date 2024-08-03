@echo off
@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT"  setlocal

chcp 65001
title MyCuckoo

set ENV_PATH=.\
if "%OS%" == "Windows_NT" set ENV_PATH=%~dp0%

set CONF_DIR=%ENV_PATH%..\config
set CLASSPATH=%CONF_DIR%

set JAVA_MEM_OPTS= -Xms128m -Xmx512m -XX:MetaspaceSize=128m
set JAVA_OPTS_EXT= -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dapplication.codeset=UTF-8 -Dfile.encoding=UTF-8
set JAVA_DEBUG_OPT= -server -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=9099,server=y,suspend=n
set MYCUCKOO_OPTS= -Dmycuckoo.config.location=%CONF_DIR% -Dspring.config.location=%CONF_DIR%\application.yml

set JAVA_OPTS= %JAVA_MEM_OPTS% %JAVA_OPTS_EXT% %JAVA_DEBUG_OPT% %MYCUCKOO_OPTS%

set CMD_STR= java %JAVA_OPTS% -classpath %CLASSPATH% -jar mycuckoo-admin.jar
echo start cmd : %CMD_STR%

java %JAVA_OPTS% -classpath %CLASSPATH% -jar mycuckoo-admin.jar