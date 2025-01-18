FROM eclipse-temurin:17-jdk
WORKDIR /app


# Install Python, pip, and necessary tools
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip && \
    python3 -m pip install --break-system-packages cqlsh

# Copy the application files
COPY target/polling-0.0.1-SNAPSHOT.jar polling-0.0.1-SNAPSHOT.jar
COPY src/main/resources/application.properties application.properties

# Set the default command to a shell
ENTRYPOINT ["sh"]
