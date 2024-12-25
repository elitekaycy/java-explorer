#!/bin/bash

rm -rf out
mkdir -p out

javac -d out \
    --module-source-path cache=src/main/java \
    $(find src/main/java -name "*.java")

if [ $? -eq 0 ]; then
    echo "Compilation successful. Running the module..."
    java --module-path out -m cache/com.elitekaycy.cache.SimpleCache
else
    echo "Compilation failed!"
    exit 1
fi

