#!/bin/bash
set -e

rm -f *.zip

rm -rf target

./grailsw compile --non-interactive

./grailsw test-app --non-interactive

./grailsw maven-install --non-interactive

./functional-test-app/run_functional_tests.sh
