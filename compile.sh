#!/bin/bash

echo "Compiling J.A.V.A Application..."
echo

# Create classes directory if it doesn't exist
mkdir -p target/classes

# Compile all Java files
javac -d target/classes -cp ".:mysql-connector-j-8.0.33.jar" *.java

if [ $? -eq 0 ]; then
    echo
    echo "Compilation successful!"
    echo "Run './run.sh' to start the application."
    echo
else
    echo
    echo "Compilation failed!"
    echo
    exit 1
fi
