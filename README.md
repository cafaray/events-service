# champion-league

This project uses Quarkus, the Supersonic Subatomic Java Framework, to manage sports venues and their locations.

## Project Structure

The project follows a clean architecture pattern with the following structure:

```plaintext
src/main/kotlin/com/supplier/championleague/
├── controller/
│   └── VenueController.kt       # REST endpoints for venue operations
├── service/
│   └── VenueService.kt          # Business logic layer
├── repositories/
│   ├── VenueRepository.kt       # Data access for venue information
│   └── VenuePositionRepository.kt # Spatial data operations for venues
└── model/
    ├── Venue.kt                 # Core venue data model
    └── VenuePosition.kt         # Spatial data model for venue locations
```

### Key Components

- **Venue Management**: Handles basic venue information including address, capacity, and facilities
- **Spatial Operations**: Supports geographical queries to find venues by location
- **REST API Endpoints**:
  - `GET /v1/venues`: Retrieve all venues
  - `GET /v1/venues/positions?lat=&long=`: Find venues by geographical coordinates

### Technologies Used

- **Quarkus**: Main framework
- **Kotlin**: Programming language
- **Firebase/Firestore**: Document database for venue data
- **PostGIS**: Spatial database extension for geographical queries
- **Hibernate Spatial**: JPA implementation for spatial data

### About postgis

PostGIS is an open source spatial database that extends the PostgreSQL database to support spatial data types and operations. It provides a set of functions and operators for working with spatial data, such as points, lines, and polygons. PostGIS is used in this project to store and query the locations of venues.

The PostGIS database is hosted on a public cluster to be accessed directly by the services. The approach that we are using to deploy the database is to use a Percona Cluster. For more information in how to setup a Percona Cluster, please refer to the [our percona cluster documentation](./spatialDatabase.md).

### About hibernate spatial

Hibernate Spatial is a JPA implementation for spatial data. It provides a set of annotations and interfaces for working with spatial data, such as points, lines, and polygons. Hibernate Spatial is used in this project to store and query the locations of venues.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/champion-league-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
