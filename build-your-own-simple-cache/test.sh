#!/bin/bash

./build.sh

if [ $? -eq 0 ]; then
    echo "Main module compilation successful. Proceeding to test compilation..."

    rm -rf out/tests
    mkdir -p out/tests

    javac -d out/tests \
        --module-source-path cacheTest=src/test/java \
        --module-path out \
        $(find src/test -name "*.java")

    if [ $? -eq 0 ]; then
        echo "Test module compilation successful. Running the tests..."

        java --module-path out:out/tests -m cacheTest/com.elitekaycy.cache.test.SimpleCacheTest
    else
        echo "Test module compilation failed!"
        exit 1
    fi
else
    echo "Main module compilation failed! Aborting test compilation."
    exit 1
fi

