#!/bin/bash
# Compile J.A.V.A Application (Linux/Mac)

echo "Compiling J.A.V.A Application..."
echo

# Check if MySQL Connector JAR exists
if [ ! -f "mysql-connector-j-8.0.28.jar" ]; then
    echo "ERROR: MySQL Connector JAR not found!"
    echo "Please download mysql-connector-j-8.0.28.jar and place it in this directory."
    echo "Download from: https://dev.mysql.com/downloads/connector/j/"
    exit 1
fi

# Compile all Java files with MySQL Connector in classpath
javac -cp .:mysql-connector-j-8.0.28.jar User.java Task.java DatabaseManager.java VolunteerApp.java

if [ $? -eq 0 ]; then
    echo
    echo "Compilation successful!"
    echo "Run './run.sh' to start the application."
else
    echo
    echo "Compilation failed! Please check the errors above."
    exit 1
fi
