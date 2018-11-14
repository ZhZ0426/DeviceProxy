#!/bin/sh

APP=ProxyClient
psid=0
RUNNING_USER=root
APP_NAME=ProxyClient.jar
checkpid() {
   javaps=`jps -l | grep $APP`

   if [ -n "$javaps" ]; then
      psid=`echo $javaps | awk '{print $1}'`
   else
      psid=0
   fi
}
start() {
   checkpid

   if [ $psid -ne 0 ]; then
      echo "================================"
      echo "warn: $APP already started! (pid=$psid)"
      echo "================================"
   else
      GW_ID=`ifconfig eth0.2|grep HWaddr|awk '{print $5}'`
      GWID=`echo "$GW_ID" | sed -r 's/://g'`
      echo -n "Starting $APP ..."
      nohup java -jar $APP_NAME $GWID >/dev/null 2>&1 &
      checkpid
      if [ $psid -ne 0 ]; then
         echo "(pid=$psid) [OK]"
      else
         echo "[Failed]"
      fi
   fi
}
stop() {
   checkpid

   if [ $psid -ne 0 ]; then
      echo -n "Stopping $APP ...(pid=$psid) "
      su - $RUNNING_USER -c "kill -9 $psid"
      if [ $? -eq 0 ]; then
         echo "[OK]"
      else
         echo "[Failed]"
      fi

      checkpid
      if [ $psid -ne 0 ]; then
         stop
      fi
   else
      echo "================================"
      echo "warn: $APP is not running"
      echo "================================"
   fi
}
status() {
   checkpid

   if [ $psid -ne 0 ];  then
      echo "$APP is running! (pid=$psid)"
   else
      echo "$APP is not running"
   fi
}
info() {
   echo "System Information:"
   echo "****************************"
   echo `head -n 1 /etc/issue`
   echo `uname -a`
   echo `java -version`
   echo "APP_MAINCLASS=$APP"
   echo "****************************"
}
case "$1" in
   'start')
      start
      ;;
   'stop')
     stop
     ;;
   'restart')
     stop
     start
     ;;
   'status')
     status
     ;;
   'info')
     info
     ;;
  *)
     echo "Usage: $0 {start|stop|restart|status|info}"
     exit 1
esac
exit 0
