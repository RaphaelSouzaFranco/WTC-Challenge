@REM Maven Wrapper Windows Batch Script
@REM Licensed to the Apache Software Foundation (ASF)

@IF "%__MVNW_ARG0_NAME__%"=="" (SET "BASE_DIR=%~dp0")

@SET MAVEN_PROJECTBASEDIR=%BASE_DIR%
@SET WRAPPER_JAR=%BASE_DIR%.mvn\wrapper\maven-wrapper.jar
@SET WRAPPER_PROPERTIES=%BASE_DIR%.mvn\wrapper\maven-wrapper.properties

@FOR /F "usebackq tokens=1,2 delims==" %%a IN ("%WRAPPER_PROPERTIES%") DO (
  @IF "%%a"=="distributionUrl" (SET "DISTRIBUTION_URL=%%b")
  @IF "%%a"=="wrapperUrl" (SET "WRAPPER_URL=%%b")
)

@IF NOT EXIST "%WRAPPER_JAR%" (
  @ECHO Downloading Maven Wrapper...
  @powershell -Command "& {Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%'}"
)

@"%JAVA_HOME%\bin\java.exe" ^
  %MAVEN_OPTS% ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  -jar "%WRAPPER_JAR%" %*

@IF ERRORLEVEL 1 GOTO error
@GOTO end

:error
@EXIT /B 1

:end
@EXIT /B 0
