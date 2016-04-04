#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd ${DIR}/../

if [ -d combined ]; then
    rm -rf combined
fi

mkdir combined

find . -type f \( -name "*.gradle" \) -exec tail -n +1 "$file" {} + > combined/code.txt 2> /dev/null
find . -type f \( -name "*.java" \) -exec tail -n +1 "$file" {} + >> combined/code.txt 2> /dev/null

echo "Done."
