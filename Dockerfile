FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy SPECIFIC JAR file (better than wildcard)
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# Set better JVM options
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]