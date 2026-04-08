#!/bin/bash

CURRENT_PATH=`pwd`
APP_NAME=mycuckoo-admin.jar
case "`uname`" in
  Linux)
    BIN_PATH=$(readlink -f $(dirname $0))
    ;;
  *)
    BIN_PATH=`cd $(dirname $0); pwd`
    ;;
esac

BASE_PATH=${BIN_PATH}/..
CONF_PATH=$BASE_PATH/config
APP_JAR=$BASE_PATH/$APP_NAME
LOADER_PATH=$CONF_PATH,$BASE_PATH/lib

export LANG=en_US.UTF-8
export BASE=$BASE_PATH

if [ ! -d $BASE_PATH/logs ] ; then
  mkdir -p $BASE_PATH/logs
fi

## set java path
if [ -z "$JAVA" ] ; then
  JAVA=$(which java)
fi

if [ -z "$JAVA" ] ; then
  echo "Cannot find a Java JDK. Please set either set JAVA or put java (>=17) in your PATH." 2>&2
  exit 1
fi

start() {
  if [ -f $BIN_PATH/cuckoo.pid ] ; then
    echo "Found cuckoo.pid, Please stop first, then start" 2>&2
    exit 1
  fi

  JAVA_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=256m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8"
  MYCUCKOO_OPTS="-Dmycuckoo.config.location=$CONF_PATH -Dspring.config.location=$CONF_PATH\application.yml"

  if [ -e $CONF_PATH ] ; then
    echo LOADER_PATH : $LOADER_PATH
    $JAVA $JAVA_OPTS $JAVA_DEBUG_OPT $MYCUCKOO_OPTS -Dloader.path="$LOADER_PATH" -cp "$APP_JAR" org.springframework.boot.loader.launch.PropertiesLauncher 1>>$BASE_PATH/logs/std.out 2>&1 &
    echo $! > $BIN_PATH/cuckoo.pid
  else
    echo "mycuckoo config "$CONF_PATH" is not exist, please create then first!"
  fi
}


stop() {
  if [ -f $BIN_PATH/cuckoo.pid ] ; then
    echo "====== stopping app: $APP_NAME .... ======"
    kill `cat $BIN_PATH/cuckoo.pid`
    sleep 3

    rm -rf $BIN_PATH/cuckoo.pid
    echo "====== App $APP_NAME was stoped at: "`date '+%Y-%m-%d %T'`"======"
  else
   echo "====== App $APP_NAME is not running ======"
  fi;
}

case "$#" in
  0 )
    echo "Usage: startup.sh {[start]|[stop]|[restart]|[debug]}"
    ;;
  1 )
    var=$*
    if [ "$var" == "start" ] ; then
      start
    elif [ "$var" == "stop" ] ; then
      stop
      sleep 3
    elif [ "$var" == "restart" ] ; then
      stop
      sleep 3

      start
    elif [ "$1" == "debug" ] ; then
      DEBUG_PORT=$2
      DEBUG_SUSPEND="n"
      JAVA_DEBUG_OPT="-agentlib:jdwp=transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=$DEBUG_SUSPEND"

      start
    else
       echo "Usage: startup.sh {[start]|[stop]|[restart]}"
      exit
    fi;;
  2 )
    if [ "$1" == "debug" ] ; then
      DEBUG_PORT=$2
      DEBUG_SUSPEND="n"
      JAVA_DEBUG_OPT="-agentlib:jdwp=transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=$DEBUG_SUSPEND"

      start
    else
      echo "Usage: startup.sh {[start]|[stop]|[restart]|[debug port]}"
      exit
    fi;;
  * )
    echo "Usage: startup.sh {[start]|[stop]|[restart]|[debug port]}"
    exit;;
esac
