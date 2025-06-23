@echo off
:: -----------------------------------------------------------------------------
:: gradlew.bat
::
:: Launches the Gradle build.
::
:: -----------------------------------------------------------------------------

@REM Attempt to set APP_HOME
set PRG=%~0
:findLoop
if exist "%PRG%" (
  for /f "usebackq tokens=*" %%i in (`"%PRG%"`) do (
    set "link=%%i"
  )
) else (
  goto afterLoop
)
if not "%link%"=="" (
  if "%link:~0,1%"=="\" (
    set PRG=%link%
  ) else (
    set PRG=%~dp0%link%
  )
  set "link="
  goto findLoop
)
:afterLoop

set SAVED=%cd%
cd /d "%~dp0%"
for /f "delims=" %%i in ('cd') do set APP_HOME=%%i
cd /d "%SAVED%"

if not defined JAVA_HOME goto findJavaFromPath
set "JAVACMD=%JAVA_HOME%\bin\java.exe"
if not exist "%JAVACMD%" (
  echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
  echo.
  echo Please set JAVA_HOME to a directory containing a Java Runtime Environment.
  goto fail
)
goto execute

:findJavaFromPath
set JAVACMD=java
%JAVACMD% -version >NUL 2>&1
if "%ERRORLEVEL%" NEQ "0" goto fail

:execute
set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
"%JAVACMD%" %JAVA_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
goto end

:fail
echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the location
echo of your Java installation.
:end
