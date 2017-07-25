#!/bin/bash

# PATH, JAR, and Version should probably not be hardcoded...
[ "$BASE_PATH" ] || BASE_PATH=/usr/local/sbin/rpmdemo/webapp
[ "$JAR_FILE" ] || JAR_FILE=SpringBootRpmDemo-0.0.1.jar
[ "$SPRING_PROFILES_ACTIVE" ] || export SPRING_PROFILES_ACTIVE=prod

# we *should* verify JAVA_HOME, if $JAVA_HOME/bin/java exists, etc.
# but if it doesn't it will error out anyway

cd $BASE_PATH
java -jar $JAR_FILE
