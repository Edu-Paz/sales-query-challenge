# Sales Query Challenge

REST API for querying sales and seller data, built as part of the **DevSuperior Modern Developer** program (Backend module — JPA, SQL, and JPQL).

The system models a sales domain where each **Sale** belongs to one **Seller**, and a seller can have many sales. The challenge focuses on implementing custom JPQL queries for reporting and aggregation.

## Technologies

- Java 17
- Spring Boot 2.7.3
- Spring Data JPA
- H2 Database (in-memory)
- Maven

## Getting Started

### Prerequisites

- JDK 17+
- Maven (or use the included Maven Wrapper)

### Run the application

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The API starts on `http://localhost:8080`.

### H2 Console

The H2 web console is enabled at `http://localhost:8080/h2-console`.

| Setting  | Value              |
| -------- | ------------------ |
| JDBC URL | `jdbc:h2:mem:testdb` |
| Username | `sa`               |
| Password | *(empty)*          |

Sample data is loaded automatically from `import.sql` on startup.

## API Endpoints

### Get sale by ID

```
GET /sales/{id}
```

Returns a single sale with id, date, amount, and seller name.

**Example**

```
GET /sales/1
```

### Sales report (paginated)

```
GET /sales/report
```

Returns a paginated list of sales filtered by date range and seller name.

| Parameter | Required | Description |
| --------- | -------- | ----------- |
| `minDate` | No       | Start date (`yyyy-MM-dd`). Defaults to one year before `maxDate`. |
| `maxDate` | No       | End date (`yyyy-MM-dd`). Defaults to today. |
| `name`    | No       | Partial seller name (case-insensitive). Defaults to empty string (matches all). |
| `page`    | No       | Page number (zero-based). |
| `size`    | No       | Page size. |
| `sort`    | No       | Sort field and direction (e.g. `id,asc`). |

**Examples**

```
GET /sales/report
GET /sales/report?minDate=2022-05-01&maxDate=2022-05-31&name=odinson
GET /sales/report?page=0&size=20&sort=id,asc
```

**Sample response**

```json
{
  "content": [
    {
      "id": 9,
      "date": "2022-05-22",
      "amount": 19476.0,
      "sellerName": "Loki Odinson"
    },
    {
      "id": 10,
      "date": "2022-05-18",
      "amount": 20530.0,
      "sellerName": "Thor Odinson"
    }
  ],
  "pageable": { ... },
  "totalElements": 3,
  "totalPages": 1,
  "size": 20,
  "number": 0
}
```

### Sales summary by seller

```
GET /sales/summary
```

Returns the total sales amount grouped by seller for a given date range.

| Parameter | Required | Description |
| --------- | -------- | ----------- |
| `minDate` | No       | Start date (`yyyy-MM-dd`). Defaults to one year before `maxDate`. |
| `maxDate` | No       | End date (`yyyy-MM-dd`). Defaults to today. |

**Examples**

```
GET /sales/summary
GET /sales/summary?minDate=2022-01-01&maxDate=2022-06-30
```

**Sample response**

```json
[
  {
    "sellerName": "Anakin",
    "total": 110571.0
  },
  {
    "sellerName": "Logan",
    "total": 83587.0
  },
  {
    "sellerName": "Loki Odinson",
    "total": 150597.0
  }
]
```

## Date handling rules

When optional date parameters are omitted:

- **`maxDate` empty** → uses the current system date.
- **`minDate` empty** → uses one year before `maxDate`.
- **`name` empty** (report only) → matches all sellers.

Date parsing and defaults are handled in the service layer; the controller receives all filter values as strings.

## Error handling

Requests for a non-existent sale ID return HTTP `404 Not Found` with a structured error body:

```json
{
  "timestamp": "2026-05-30T12:00:00Z",
  "status": 404,
  "error": "Resource not found.",
  "path": "/sales/999"
}
```

## Project structure

```
src/main/java/com/devsuperior/dsmeta/
├── controllers/          # REST endpoints
├── controllers/handlers/ # Global exception handling
├── dto/                  # Data transfer objects
├── entities/             # JPA entities (Sale, Seller)
├── repositories/         # Spring Data JPA repositories with JPQL queries
└── services/             # Business logic and date filter defaults
```

## Testing with Postman

A Postman collection for manual testing is available at:

https://www.getpostman.com/collections/dea7904f994cb87c3d12

Import the collection and run the requests against `http://localhost:8080`.

## License

Educational project — DevSuperior.
