#!/usr/bin/env bash

set -e
echo "TRAVIS_TAG          : $TRAVIS_TAG"
echo "TRAVIS_BRANCH       : $TRAVIS_BRANCH"
echo "TRAVIS_PULL_REQUEST : $TRAVIS_PULL_REQUEST"
echo "Publishing archives for branch $TRAVIS_BRANCH"
rm -rf build

EXIT_STATUS=0

./gradlew -q clean check install --stacktrace || EXIT_STATUS=$?

if (( $EXIT_STATUS > 0))
then
  exit $EXIT_STATUS
fi

# Only publish if the branch is on master, and it is not a PR
if [[ -n $TRAVIS_TAG ]] || [[ $TRAVIS_BRANCH == '3.0.x' && $TRAVIS_PULL_REQUEST == 'false' ]]; then
  echo "Publishing archives for branch $TRAVIS_BRANCH"
  if [[ -n $TRAVIS_TAG ]]; then
      echo "Pushing build to Bintray"
      ./gradlew :spring-security-ui:bintrayUpload || EXIT_STATUS=$?
  else
      echo "Publishing snapshot to OJO"
      ./gradlew :spring-security-ui:artifactoryPublish || EXIT_STATUS=$?
  fi

  if (( $EXIT_STATUS > 0))
  then
     exit $EXIT_STATUS
  fi

  ./gradlew :docs:docs || EXIT_STATUS=$?

  if (( $EXIT_STATUS > 0))
  then
     exit $EXIT_STATUS
  fi

  git config --global user.name "$GIT_NAME"
  git config --global user.email "$GIT_EMAIL"
  git config --global credential.helper "store --file=~/.git-credentials"
  echo "https://$GH_TOKEN:@github.com" > ~/.git-credentials

  git clone https://${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git -b gh-pages gh-pages --single-branch > /dev/null
  cd gh-pages

  # If this is the master branch then update the snapshot
  if [[ $TRAVIS_BRANCH == 'master' ]]; then

    mkdir -p snapshot
    cp -r ../docs/build/docs/. ./snapshot/
    git add snapshot/*

  fi

  # If there is a tag present then this becomes the latest
  if [[ -n $TRAVIS_TAG ]]; then
        # git rm -rf latest/
        # mkdir -p latest
        # cp -r ../docs/build/docs/. ./latest/
        # git add latest/*

        version="$TRAVIS_TAG" # eg: v3.0.1
        version=${version:1} # 3.0.1
        majorVersion=${version:0:4} # 3.0.
        majorVersion="${majorVersion}x" # 3.0.x

        mkdir -p "$version"
        cp -r ../docs/build/docs/. "./$version/"
        git add "$version/*"

        git rm -rf "$majorVersion"
        cp -r ../docs/build/docs/. "./$majorVersion/"
        git add "$majorVersion/*"
  fi

  if [[ -n $TRAVIS_TAG ]] || [[ $TRAVIS_BRANCH == 'master' ]]; then
      git commit -a -m "Updating docs for Travis build: https://travis-ci.org/$TRAVIS_REPO_SLUG/builds/$TRAVIS_BUILD_ID"
      git push origin HEAD
  fi

  cd ..
  rm -rf gh-pages
fi

exit $EXIT_STATUS