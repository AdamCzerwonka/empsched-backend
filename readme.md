# Employee Scheduling Application

This is a microservices-based employee scheduling application. It provides functionalities for managing employees, organizations, and their schedules. The application is built using Java and Spring Boot.

## Architecture

The application follows a microservices architecture, with different services responsible for specific business capabilities. These services communicate with each other and are managed by a service discovery component.

The main components of the architecture are:

*   **API Gateway**: The single entry point for all client requests. It routes requests to the appropriate backend service.
*   **Service Discovery**: Allows services to find and communicate with each other without hardcoding hostnames and ports.
*   **Individual Services**: Each service is responsible for a specific domain (e.g., `EmployeeService`, `OrganisationService`).
*   **Shared Library**: A common library (`CommonShared`) for shared code and DTOs.

## Services

The application is composed of the following services:

*   **`ApiGateway`**: Built with Spring Cloud Gateway, this service acts as the front door for all incoming API requests. It provides routing, filtering, and cross-cutting concerns like security.

*   **`AuthService`**: Manages user authentication and authorization. It is responsible for issuing and validating security tokens.

*   **`EmployeeService`**: Handles all employee-related operations, such as creating, retrieving, updating, and deleting employee data.

*   **`OrganisationService`**: Manages organization-specific data and business logic.

*   **`WorkflowService`**: Implements business workflows using Temporal. This service orchestrates complex, long-running processes that may involve multiple other services.

*   **`EurekaDiscovery`**: A service discovery server based on Netflix Eureka. All microservices register with Eureka, which allows them to locate each other.

*   **`SchedulingService`**: Manages employee schedules, including creation, updates, and lookups.

## CommonShared Library

The `CommonShared` module is a vital part of this project. It contains code and classes that are used across multiple services. The primary purpose of this library is to:

*   **Avoid Code Duplication**: By centralizing common functionalities, we reduce redundancy and improve maintainability.
*   **Promote Consistency**: Ensures that all services use the same data transfer objects (DTOs) and utility classes.

The `CommonShared` library typically includes:

*   **DTOs (Data Transfer Objects)**: Used for communication between services.
*   **Utility Classes**: Helper classes for common tasks.
*   **Exception Classes**: Custom exception classes used throughout the application.
*   **Model Classes**: Shared domain models.

To use the `CommonShared` library in another service, you need to add it as a dependency in the service's `pom.xml` file.

## Push Notifications

The application supports sending push notifications to the frontend client using the Web Push Protocol. This is used to notify users of important events, such as being assigned to a new schedule.

The implementation is split between two services:

*   **`AuthService`**: Responsible for managing user subscriptions.
    *   `POST /api/v1/notifications/subscribe`: Allows a user to register their browser's push subscription.
    *   `POST /api/v1/notifications/unsubscribe`: Allows a user to remove their subscription.
    *   `GET /api/v1/notifications/vapidPublicKey`: Provides the VAPID public key to the client.

*   **`EmployeeService`**: Responsible for triggering and sending notifications.
    *   When a relevant event occurs (e.g., a new schedule is created for an employee), this service constructs the notification payload and uses the `WebPushService` to send it to the appropriate user.

### Configuration

To enable push notifications, you must provide VAPID (Voluntary Application Server Identification) keys. These keys are used to secure the push messages.

The following environment variables must be set for the application in `.env`:
*   `VAPID_PUBLIC_KEY`: The public key, which is shared with the client.
*   `VAPID_PRIVATE_KEY`: The private key, which must be kept secret on the server.

You can generate these keys using online tools or a library like `web-push`.

## Getting Started

To get the application up and running, you will need to have the following prerequisites installed:

*   Java 24 or later
*   Maven 3.8 or later
*   Docker and Docker Compose

### Building the Project

To build all the services, run the following Maven command from the root of the project:

```bash
mvn clean install
```

### Running the Application

The easiest way to run the entire application is by using the provided Docker Compose setup.

1.  **Configure Environment**:
    Copy the `.env.example` and `.env.services.example` files to `.env` and `.env.services` respectively, and fill in the required values.

2.  **Build the Docker images**:
    Each service has a `Dockerfile` that can be used to build a Docker image.

3.  **Run with Docker Compose**:
    Navigate to the `docker` directory and run:

    ```bash
    ./run.ps1  # For Windows PowerShell
    ```

    This will start all the necessary services, including the application's microservices and the backing services like the database.

## Configuration

Each microservice is configured using an `application.yaml` file located in its `src/main/resources` directory. Additionally, there are profile-specific configuration files like `application-dev.yaml` for development environments.

The configuration files manage properties such as:

*   **Database connections**
*   **Server ports**
*   **Eureka registration**
*   **Temporal worker settings**

## Temporal Workflows in `WorkflowService`

The `WorkflowService` uses [Temporal](https://temporal.io/) to manage long-running, reliable business processes.

### Configuring New Workflows

To add a new workflow to the `WorkflowService`, you need to define the workflow and its activities, and then configure a worker to host them.

1.  **Define the Workflow Interface**:
    Create a new Java interface and annotate it with `@WorkflowInterface`. This interface defines the methods that your workflow will expose.

    ```java
    @WorkflowInterface
    public interface MyNewWorkflow {
        @WorkflowMethod
        String doSomething(String input);
    }
    ```

2.  **Define Activities**:
    If your workflow needs to perform external actions (like calling another service), define these as activities in an `@ActivityInterface`.

3.  **Implement the Workflow and Activities**:
    Create classes that implement the workflow and activity interfaces. This is where your business logic resides.

4.  **Assign Implementations to a Worker**:
    Use the `@WorkflowImpl` and `@ActivityImpl` annotations to assign your workflow and activity implementations to a specific worker. The `workers` attribute takes the name of the worker.

    In your workflow implementation:
    ```java
    @WorkflowImpl(workers = "my-new-worker")
    public class MyNewWorkflowImpl implements MyNewWorkflow {
        // ... workflow logic ...
    }
    ```

    In your activity implementation:
    ```java
    @Component
    @ActivityImpl(workers = "my-new-worker")
    public class MyActivitiesImpl implements MyActivities {
        // ... activity logic ...
    }
    ```

5.  **Configure the Worker in `application.yaml`**:
    Next, you must declare the worker in the `WorkflowService/src/main/resources/application.yaml` file. Each worker is configured with a name (which must match the one used in the annotations) and a `task-queue` that it will poll for tasks.

    ```yaml
    spring:
      temporal:
        workers:
          - name: create-organisation-worker # Existing worker
            task-queue: TASK_QUEUE_CREATE_ORGANISATION
          - name: my-new-worker # Your new worker
            task-queue: MY_NEW_TASK_QUEUE
    ```

6.  **Start the Workflow**:
    When starting a workflow from your controller or another service, you must specify the `task-queue` that the corresponding worker is listening on.

    ```java
    WorkflowOptions options = WorkflowOptions.newBuilder()
            .setTaskQueue("MY_NEW_TASK_QUEUE")
            // ... other options
            .build();

    MyNewWorkflow workflow = client.newWorkflowStub(MyNewWorkflow.class, options);
    workflow.doSomething("my-input");
    ```

By following these steps, you can extend the `WorkflowService` with new business processes, leveraging the power and reliability of Temporal.
