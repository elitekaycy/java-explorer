#!/bin/bash

if [ $# -eq 0 ]; then
  echo "Usage: $0 [arguments...]"
  echo "Example: $0 arg1 arg2 arg3"
  exit 1
fi

ARGS=""
for ARG in "$@"; do
  ARGS+="${ARG//\"/\\\"} "
done

ARGS=$(echo "$ARGS" | sed 's/ *$//')

echo "Running Gradle with arguments: $ARGS"

./gradlew cmdLineJavaExec -Pargs="$ARGS"

