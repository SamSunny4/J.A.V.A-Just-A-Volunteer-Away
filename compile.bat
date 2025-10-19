@echo off
echo Compiling J.A.V.A Application...
echo.

REM Create classes directory if it doesn't exist
if not exist "target\classes" mkdir target\classes

REM Compile all Java files
javac -d target\classes -cp ".;mysql-connector-j-9.4.0.jar" *.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful!
    echo Run 'run.bat' to start the application.
    echo.
) else (
    echo.
    echo Compilation failed!
    echo.
)

pause
