#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd ${DIR}/../

./gradlew client:installApp

if [ $? -eq 0 ]; then
    ./client/build/install/client/bin/client
fi

