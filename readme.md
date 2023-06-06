# Elite Dangerous Pilot Network Backend (EDPN Backend)
The Elite Dangerous Pilot Network Backend (EDPN Backend) project provides a REST API that consumes data from the Elite Dangerous Data Network (EDDN) message stream. The data is then processed and stored in a database for use by other applications.

This module in particular will consume the EDDN messages and place them on a Kafka for other EDPN modules to process as needed.

## Table of Contents

- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
    - [Code structure](#code-structure)
- [Installation](#installation)
- [Reporting Issues](#reporting-issues)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)
- [Further info](#further-info)

___
## Technologies Used
The EDPN Backend project is built using the following technologies:

- [Spring Boot](https://spring.io/projects/spring-boot): An open-source Java-based framework used to create stand-alone, production-grade Spring applications quickly and easily.
- [Gradle](https://gradle.org/): A build automation tool used to manage dependencies and build Java projects.
- [Kafka](https://kafka.apache.org/): A distributed streaming platform used to build real-time data pipelines and streaming applications.
- [Docker](https://www.docker.com/): A platform used to build, ship, and run distributed applications.

___
## Project Structure
The EDPN Backend project consists of several independent projects either in their own repository or contained within the main modular monolithic project:

- `backend-eddn-message-listener`: The project used to consume the EDDN message stream. It will consume the messages, split them out per type and send them to a Kafka.
- `backend`: modular monolith that contains the other modules and functionality. Each module is independent of the others, except for the overarching `boot` project that combines and exposes the other modules to teh end users.   

### Code structure
The project adheres to Domain-Driven Design (DDD) principles.

```
src
└── main
├── java
│   └── com
│       └── example
│           └── stations
│               ├── application
│               │   ├── controller
│               │   ├── dto
│               │   ├── mapper
│               │   └── usecase
│               ├── configuration
│               ├── domain
│               │   ├── model
│               │   └── repository
│               │   └── util
│               ├── infrastructure
│               │   ├── adapter
│               │   └── persistence
│               │       ├── entity
│               │       └── repository
│               └── StationsApplication.java
└── resources
├── application.properties
└── ...
```

#### Application Layer

The application layer is missing in this project, as it is not needed since we only us zeromq to kafka integration

#### Configuration Layer

The config layer contains all the Bean configurations and annotations needed to instantiate the beans and bootstrap the Spring boot application

### Domain Layer

The domain layer contains some util classes and custom exceptions 

### Infrastructure Layer

The infrastructure layer contains the following components:

- **kafka**: This package contains the topic handler for creating new Kafka topics when needed 
- **zmq**: This package contains the Message handlers for the ZeroMQ messages 

___
## Installation
To run the EDPN Backend EDDN Message Listener project, follow these steps:

1. Install Docker
2. install Docker-compose
3. run the following command from the root project folder: `docker compose up -d`
   - this command will create a stack which contains a Zookeeper and a Kafka
   - this command will containerize the code via the dockerfile included, and run it in the stack

___
## Reporting Issues
To report an issue with the EDPN Backend project or to request a feature, please open an issue on the project's GitHub repository. You can also join the [discord](https://discord.gg/RrhRmDQD) and make a suggestion there in `ideas` section.

___
## Contributing
How to contribute to the project (and much more) is explained in our [charter](https://github.com/ed-pilots-network/charter).

___
## License
We are  using [Apache-2.0](https://opensource.org/license/apache-2-0/).

___
## Contact
The best way to contact us would be to join our [discord](https://discord.gg/RrhRmDQD) and ping the @Admin or @Developer groups


