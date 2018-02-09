@echo off

rmdir /Q /S target

call mvnw -U clean install

mkdir target
mkdir target\cluster

FOR /L %%i IN (8080,1,8089) DO (

    mkdir target\cluster\node%%i
    copy simple-cluster\target\simple-cluster.jar target\cluster\node%%i
    start cmd.exe /K java -Dserver.port=%%i -jar target\cluster\node%%i\simple-cluster.jar
)
