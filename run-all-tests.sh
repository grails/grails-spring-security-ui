#!/usr/bin/env bash

set -e

rm -rf build

./gradlew -q clean check install --stacktrace

./run_functional_tests.sh
