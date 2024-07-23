# aatest3

This project is a comprehensive solution for processing and aggregating transaction records. It utilizes a Java Spring Boot backend to parse and aggregate data from uploaded files, and an Angular frontend for user interactions and visualizations.

## Starting the REST API Server

1. Navigate to the project root directory.
2. Run `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080` to start the `TransactionApplication` server on port 8080.

## Preparing the Client (Angular)

1. Navigate to the project root directory.
2. Navigate to the client directory: `cd transaction-client`
3. Ensure Node.js and npm are installed: `node -v` and `npm -v`
4. Install Angular CLI globally: `npm install -g @angular/cli`
5. Install dependencies: `npm install`

## Running the Client

1. From the client directory, start the Angular app: `ng serve`
2. Open a browser and navigate to `http://localhost:4200`