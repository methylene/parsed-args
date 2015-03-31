#!/bin/bash

if [[ ! -d "parsed-args/target/classes" ]] || [[ ! -f "try-parse/target/tp.jar" ]]; then
    mvn -q install -f parsed-args/pom.xml -DskipTests
    mvn -q package -f try-parse/pom.xml -DskipTests
fi

java -cp parsed-args/target/classes:try-parse/target/tp.jar com.github.methylene.args.test.PrintArgs "$@"

if [[ "$?" != "0" ]]; then
    mvn -q clean -f parsed-args/pom.xml
fi
