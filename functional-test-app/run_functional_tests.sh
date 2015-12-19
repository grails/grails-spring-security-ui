#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"

set -e

CONFIGS="simple extended"

source ~/.sdkman/bin/sdkman-init.sh
sdk use grails 2.4.5

function runTests {
	config=$1

	echo $config > testconfig

	rm -rf target

	grails compile --non-interactive

	grails test-app --non-interactive -functional

	mv target/test-reports target/test-reports-$config
}

for config in $CONFIGS; do
	runTests $config
done
