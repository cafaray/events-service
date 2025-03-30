# Champion League API Documentation

This directory contains the OpenAPI (Swagger) specification for the Champion League API.

## API Overview

The API provides endpoints for managing sports venues, teams, matches, leagues, users, and authentication. It follows REST principles and uses JSON for request/response payloads.

### Base URLs

- API endpoints: `/v1`
- Authentication endpoints: `/auth`

### Available Endpoints

#### Venue Management

1. **Get All Venues**
   - Endpoint: `GET /venues`
   - Description: Retrieves a list of all venues
   - Response: Array of Venue objects
   - Status Codes:
     - 200: Success
     - 404: No venues found
     - 500: Server error

2. **Get Venues by Location**
   - Endpoint: `GET /venues/positions`
   - Description: Finds venues near specified coordinates
   - Parameters:
     - lat (query): Latitude coordinate
     - long (query): Longitude coordinate
   - Response: Array of VenuePosition objects
   - Status Codes:
     - 200: Success
     - 404: No venues found at location

#### Team Management

1. **Get All Teams**
   - Endpoint: `GET /teams`
   - Description: Retrieves a list of all teams
   - Response: Array of Team objects
   - Status Codes:
     - 200: Success
     - 404: Teams not found

2. **Get Team by ID**
   - Endpoint: `GET /teams/{id}`
   - Description: Retrieves a specific team
   - Parameters:
     - id (path): Team ID
   - Response: Team object
   - Status Codes:
     - 200: Success
     - 404: Team not found

#### Match Management

1. **Get All Matches**
   - Endpoint: `GET /matches`
   - Description: Retrieves a list of all matches
   - Response: Array of Match objects
   - Status Codes:
     - 200: Success
     - 404: Matches not found

2. **Get Match by ID**
   - Endpoint: `GET /matches/{uid}`
   - Description: Retrieves a specific match
   - Parameters:
     - uid (path): Match ID
   - Response: Match object
   - Status Codes:
     - 200: Success
     - 404: Match not found

3. **Get Match Details**
   - Endpoint: `GET /matches/{uid}/details`
   - Description: Retrieves detailed information about a match
   - Parameters:
     - uid (path): Match ID
   - Response: MatchDetails object
   - Status Codes:
     - 200: Success
     - 404: Match details not found

#### League Management

1. **Get All Leagues**
   - Endpoint: `GET /leagues`
   - Description: Retrieves a list of all leagues
   - Response: Array of League objects
   - Status Codes:
     - 200: Success
     - 404: Leagues not found

#### User Management

1. **Get All Users**
   - Endpoint: `GET /users`
   - Description: Retrieves a list of all users
   - Response: Array of User objects
   - Status Codes:
     - 200: Success
     - 404: Users not found

2. **Add New User**
   - Endpoint: `POST /users`
   - Description: Creates a new user
   - Request Body: User object
   - Response: Success message
   - Status Codes:
     - 200: Success
     - 400: Invalid input

3. **Get User by ID**
   - Endpoint: `GET /users/{uid}`
   - Description: Retrieves a specific user
   - Parameters:
     - uid (path): User ID
   - Response: User object
   - Status Codes:
     - 200: Success
     - 404: User not found

#### Authentication

1. **Verify Token**
   - Endpoint: `POST /auth/verify`
   - Description: Verifies an authentication token
   - Request Body: Token object
   - Response: User ID if valid
   - Status Codes:
     - 200: Token valid
     - 400: Token missing
     - 401: Token invalid

### Data Models

#### Venue

```json
{
  "id": "string",
  "address": "string",
  "capacity": 0,
  "city": "string",
  "country": "string",
  "surface": "string",
  "image": "uri"
}
```

#### VenuePosition

```json
{
  "id": 0,
  "name": "string",
  "location": {
    "type": "Point",
    "coordinates": [0, 0]
  },
  "area": {
    "type": "Polygon",
    "coordinates": [[[0, 0], [0, 1], [1, 1], [1, 0], [0, 0]]]
  }
}
```

#### Team

```json
{
  "id": "string",
  "name": "string",
  "country": "string",
  "founded": 0,
  "logo": "uri"
}
```

#### Match

```json
{
  "id": "string",
  "homeTeam": { "Team object" },
  "awayTeam": { "Team object" },
  "date": "date-time",
  "venue": { "Venue object" }
}
```

#### MatchDetails

```json
{
  "match": { "Match object" },
  "statistics": {
    "additionalProperties": true
  }
}
```

#### League

```json
{
  "id": "string",
  "name": "string",
  "country": "string",
  "season": "string",
  "logo": "uri"
}
```

#### User

```json
{
  "id": "string",
  "name": "string",
  "email": "string"
}
```

## Using the API Documentation

The API is documented using OpenAPI 3.0.3 specification. You can:

1. View the documentation using Swagger UI at: `http://localhost:8080/q/swagger-ui`
2. Download the OpenAPI specification from: `http://localhost:8080/q/openapi`

## Development

To modify the API specification:

1. Edit the `openapi.yaml` file in this directory
2. The changes will be automatically reflected in the Swagger UI when you restart the application
