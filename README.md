# Lost & Found Java

## About The Project

* Project is using:

Spring Boot
Java 21
Spring MVC
Spring Data JPA
OpenCSV

* And if you need/want to develop in it, it supports:

Visual Studio Code
VS Code Remote Development Extensions

* How to run it

There's a Dockerfile and a docker-compose.yml in the root folder, so you just need to run:

`docker compose up --build`

And it will compile the application, start it together with the mock.

* Calling the endpoints

There's a Postman collection with all endpoints.

To upload a CSV, just make sure to use the right path for `example.csv` in Postman.

To claim an item, just change the id in the path to an existing lost item id;

* Regarding the application

There are three users:

admin, user and userTwo

For simplicity, they're configured in memory and their password is `password`.