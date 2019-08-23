FROM leandrocruz/base-image-java:v11.0

ADD lib  /opt/inpd/service/lib
ADD bin  /opt/inpd/service/bin
ADD conf /opt/inpd/service/conf

RUN mkdir -p /etc/service/inpd-service
ADD run      /etc/service/inpd-service/run

