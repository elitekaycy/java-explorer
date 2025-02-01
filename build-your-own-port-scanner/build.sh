#!/bin/bash

# Clean up any previous build output
rm -rf out
mkdir -p out

# Compile the module
javac -d out \
    --module-source-path com.elitekaycy.scanner=src/main/java \
    $(find src/main/java -name "*.java")

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful. Running the module..."
    # Run the compiled module
    java --module-path out -m com.elitekaycy.scanner/com.elitekaycy.scanner.Main "$@"
else
    echo "Compilation failed!"
    exit 1
fi

rm -rf out
