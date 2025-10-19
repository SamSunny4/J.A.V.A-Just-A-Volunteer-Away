@echo off
REM Compile J.A.V.A Application (Windows)

echo Compiling J.A.V.A Application...
echo.

REM Check if MySQL Connector JAR exists
if not exist "mysql-connector-j-9.4.0.jar" (
    echo ERROR: MySQL Connector JAR not found!
    echo Please download mysql-connector-j-9.4.0.jar and place it in this directory.
    echo Download from: https://dev.mysql.com/downloads/connector/j/
    pause
    exit /b 1
)

REM Compile all Java files with MySQL Connector in classpath
javac -cp .;mysql-connector-j-9.4.0.jar User.java Task.java DatabaseManager.java VolunteerApp.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful!
    echo Run 'run.bat' to start the application.
) else (
    echo.
    echo Compilation failed! Please check the errors above.
)

pause
