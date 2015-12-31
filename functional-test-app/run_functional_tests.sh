#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"

set -e

# doesn't work with 3.1 yet because the compiled domain class references org.grails.datastore.gorm.GormEntity in org.grails:grails-datastore-gorm-support and this results in a NoClassDefFoundError at startup
# GRAILS_VERSIONS="3.0.11 3.1.0.RC1"
GRAILS_VERSIONS="3.0.11"
TEST_GROUPS="simple extended"

rm -rf build

function generateBuildGradle {
	testGroup=$1
	grailsVersion=$2

	rm -f build.gradle
	echo -e "$(<gradle/buildscript.inc)\n" >> build.gradle

	if [[ $grailsVersion =~ 3\.0\..+ ]]; then
		echo "$(<gradle/spring_dependency_management.inc)" >> build.gradle

		echo -e "\napply plugin: 'spring-boot'\n" >> build.gradle
	fi

	echo -e "apply from: '../gradle/testapp.gradle'" >> build.gradle

	echo -e "\ndependencies {" >> build.gradle
	echo -e "\tcompile 'dumbster:dumbster:1.6', { transitive = false }" >> build.gradle
	echo -e "\tcompile 'org.grails.plugins:mail:2.0.0.RC4'" >> build.gradle
	if [[ $testGroup = 'extended' ]]; then
		echo -e "\tcompile 'org.grails.plugins:spring-security-acl:3.0.1'" >> build.gradle
	fi
	if [[ $grailsVersion =~ 3\.0\..+ ]]; then
		echo -e "\tcompile 'org.grails.plugins:hibernate'" >> build.gradle
	else
		echo -e "\tcompile 'org.grails.plugins:hibernate4'" >> build.gradle
		echo -e "\tcompile 'org.grails:grails-core'" >> build.gradle
		echo -e "\tprofile 'org.grails.profiles:web:$grailsVersion'" >> build.gradle
	fi
	echo "}" >> build.gradle

	echo "$(<gradle/integrationTest.inc)" >> build.gradle
}

function runTestGroup {
	testGroup=$1
	grailsVersion=$2

	echo $testGroup > testconfig

	_grailsVersion=${grailsVersion//\./_}

	echo grailsVersion=$grailsVersion > gradle.properties
	cp gradle.properties gradle-$_grailsVersion.properties

	generateBuildGradle $testGroup $grailsVersion
	cp build.gradle build-$testGroup-$_grailsVersion.gradle

	./gradlew -q cleanBuild check --stacktrace

	mv build/reports/tests build/reports/tests-$testGroup-$_grailsVersion
	mv build/geb-reports build/geb-reports-$testGroup-$_grailsVersion
	mv build/test-results build/test-results-$testGroup-$_grailsVersion
}

for grailsVersion in $GRAILS_VERSIONS; do

	rm -rf .gradle

	for testGroup in $TEST_GROUPS; do
		runTestGroup $testGroup $grailsVersion
	done

done
