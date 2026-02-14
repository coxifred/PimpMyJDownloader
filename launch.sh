#!/bin/sh -x


ID=$(id -u)
if [ "$ID" != "0" ]
 then
  echo "Must be root"
  exit 1
fi

while true
 do
  date
   chmod 777 tmp/extensioncache/extensionInfos.json
   cp tmp/extensioncache/extensionInfos.fred tmp/extensioncache/extensionInfos.json
   cd /opt/jd2/extensions
   ln -s PimpMyJDownloader-1.0.jar PimpMyJDownloader.jar 2>/dev/null
   # Clean rm -rf /opt/jd2/jd/captcha/methods/*
   echo "Cleaning rm -rf /opt/jd2/jd/captcha/methods/*"
   rm -rf /opt/jd2/jd/captcha/methods/* 2>/dev/null
   pkill -9 -f "JDownloader2"
   ./JDownloader2 &
   sleep 120
   PRESENT=$(pgrep -f "JDownloader2")
   while [ ! -z "$PRESENT" ]
    do
      echo "Sleep 200"
      sleep 200
      PRESENT=$(pgrep -f "JDownloader2")
      echo "Check if localhost:8080 is available"
      wget http://localhost:8080 -O /tmp/test >/dev/null 2>&1
      if [ "$?" -ne 0 ]
       then
         echo "No more online kill JDownloader"
         pkill -9 -f "JDownloader2"
      fi
    done
 done
