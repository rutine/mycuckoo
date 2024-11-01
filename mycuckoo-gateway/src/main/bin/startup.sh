#!/bin/bash

CURRENT_PATH=`pwd`
APP_NAME=mycuckoo-gateway.jar
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
  echo "Cannot find a Java JDK. Please set either set JAVA or put java (>=1.8) in your PATH." 2>&2
  exit 1
fi

start() {
  if [ -f $BIN_PATH/cuckoo.pid ] ; then
    echo "Found cuckoo.pid, Please stop first, then start" 2>&2
    exit 1
  fi

  str=`file -L $JAVA | grep 64-bit`
  if [ -n "$str" ]; then
    JAVA_OPTS="-server -Xms256m -Xmx512m -Xmn256m -XX:SurvivorRatio=2 -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=256m -Xss256k -XX:-UseAdaptiveSizePolicy -XX:MaxTenuringThreshold=15 -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError"
  else
    JAVA_OPTS="-server -Xms256m -Xmx512m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:MaxMetaspaceSize=128m "
  fi

  JAVA_OPTS=" $JAVA_OPTS -Dfile.encoding=UTF-8"
  MYCUCKOO_OPTS="-Dmycuckoo.config.location=$CONF_PATH -Dspring.config.location=$CONF_PATH\bootstrap.yml"

  if [ -e $CONF_PATH ] ; then
    CLASSPATH="$CONF_PATH:$CLASSPATH";

    echo CLASSPATH : $CLASSPATH
    $JAVA $JAVA_OPTS $JAVA_DEBUG_OPT $MYCUCKOO_OPTS -classpath .:$CLASSPATH -jar $BASE_PATH/$APP_NAME 1>>$BASE_PATH/logs/std.out 2>&1 &
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
    echo "Usage: server.sh {[start]|[stop]|[restart]|[debug]}"
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
      JAVA_DEBUG_OPT="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=$DEBUG_SUSPEND"

      start
    else
       echo "Usage: server.sh {[start]|[stop]|[restart]}"
      exit
    fi;;
  * )
    echo "Usage: server.sh {[start]|[stop]|[restart]|[debug]}"
    exit;;
esac