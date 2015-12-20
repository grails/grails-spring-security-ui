#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"

set -e

CONFIGS="simple extended"

function runTests {
	config=$1

	echo $config > testconfig

	rm -rf target

	./grailsw compile --non-interactive

	./grailsw test-app --non-interactive -functional

	mv target/test-reports target/test-reports-$config
}

for config in $CONFIGS; do
	runTests $config
done
