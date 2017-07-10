#!/usr/bin/env bash

TEMPLATE_FOLDER="functional-test-app"

# static annotation requestmap basic misc bcrypt
TEST_GROUPS="extended simple"

# firefox, htmlUnit, chrome, phantomJs
GEBENV=$1
if [[ $GEBENV = "" ]]; then
    GEBENV=htmlUnit
fi
# /Users/sdelamo/Applications/chromedriver
CHROMEDRIVER=$2
if [[ $CHROMEDRIVER = "" ]]; then
    CHROMEDRIVER=/Users/sdelamo/Applications/chromedriver
fi

# /Users/sdelamo/Applications/phantomjs-2.1.1-macosx/bin/phantomjs
PHANTOMJSDRIVER=$2
if [[ $PHANTOMJSDRIVER = "" ]]; then
    PHANTOMJSDRIVER=/Users/sdelamo/Applications/phantomjs-2.1.1-macosx/bin/phantomjs
fi

echo "GEB environment: $GEBENV"
echo "Chrome driver: $CHROMEDRIVER"
echo "PhantomJS driver: $PHANTOMJSDRIVER"

rm -rf $TEMPLATE_FOLDER/build
rm -rf $TEMPLATE_FOLDER/.gradle

function runTestGroup {
  testGroup=$1
  echo "Running test group $testGroup"
  ./gradlew -q -Dgeb.env=$GEBENV -Dwebdriver.chrome.driver=$CHROMEDRIVER -Dphantomjs.binary.path=$PHANTOMJSDRIVER -DTEST_CONFIG=$testGroup cleanBuild check --stacktrace
}

cd $TEMPLATE_FOLDER
for testGroup in $TEST_GROUPS; do
  runTestGroup $testGroup $grailsVersion
done
cd ..
