FROM maven:3.9.9-eclipse-temurin-24-alpine AS build

WORKDIR /app

COPY . ./

RUN mvn -B -pl AuthService -am clean package

FROM eclipse-temurin:24-alpine AS jre-build

WORKDIR /app

RUN apk update && \
    apk add --no-cache tar binutils

COPY --from=build /app/AuthService/target/auth-1.0-SNAPSHOT.jar auth-service.jar
RUN jar xvf /app/auth-service.jar
RUN jdeps --ignore-missing-deps -q \
    --recursive \
    --multi-release 24 \
    --print-module-deps \
    --class-path 'BOOT-INF/lib/*' \
    auth-service.jar > modules.txt
RUN jlink \
    --add-modules $(cat modules.txt) \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /app/jre

FROM alpine:3.21
ENV JAVA_HOME=/opt/jdk/jdk-24
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY --from=jre-build /app/jre ${JAVA_HOME}
COPY --from=jre-build /app/auth-service.jar /app/auth-service.jar

EXPOSE 9001

CMD ["java", "-jar", "/app/auth-service.jar"]