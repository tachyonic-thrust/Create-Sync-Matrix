#!/usr/bin/env sh

# Simplified Gradle wrapper script. Mirrors the default upstream wrapper but
# trimmed for brevity because we cannot download it automatically in this
# environment.

APP_HOME=$(cd "$(dirname "$0")" && pwd -P)
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
MAIN_CLASS=org.gradle.wrapper.GradleWrapperMain

if [ -n "$JAVA_HOME" ] ; then
  JAVA_EXEC="$JAVA_HOME/bin/java"
else
  JAVA_EXEC="java"
fi

exec "$JAVA_EXEC" -classpath "$CLASSPATH" $MAIN_CLASS "$@"
