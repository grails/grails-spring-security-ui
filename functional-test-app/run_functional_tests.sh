#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"

set -e

CONFIGS="simple extended"

rm -rf target

function runTests {
	config=$1

	echo $config > testconfig

	rm -f target/*.log
	rm -f target/web.xml.tmp
	rm -rf target/*classes
	rm -rf target/plugins
	rm -rf target/resources
	rm -rf target/tomcat

	./grailsw compile --non-interactive

	./grailsw test-app --non-interactive -functional

	mv target/test-reports target/test-reports-$config
}

for config in $CONFIGS; do
	runTests $config
done
