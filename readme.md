# Tenpo Challenge Project

This project is a backend service developed as part of the Tenpo backend challenge. It is a RESTful API built with Spring Boot and Java 21, designed to meet the following functionalities and requirements:

## Functionalities

1. **Dynamic Percentage Calculation**:
   - Implements a REST endpoint that receives two numbers (`num1` and `num2`).
   - The service sums both numbers and applies an additional percentage obtained from an external service (e.g., if `num1=5` and `num2=5` and the external service returns 10%, the result will be `(5 + 5) + 10% = 11`).
   - The external service can be a mock that returns a fixed percentage value (e.g., 10%).

2. **Percentage Cache**:
   - The percentage obtained from the external service is stored in memory (cache) and considered valid for 30 minutes.
   - If the external service fails, the last cached value should be used.
   - If there is no previously cached value, the API should respond with an appropriate HTTP error.

3. **Retry Logic for External Service Failures**:
   - If the external service fails, implement retry logic with a maximum of 3 attempts before returning an error or using the cached value.

4. **Call History**:
   - Implements an endpoint to query the history of all calls made to the API endpoints.
   - The history should include details such as:
     - Date and time of the call.
     - Invoked endpoint.
     - Received parameters.
     - Response (in case of success) or returned error.
   - The history query should support pagination.

5. **Rate Limiting**:
   - The API should support a maximum of 3 RPM (requests per minute).
   - If this threshold is exceeded, it should respond with an appropriate HTTP error (e.g., 429 Too Many Requests) and a descriptive message.

6. **HTTP Error Handling**:
   - Implements proper HTTP error handling for 4XX and 5XX series errors. Includes descriptive messages to help clients understand the problem.


## Requirements

- **Java 21**: The project is built using Java 21.
- **Spring Boot**: Utilizes Spring Boot for rapid development and ease of configuration.
- **PostgreSQL**: Uses PostgreSQL as the database to store call history.
- **Redis**: Uses Redis for caching and rate limiting.
- **Docker**: The project is containerized using Docker and can be run using Docker Compose.

## Getting Started

Follow the instructions below to set up and run the project.

### Prerequisites

- Docker
- Docker Compose

## Getting Started

1. Clone the repository:
    ```sh
    git clone https://github.com/Patrpg/tenpo-backend-challenge.git
    cd tenpo-backend-challenge
    ```

2. Build and run the Docker containers:
    ```sh
    docker-compose up --build
    ```

3. Try the API:
    - You can import a postman collection to achieve it.

## Postman Collection

You can use the following Postman collection to test the API endpoints:

```json
{
  "info": {
    "name": "Tenpo Challenge API",
    "_postman_id": "12345678-1234-1234-1234-123456789012",
    "description": "Postman collection for Tenpo Challenge API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Health Check",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/health",
          "protocol": "http",
          "host": [
            "localhost"
          ],
          "port": "8080",
          "path": [
            "health"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Dynamic Percentage",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/x-www-form-urlencoded"
          }
        ],
        "body": {
          "mode": "urlencoded",
          "urlencoded": [
            {
              "key": "num1",
              "value": "10",
              "type": "text"
            },
            {
              "key": "num2",
              "value": "20",
              "type": "text"
            }
          ]
        },
        "url": {
          "raw": "http://localhost:8080/api/dynamicPercentage",
          "protocol": "http",
          "host": [
            "localhost"
          ],
          "port": "8080",
          "path": [
            "api",
            "dynamicPercentage"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Get Paginated Dynamic Percentage Call History",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/dynamic-percentage-call-history?page=0&size=10",
          "protocol": "http",
          "host": [
            "localhost"
          ],
          "port": "8080",
          "path": [
            "api",
            "dynamic-percentage-call-history"
          ],
          "query": [
            {
              "key": "page",
              "value": "0"
            },
            {
              "key": "size",
              "value": "10"
            }
          ]
        }
      },
      "response": []
    }
  ]
}
```
To use this collection:

Copy the JSON content into a new file named TenpoChallengeAPI.postman_collection.json.
Open Postman.
Import the collection by selecting File -> Import and then choosing the JSON file you just created.

## Stopping the Application

To stop the application, run:
```sh
docker-compose down
```

## Additional Commands

- To rebuild the containers without cache:
    ```sh
    docker-compose build --no-cache
    ```

- To view the logs:
    ```sh
    docker-compose logs
    ```

## Notes

- If you send 11 as num1 parameter in the Dynamic Percentage, the percentage service will always fail.

- The cache is deleted every 30 minutes by our cacheManager.

- The rate limit works for each endpoint independently.

- The image of this project is available on docker hub, to use it:
    ```sh
    docker pull patrpg/tenpo-backend-challenge:latest
    ```