#!/bin/bash

COMPOSE_FILE="node-exporter.yml"


if [ ! -f "$COMPOSE_FILE" ]; then
    echo "Compose file $COMPOSE_FILE not found"
    exit 1
fi

docker compose -f "$COMPOSE_FILE" up -d
