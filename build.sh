#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p target/classes
find src/main/java -name '*.java' > target/sources.list
javac --release 21 -encoding UTF-8 -d target/classes @target/sources.list
jar --create --file target/poc-delivery-1.0.0.jar --main-class supermercado.Main -C target/classes .
printf 'JAR criado em target/poc-delivery-1.0.0.jar\n'
