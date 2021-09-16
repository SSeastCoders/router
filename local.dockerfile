
# stage 1 copy dependencies from cache and build project
FROM maven:3.8.2-openjdk-11 as builder
WORKDIR /app
COPY src /app/src
COPY pom.xml /app/pom.xml
RUN mvn clean install -DskipTests

FROM openjdk:11-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
ADD https://raw.githubusercontent.com/eficode/wait-for/v2.1.3/wait-for /wait-for
RUN chmod +x /wait-for
RUN apt-get -q update && apt-get -qy install netcat
CMD [ "/wait-for", "mysql:3307", "-t", "300", "--", "java", "-jar", "/app/app.jar" ]