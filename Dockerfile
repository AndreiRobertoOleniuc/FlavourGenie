# Use an official OpenJDK 21 image as a parent image
FROM openjdk:21-slim

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Copy the project files to the container
COPY . .

# Run Maven clean install to build the project
RUN mvn clean install -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Set the OpenAI API Key and Google Application Credentials as environment variables
ENV OPENAI_API_KEY=
ENV GOOGLE_APPLICATION_CREDENTIALS=

# Run the Spring Boot application
CMD ["sh", "-c", "java -Dspring.profiles.active=local -jar target/*.jar"]