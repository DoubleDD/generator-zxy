FROM docker.zhixueyun.com:5000/jdk:v8u101

ENV MAX_HEAP 2048m
ENV MIN_HEAP 1024m

EXPOSE 8080

RUN mkdir /work
WORKDIR /work

RUN mkdir /log
VOLUME /log

ENV PROJECT <%= appName %>
ADD ./${PROJECT}-*.jar /work/

CMD java -jar -Xms${MIN_HEAP} -Xmx${MAX_HEAP} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/log ${PROJECT}-*.jar \
    > /log/${PROJECT}-`date +%m%d%H%M`.log  2> /log/${PROJECT}-error-`date +%m%d%H%M`.log