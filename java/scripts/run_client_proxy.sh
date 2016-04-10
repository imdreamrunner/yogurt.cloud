#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd ${DIR}/../

./gradlew client:installApp

./client/build/install/client/bin/client 127.0.0.1 3001
