#!/bin/sh

set -x

DUID=${HOST_UID:0}
DGID=${HOST_GID:0}

getent group $DGID
if [ $? -eq 0 ];then
    echo "gid: $DGID already exists in container, continue ..."
else
    echo "gid: $DGID does not exist, creating group ..."
    addgroup -g $DGID weihu
fi

getent passwd $DUID
if [ $? -eq 0 ];then
    echo "uid: $DUID already exists in container, continue ..."
else
    echo "uid: $DUID does not exist, creating user ..."
    adduser -D -H -u $DUID -G weihu weihu
fi

exec /usr/bin/gosu $DUID:$DGID java  $JAVA_OPTS -jar /@project.build.finalName@.jar  $SPRING_OPTS
