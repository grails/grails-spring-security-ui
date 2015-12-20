#!/bin/bash
set -e

rm -rf build

./gradlew -q clean check install --stacktrace

functional-test-app/run_functional_tests.sh
