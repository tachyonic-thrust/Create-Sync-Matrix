@echo off
set DIR=%~dp0
set APP_HOME=%DIR:~0,-1%
set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
set MAIN_CLASS=org.gradle.wrapper.GradleWrapperMain

if not defined JAVA_HOME (
  set JAVA_EXE=java
) else (
  set JAVA_EXE=%JAVA_HOME%\bin\java.exe
)

"%JAVA_EXE%" -classpath "%CLASSPATH%" %MAIN_CLASS% %*
