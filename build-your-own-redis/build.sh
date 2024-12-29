#!/bin/bash

rm -rf out

cd "$(dirname "$0")/src/main/java/com/elitekaycy/redis"

javac -d ../../../../../out *.java

cd ../../../../../
java -cp out Main

