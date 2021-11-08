FROM jdk:1.8

COPY gosu/gosu-amd64 /usr/bin/gosu
RUN chmod +x /usr/bin/gosu

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

COPY @project.build.finalName@.jar @project.build.finalName@.jar

ENTRYPOINT ["/entrypoint.sh"]
#ENTRYPOINT java $JAVA_OPTS -jar /@project.build.finalName@.jar $SPRING_OPTS