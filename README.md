# Library Management System

![Test Status](https://github.com/zakirhossain-dsi/library-management-system/actions/workflows/test.yml/badge.svg)
![Build Status](https://github.com/zakirhossain-dsi/library-management-system/actions/workflows/docker-build-push.yml/badge.svg)
![Docker Pulls](https://img.shields.io/docker/pulls/hellozakir/library-management-system)
![Docker Image Size](https://img.shields.io/docker/image-size/hellozakir/library-management-system)

## Introduction

The **Library Management System** is a robust and comprehensive RESTful API designed to efficiently manage library operations, including book and borrower management. With a focus on scalability, reliability, and modern DevOps practices, it offers the following key features:

- **Borrower and Book Registration**: Simplified processes for registering new borrowers and adding books to the library's catalog.
- **Book Management**: Seamlessly manage borrowing and returning books, ensuring an organized and efficient tracking system.
- **Comprehensive Logging**: Implements detailed logging capabilities using SLF4J and Logback, enhancing traceability and system monitoring.
- **Containerization**: Fully containerized with Docker for streamlined development, testing, and deployment across environments.
- **Kubernetes Integration**: Supports scalable deployments with Kubernetes, including MySQL integration for persistent data storage.
- **CI/CD Pipeline**: Automated build, testing, and deployment workflows powered by GitHub Actions, ensuring rapid and error-free releases.

This system leverages modern software engineering practices to deliver an optimized and maintainable library management solution.

## Key Features
- **Java 17 Compatibility**: Harnesses the power of the latest Java version, ensuring modern language features and enhanced performance, with Maven for efficient dependency management.
- **Spring Boot Framework**: Streamlines application setup, configuration, and development with the powerful and versatile Spring Boot framework.
- **Optimized Containerization**: Implements multi-stage Docker builds to create lightweight and efficient Docker images, improving deployment speed and resource utilization.
- **Seamless CI/CD Integration**: Leverages GitHub Actions for automated build, testing, and deployment workflows, including Docker builds and seamless pushes to Docker Hub.
- **Comprehensive API Documentation**: Features interactive and user-friendly API documentation powered by Swagger, enhancing developer productivity and ease of integration.
This feature set ensures a cutting-edge, efficient, and scalable solution for modern application development and deployment.

## Getting Started

### Prerequisites

Before you begin, make sure your environment is set up with the following:

- **Java 17**: Ensure the latest version is installed for compatibility.
- **Maven**: For managing dependencies and building the project.
- **GitHub Account**: To access the repository and CI/CD workflows.
- **Docker**: Required for containerization and deployment.
- **Docker Hub Account**: For storing and managing Docker images.
- **Kubernetes**: Set up a cluster using Minikube or a similar tool for orchestration.

### Build and Run the Application Locally

Follow these steps to set up and run the **Library Management System** on your local machine:

1. **Clone the Repository**
   <br/>Start by cloning the GitHub repository to your local system: 
   ```sh
    git clone https://github.com/zakirhossain-dsi/library-management-system.git
    cd library-management-system
    ```
    This command fetches the project code and navigates to the project directory.<br/><br/>
    
2. **Build the Project with Maven**
   <br/>Use Maven to clean, compile, and package the application: 
   ```sh
    mvn clean install
    ```
   This will ensure all dependencies are resolved and the project is packaged into a runnable JAR file.<br/><br/>

3. **Run the Application**
   <br/>Execute the packaged JAR file to start the application: 
   ```sh
    java -jar target/library-management-system-0.0.1-SNAPSHOT.jar
    ```
   After running this command, the application should be accessible locally on the configured port (e.g., http://localhost:8080 by default).

### Docker Setup and Deployment
Follow these steps to containerize and deploy the Library Management System using Docker:

1. **Build the Docker Image**
   <br/>Create a Docker image for the application
   ```sh
    docker build -t hellozakir/library-management-system .
    ```

2. **Run the Docker Container**
   <br/>Launch a Docker container from the built image
    ```sh
    docker run -p 8080:8080 hellozakir/library-management-system
    ```
   - The -p 8080:8080 option maps the container's port 8080 to your local machine's port 8080
   - Access the application in your browser or API client at http://localhost:8080.<br/><br/>
    
3. **Run the Application with a Specific Profile**
   <br/>Specify a Spring profile while running the container to use different configurations (e.g., `prod`, `test`):
    ```sh
    docker run -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=prod" hellozakir/library-management-system
    ```
   - The `-e` flag sets environment variables inside the container. 
   - Replace `prod` with the desired profile name, such as `dev` or `test`.

### Deploying with Docker Swarm
Docker Swarm is a container orchestration tool that enables the deployment and management of distributed applications. Follow these steps to deploy the **Library Management System** using Docker Swarm:

1. **Initialize Docker Swarm**
    <br/>Set up your Docker environment as a Swarm manager to orchestrate containerized services:
    ```sh
    docker swarm init
    ```

2. **Deploy the Application Stack**
   <br/>Deploy the Library Management System as a stack using the `docker-compose.yml` file:
    ```sh
    docker stack deploy -c docker-compose.yml library-management-system
    ```

### CI/CD Pipeline with GitHub Actions

Leverage GitHub Actions to automate the build, testing, and deployment processes for the Library Management System, ensuring streamlined development and deployment workflows. The configuration is defined in the `.github/workflows/test.yml` and `.github/workflows/docker-build-push.yml` files.

- **Automated Docker Image Build and Push**: Triggered on every push to the `main` branch to ensure the latest code is packaged and ready for deployment.

## Kubernetes Deployment with MySQL

Deploy the **Library Management System** seamlessly to a Kubernetes cluster with **MySQL** as the database.

### Prerequisites
Ensure the following prerequisites are met before proceeding:
- A functional **Kubernetes cluster** (local or cloud-based).
- **kubectl** installed and configured to interact with your cluster.
- A **Docker Hub account** with the application's Docker image built and pushed.

### Kubernetes Manifests

The deployment is managed using Kubernetes manifests located in the `k8s` directory. These files are structured to define the various resources needed for the application and database setup:
  
- #### Database Configuration
    - `mysql-secret.yaml`: Configures database credentials as Kubernetes Secrets, ensuring secure access to MySQL.<br/><br/>
    - `mysql-deployment.yaml`: Deploys the MySQL database as a pod with appropriate configuration and resource allocation.<br/><br/>
    - `mysql-service.yaml`: Exposes the MySQL database internally within the cluster using a Kubernetes Service.<br/><br/>
    - `mysql-pvc.yaml`: Ensures persistent data storage for MySQL using a PersistentVolumeClaim (PVC).

- #### Application Configuration
  - `deployment.yaml`: Deploys the Library Management System application, specifying the image, replicas, and environment variables.<br/><br/>
  - `service.yaml`: Exposes the application to other services or pods in the cluster using a Kubernetes Service.

- #### Traffic Routing (Optional)
  - `ingress.yaml`: Configures an Ingress resource to route external traffic to the application. This is useful for exposing the application to the internet with hostname-based routing.

### Deploying Kubernetes Manifests
Follow these steps to deploy the Library Management System and its dependencies to a Kubernetes cluster.
1. **Clone the repository**
   <br/>Start by cloning the project repository to your local machine. This will provide you access to the necessary Kubernetes manifests stored in the `k8s` directory.

    ```sh
    git clone https://github.com/zakirhossain-dsi/library-management-system.git
    cd library-management-system
    ```
   
2. **Apply Kubernetes Manifests**
   <br/>Apply the provided Kubernetes manifests to your cluster in the specified order:<br/><br/>
   - **Database Setup**
     <br/>Configure MySQL with Secrets, Persistent Volume, Deployment, and Service:

     ```sh
     kubectl apply -f k8s/mysql-secret.yaml
     kubectl apply -f k8s/mysql-pvc.yaml
     kubectl apply -f k8s/mysql-deployment.yaml
     kubectl apply -f k8s/mysql-service.yaml
     ```
   - **Logging Setup (Optional)**    
     If your application requires persistent logging, apply the log-related Persistent Volume manifest:
       ```sh
       kubectl apply -f k8s/log-pv.yaml
       ``` 
    - **Application Setup:**
     <br/>Deploy the Library Management System application and expose it via a Kubernetes Service:
     ```shell
     kubectl apply -f k8s/deployment.yaml
     kubectl apply -f k8s/service.yaml
     ```
   - **Ingress Setup (Optional)**
   <br/>If you want to route external traffic to the application, ensure an Ingress controller is installed in your cluster. Then, apply the Ingress manifest:
     ```shell
     kubectl apply -f k8s/ingress.yaml
     ```

### Accessing the Application
Once the **Library Management System** is deployed, you can access it via the Kubernetes-provisioned Service or through an Ingress resource (if configured).
1. **Access via LoadBalancer Service**: 
<br/>The Service is configured as a `LoadBalancer`, Kubernetes will provision an external IP address to expose the application. To retrieve the external IP:
    ```sh
    kubectl get svc library-management-system
    ```
   - Check the EXTERNAL-IP field in the output.
   - Once available, access the application using the URL:
      ```shell
      http://<EXTERNAL-IP>:8080
      ```
2. **Access via Ingress (Optional)**: 
<br/>If an Ingress resource is configured, you can access the application using the host or domain name defined in the `ingress.yaml` file.<br/><br/>
   
   - **Steps to Access Using Ingress**
     - Verify the Ingress resource is applied:
        ```shell
        kubectl get ingress
        ```
     - Ensure the host defined in `ingress.yaml` (e.g., library.example.com) is resolvable:
       - Update DNS records to map the hostname to the ingress controller's external IP.
       - For local testing, you can add an entry to your `/etc/hosts` file:
         ```shell
         <INGRESS-EXTERNAL-IP> library.example.com
         ```
       - Access the application using the defined hostname:
         ```shell
          http://library.example.com
         ```
### Additional Commands for Managing the Application
Here are some useful commands to manage and monitor the Library Management System deployment in your Kubernetes cluster:

1. **Scale the Deployment**
   <br/>To adjust the number of replicas (pods) for the application, use the following command:
    ```sh
    kubectl scale deployment/library-management-system --replicas=5
    ```
2. **Check the Status of the Pods**
  <br/>View the status of all pods associated with the application:
    ```sh
    kubectl get pods -l app=library-management-system
    ```
3. **Describe the Deployment**
   <br/>Get detailed information about the deployment, including events, strategy, and resource configuration:
    ```sh
    kubectl describe deployment/library-management-system
    ```
4. **Monitor Logs**
   <br/>Check application logs for debugging
   ```shell
   kubectl logs -l app=library-management-system
   ```

### Cleanup Resources

When you no longer need the deployed resources, follow these steps to clean up your Kubernetes environment. This ensures no unused resources consume cluster capacity.
1. **Delete Application and Database Resources**
   ```sh
   kubectl delete -f k8s/service.yaml
   kubectl delete -f k8s/log-pv.yaml
   kubectl delete -f k8s/deployment.yaml   
   kubectl delete -f k8s/mysql-service.yaml   
   kubectl delete -f k8s/mysql-pvc.yaml
   kubectl delete -f k8s/mysql-deployment.yaml 
   kubectl delete -f k8s/mysql-secret.yaml
   ```
2. **Delete Ingress Resource (Optional)**
<br/>If you deployed an Ingress resource to expose the application externally, delete it as well:
   ```shell
   kubectl delete -f k8s/ingress.yaml
   ```
## Logging

The Library Management System uses SLF4J with Logback as its logging framework, providing a robust and flexible way to track application activity and debug issues.

### Logback Configuration

The logging behavior is configured in the `logback.xml` file located in `src/main/resources/`. This configuration file defines:
- **Logging Pattern**: Specifies the format of log messages, including timestamps, log levels, and message content.<br/><br/>
- **Log File Location**: Logs are saved in the logs/ directory under the filename library-management-system.log.<br/><br/>
- **Log Rotation Policy**: Automatically rotates logs daily to prevent files from growing too large. Older logs are archived, and files older than 30 days are deleted to free up disk space.

You can modify the `logback.xml` file to customize these settings based on your requirements.

### Accessing Logs
Logs are generated in two locations:
1. **Console**: Logs are printed to the standard output during runtime for immediate visibility.<br/><br/>
2. **File**: A persistent log file is created at:
   ```sh
   logs/library-management-system.log
   ```
The log file includes detailed activity and error logs, making it ideal for post-analysis or debugging.

### Logging Levels

The application uses the following logging levels to categorize log messages:
- `INFO`: Provides general application events and status updates.<br/><br/>
- `DEBUG`: Offers detailed technical information for debugging purposes, such as data flows and method-level details.<br/><br/>
- `ERROR`: Highlights critical issues or failures that need immediate attention.

You can control the verbosity of logs by adjusting the logging levels in the logback.xml file.

### Viewing Logs
You can monitor the application’s activity and troubleshoot issues by accessing the logs. There are two primary ways to view logs:

1. **Console Output**: When the application is running, logs are displayed in real-time on the console. This is useful for quick debugging and immediate insights into the application's behavior.<br/><br/>
2. **Log File**: A detailed and persistent log file is generated at: `logs/library-management-system.log`.

## Testing

Unit tests are essential to verify the functionality, reliability, and correctness of the Library Management System. The project includes comprehensive test cases to validate core features and ensure consistent performance during development and deployment.

### Executing Unit Tests

You can easily run the unit tests using Maven, which will execute all test cases and generate detailed reports.<br/>

**Command to Run Tests:**
Navigate to the root directory of your project and execute:
```bash
mvn test
```

### Test Status Badges

The repository includes dynamic badges that reflect the current build status and test coverage. These badges are updated automatically with every new commit, offering an at-a-glance view of the project’s health.

### Postman Collection

The Postman Collection for the Library Management System API provides an organized and ready-to-use set of API requests, enabling developers and testers to interact seamlessly with the application’s endpoints.

**Details**
- **Name**: Library Management System<br/><br/>
- **Description**: This collection simplifies development and debugging by offering a structured way to explore the API.<br/><br/>
- **Schema**: The Postman collection schema is available in JSON format and can be imported directly into Postman: <br/>
  [Download Schema](https://raw.githubusercontent.com/zakirhossain-dsi/library-management-system/main/postman_collection.json)

**Endpoints:**
1. **Register a New Borrower**
   - Purpose: Allows the registration of a new borrower in the system, capturing essential details like name and email. 
   - Endpoint: `POST {{base_url}}/api/borrowers`
   - Request Headers:
      - `Content-Type: application/json`
   - Request Body:
     ```json
     {
      "email": "raymond.tang@example.com",
       "name": "Raymond Tang"
     }
     ```
2. **Register a New Book**
    - Purpose: This endpoint allows you to add a new book to the library's catalog by providing key details like ISBN, title, and author. 
    - Endpoint: `POST {{base_url}}/api/books`
    - Request Headers:
        - `Content-Type: application/json`
    - Request Body:
      ```json
      {
        "isbn": "1234567890",
        "title": "Clean Code",
        "author": "Robert C. Martin"
      }
      ```
3. **List All Books**
   - Purpose: This endpoint retrieves a comprehensive list of all the books available in the library. It is useful for displaying the library’s catalog or performing operations like searching and filtering. 
   - Endpoint: `GET {{base_url}}/api/books`
 

4. **Borrow a Book**
   - Purpose: This endpoint enables a borrower to borrow a specific book from the library. It updates the library records to reflect the transaction, ensuring that the book is marked as borrowed. 
   - Endpoint: `POST {{base_url}}/api/borrowers/{{borrower_id}}/borrow/{{book_id}}`
   - Request Headers:
      - `Content-Type: application/json`
   - Request Body:
     ```json
     {
      "borrowerId": {{borrower_id}},
      "bookId": {{book_id}}
     }
     ```

5. **Return a Book**
   - Purpose: This endpoint allows a borrower to return a previously borrowed book to the library. It updates the library's records to reflect that the book is now available for borrowing. 
   - Endpoint: `POST {{base_url}}/api/borrowers/{{borrower_id}}/return/{{book_id}}`
   - Request Headers:
      - `Content-Type: application/json`


6. **Verify Borrower Details**
   - Purpose: This endpoint allows you to retrieve detailed information about a specific borrower. It is useful for verifying borrower data, such as their personal details, borrowed books, and account status.
   - Endpoint: `GET {{base_url}}/api/borrowers/{{borrower_id}}`

7. **Verify Book Details**
   - Purpose: This endpoint retrieves detailed information about a specific book in the library. It is useful for verifying the book's availability, metadata, and borrowing status. 
   - Endpoint: `GET {{base_url}}/api/books/{{book_id}}`

**Variables Included:**
<br/>Below are the key variables used in the API examples. Update these values as needed to match your environment or test data:
1. **base_url**: `http://localhost:8080`
2. **borrower_id**: `1`
3. **book_id**: `1`

### Importing the Postman Collection
The Postman collection simplifies testing and interacting with the **Library Management System** API by providing pre-configured requests for all available endpoints. Follow the steps below to import the collection into Postman:
To import the Postman collection:
1. Download the `postman_collection.json` file from the repository.
2. Launch the Postman application.
3. Click on the `Import` button in the top-left corner of the Postman interface.
4. Select the `postman_collection.json` file.
5. Click `Open` to complete the import process.

By importing the Postman collection, you gain a powerful tool for efficient and streamlined interaction with the **Library Management System** API.

### API Documentation with Swagger

The Library Management System includes Swagger integration to provide comprehensive and interactive API documentation, making it easy to explore, test, and understand the available endpoints.
#### Accessing Swagger UI
Follow these steps to view and interact with the API documentation:
1. **Run the application**:
   - Start the application locally using `java -jar`, **Docker**, or **Kubernetes**. Ensure the application is running and accessible.
2. **Open Swagger UI**:
   - Launch your browser and navigate to the following URL. Replace `localhost:8080` with your application's host and port if running in a different environment.
     ```sh
     http://localhost:8080/swagger-ui.html
     ```

### API Endpoints

- **Register a new borrower**:
    ```http
    POST /api/borrowers
    {
      "name": "Raymond Tang",
      "email": "raymond.tang@example.com"
    }
    ```

- **Register a new book**:
    ```http
    POST /api/books
    {
      "isbn": "1234567890",
      "title": "Clean Code",
      "author": "Robert C. Martin"
    }
    ```

- **Get a list of all books**:
    ```http
    GET /api/books
    ```

- **Borrow a book**:
    ```http
    POST /api/borrowers/{borrowerId}/borrow/{bookId}
    ```

- **Return a borrowed book**:
    ```http
    POST /api/borrowers/{borrowerId}/return/{bookId}
    ```

- **Get borrower details**:
    ```http
    GET /api/borrowers/{borrowerId}
    ```

- **Get book details**:
    ```http
    GET /api/books/{bookId}
    ```

## Acknowledgments
We sincerely thank the open-source community for their invaluable contributions. Special thanks to the maintainers of **Spring Boot**, **Docker**, and **GitHub Actions** for providing tools that empower seamless development, deployment, and automation. Your efforts make projects like this possible!