FROM alpine:3.22.1

LABEL "maintainer"="LuCray <ali.samet.ar@gmail.com>"
LABEL "description"="LuCray's Bankweb App"


RUN apk update && apk add openjdk17


ARG APP=app.jar
ENV APPDIR /app
WORKDIR /${APPDIR}




USER root


COPY target/bankwebapp-0.0.1-SNAPSHOT.jar ${APP}

ENTRYPOINT ["java","-jar","app.jar"]