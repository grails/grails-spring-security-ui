#!/usr/bin/env bash

set -e

rm -rf build

./gradlew -q clean check install --stacktrace

# functional-test-app/run_functional_tests.sh

if [[ $TRAVIS_BRANCH == 'master' && $TRAVIS_PULL_REQUEST == 'false' ]]; then

    if [[ -n $TRAVIS_TAG ]]; then

        ./gradlew bintrayUpload --stacktrace

        ./publish-docs.sh

    fi

fi
