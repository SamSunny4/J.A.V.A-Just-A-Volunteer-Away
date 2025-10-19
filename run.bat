@echo off
echo Starting J.A.V.A Application...
echo.

REM Check if classes exist
if not exist "target\classes\VolunteerGUI.class" (
    echo Classes not found! Please run compile.bat first.
    pause
    exit /b 1
)

REM Run the GUI application
java -cp "target\classes;mysql-connector-j-9.4.0.jar" VolunteerGUI

pause
