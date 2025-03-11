FROM openjdk:25-ea-4-jdk-oraclelinux9

# Set the working directory in the container
WORKDIR /app

# Copy the entire source directory
COPY ./ /app

# Copy JSON data files and place them in a known directory
COPY src/main/java/com/example/data/*.json /app/data/

# Set environment variables for JSON file paths
ENV CARTS_FILE_PATH="/app/data/carts.json"
ENV ORDERS_FILE_PATH="/app/data/orders.json"
ENV PRODUCTS_FILE_PATH="/app/data/products.json"
ENV USERS_FILE_PATH="/app/data/users.json"

# Expose the application port (adjust if needed)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/mini1.jar"]