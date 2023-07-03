#!/usr/bin/env bash

find . -name target -type d | xargs rm -rf

echo "Running initial compile..."
init="$(SBT_DEBUG=1 sbt compile 2>&1)"

if [ "$?" != '0' ]; then
  printf "Initial compile failed! Output:\n\n$init\n\n"
  exit 1
fi

echo "Modifying Queries.scala..."
file=src/main/scala/database/Queries.scala

if grep -qE 's"AVG\(' $file; then
  sed -i'' -E 's/s"AVG\(/s"AVG2(/' $file
else
  sed -i'' -E 's/s"AVG2\(/s"AVG(/' $file
fi

echo "Running incremental compile..."
out=$(SBT_DEBUG=1 sbt -Dsbt.color=always compile 2>&1)

if echo "$out" | grep -qE 'compiling 3 Scala sources'; then
  set +e
  apiDiff="$(echo "$out" | grep -E '\[diff\].*\[32m')"
  printf "\n\nTest failed! All sources were recompiled. API diff:\n\n$apiDiff\n\n"
  exit 1
else
  printf "Test succeeded!"
fi
