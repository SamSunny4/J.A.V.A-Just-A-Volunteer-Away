#!/bin/bash

echo "Starting J.A.V.A Application..."
echo

# Check if classes exist
if [ ! -f "target/classes/VolunteerGUI.class" ]; then
    echo "Classes not found! Please run compile.sh first."
    exit 1
fi

# Run the GUI application
java -cp "target/classes:mysql-connector-j-8.0.33.jar" VolunteerGUI
