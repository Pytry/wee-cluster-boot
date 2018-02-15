@echo off

echo WARNING! This script will open up to 10 more prompts.
set /p answer="Continue? (y/n):"

IF "%JAVA_HOME%"=="" GOTO needJava
IF "%answer%"=="n" GOTO abort

call mvnw -U clean install

mkdir target
mkdir target\cluster

FOR /L %%i IN (1,1,10) DO (
    mkdir target\cluster\node%%i
    copy simple-cluster\target\simple-cluster.jar target\cluster\node%%i
    start cmd.exe /K java -Dserver.port=%%i -jar target\cluster\node%%i\simple-cluster.jar
)
GOTO end

:needJava
echo The run-cluster script requires 'Java 8' to be installed
:abort
echo Aborting Cluster Startup
:end
echo DONE