#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X (modified to remove -d64)
##
##############################################################################

# Attempt to set APP_HOME
PRG="$0"
# need this for relative symlinks
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> .*$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

# Remove any default JVM opts entirely (we don’t want -d64)
DEFAULT_JVM_OPTS=""

# Detect which java to use
if [ -n "$JAVA_HOME" ] ; then
  JAVACMD="$JAVA_HOME/bin/java"
  if [ ! -x "$JAVACMD" ] ; then
    echo "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    echo "Please set JAVA_HOME to a directory containing a Java Runtime Environment."
    exit 1
  fi
else
  JAVACMD="java"
  which java >/dev/null 2>&1 || {
    echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
    echo "Please set the JAVA_HOME variable in your environment to match the location of your Java installation."
    exit 1
  }
fi

# Determine the Java launcher to use. If JAVA_HOME is set and valid, we use that.
if [ -n "$JAVA_HOME" ] ; then
  if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
    JAVACMD="$JAVA_HOME/jre/sh/java"
  else
    JAVACMD="$JAVA_HOME/bin/java"
  fi
fi

CLASSJAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$CLASSJAR" ] ; then
  echo "ERROR: Could not find gradle wrapper jar at $CLASSJAR"
  echo "Make sure the file is present or try regenerating the wrapper."
  exit 1
fi

exec "$JAVACMD" $JAVA_OPTS -classpath "$CLASSJAR" org.gradle.wrapper.GradleWrapperMain "$@"
