@echo off
REM Run J.A.V.A Application (Windows)

echo Starting J.A.V.A Application...
echo.

REM Check if compiled classes exist
if not exist "VolunteerApp.class" (
    echo ERROR: Application not compiled!
    echo Please run 'compile.bat' first.
    pause
    exit /b 1
)

REM Check if MySQL Connector JAR exists
if not exist "mysql-connector-j-9.4.0.jar" (
    echo ERROR: MySQL Connector JAR not found!
    echo Please download mysql-connector-j-9.4.0.jar and place it in this directory.
    pause
    exit /b 1
)

REM Run the application with MySQL Connector in classpath
java -cp .;mysql-connector-j-9.4.0.jar VolunteerApp

pause
