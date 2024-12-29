#!/bin/bash

MAIN_CLASS_FILE="Main.java"
SRC_DIR="src/main/java/com/elitekaycy/wc"
FULL_PATH="$SRC_DIR/$MAIN_CLASS_FILE"

if [ ! -f "$FULL_PATH" ]; then
    echo "Error: $MAIN_CLASS_FILE does not exist at $FULL_PATH."
    exit 1
fi

JAVA_ARGS="$@"

java "$FULL_PATH" $JAVA_ARGS

