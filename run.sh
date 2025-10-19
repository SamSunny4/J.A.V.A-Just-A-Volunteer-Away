#!/bin/bash
# Run J.A.V.A Application (Linux/Mac)

echo "Starting J.A.V.A Application..."
echo

# Check if compiled classes exist
if [ ! -f "VolunteerApp.class" ]; then
    echo "ERROR: Application not compiled!"
    echo "Please run './compile.sh' first."
    exit 1
fi

# Check if MySQL Connector JAR exists
if [ ! -f "mysql-connector-j-8.0.28.jar" ]; then
    echo "ERROR: MySQL Connector JAR not found!"
    echo "Please download mysql-connector-j-8.0.28.jar and place it in this directory."
    exit 1
fi

# Run the application with MySQL Connector in classpath
java -cp .:mysql-connector-j-8.0.28.jar VolunteerApp
