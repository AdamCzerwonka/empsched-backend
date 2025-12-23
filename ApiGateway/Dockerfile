FROM maven:3.9.9-eclipse-temurin-24-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY ApiGateway/pom.xml ./ApiGateway/
COPY AuthService/pom.xml ./AuthService/
COPY CommonShared/pom.xml ./CommonShared/
COPY EmployeeService/pom.xml ./EmployeeService/
COPY EurekaDiscovery/pom.xml ./EurekaDiscovery/
COPY OrganisationService/pom.xml ./OrganisationService/

RUN mvn -B dependency:go-offline

COPY . ./

RUN mvn -B -pl ApiGateway -am clean package

FROM eclipse-temurin:24-alpine AS jre-build

WORKDIR /app

RUN apk update && \
    apk add --no-cache tar binutils

COPY --from=build /app/ApiGateway/target/gateway-1.0-SNAPSHOT.jar api-gateway.jar
RUN jar xvf /app/api-gateway.jar
RUN jdeps --ignore-missing-deps -q \
    --recursive \
    --multi-release 24 \
    --print-module-deps \
    --class-path 'BOOT-INF/lib/*' \
    api-gateway.jar > modules.txt
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
COPY --from=jre-build /app/api-gateway.jar /app/api-gateway.jar

EXPOSE 9001

CMD ["java", "-jar", "/app/api-gateway.jar"]