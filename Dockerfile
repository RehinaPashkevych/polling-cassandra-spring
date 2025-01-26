FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Install Python, pip, SSH server, and necessary tools
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    openssh-server \
    vim \
    sshpass \
    parallel \
    sudo && \
    python3 -m pip install --no-cache-dir --break-system-packages cqlsh requests && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Create a user for SSH access
RUN useradd -ms /bin/bash dockeruser && \
    echo 'dockeruser:password' | chpasswd && \
    echo 'dockeruser ALL=(ALL) NOPASSWD: ALL' > /etc/sudoers.d/dockeruser

# Grant `dockeruser` permission to access `/app` folder



# Configure SSH
RUN mkdir /var/run/sshd
RUN echo 'PermitRootLogin yes' >> /etc/ssh/sshd_config
RUN echo 'PasswordAuthentication yes' >> /etc/ssh/sshd_config

# Expose SSH port
EXPOSE 22

# Copy the application files
COPY target/polling-0.0.1-SNAPSHOT.jar polling.jar
COPY src/main/resources/application.properties application.properties
COPY tests/test.py test.py
COPY tests/test-vote.py test-vote.py

RUN chown -R dockeruser:dockeruser /app
RUN mkdir -p /home/dockeruser/app && cp -r /app/* /home/dockeruser/app/

# Set the default command to start SSH service and then your application
CMD service ssh start && sh
