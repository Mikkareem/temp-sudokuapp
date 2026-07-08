#!/usr/bin/env sh

set -eu

: "${GRADLE_DISTRIBUTION_URL:?GRADLE_DISTRIBUTION_URL environment variable must be set}"

TEMPLATE="gradle/wrapper/gradle-wrapper.properties.template"
TARGET="gradle/wrapper/gradle-wrapper.properties"

[ -f "$TEMPLATE" ] || {
    echo "Template file not found: $TEMPLATE" >&2
    exit 1
}

sed "s|@GRADLE_DISTRIBUTION_URL@|$GRADLE_DISTRIBUTION_URL|g" \
    "$TEMPLATE" > "$TARGET"
