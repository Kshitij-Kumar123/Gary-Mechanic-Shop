# Gary's Mechanic Shop CMS

This is a fictitious content management system which allows mechanic shops to monitor progress of the customer's cars, all built using Spring Boot, Spring Security, PostrgeSQL and more. 
You can easily create, modify or delete a number of tickets to the customer's cars. To help them make sure that the car's repair is actually progressing: 

- Add useful information to the ticket as car model, vin number, car title status, recorded mileage and more. This allows a clear description of what the vehicle is!
- To the tickets, mechanics can easily assign it to themselves and add basic things such as name, description, helpful comments, initial repair quote etc. 
- Easily add only compatible parts used in repair to each ticket. 
- Of course, everytime a mechanic modifies details in the ticket, they are automatically recorded via automatic history record keeping.

All customers have to do is simply log in and see how their repair is progressing!


## How did I build it?

![microservice basic structure](https://user-images.githubusercontent.com/68606208/149051348-fb7e2f94-08d6-41cc-acfc-9459071faf61.png)

This is a Spring Boot application which follows the microservice architecture. Vital tools were used in this microservices such as 
- Spring Security and JSON Web Tokens (JWT) to secure the application with basic user heirarchy and privledges.  
- PostgreSQL database to store all user and employee information (with encrypted passwords via Bcrypt), ticket, customer car, model and car parts information and more,
- Spring Cloud config to store most of the services configuration data, 
- Spring Web and Spring Data JPA to create basic RESTful endpoints routed effectively by Spring Gateway, 
- add a discovery service through Eureka, 
- and lastly, used Resilience4J as circuit breaker as service communicate with each other. 


## What's next for the project?

The project still has many features that will be implemented in the future, such as 
- using Hystrix Dashboard or equivalent to monitor the microservice very easily.
- Add options for mechanics to add PDF or word files, 
- Instant messages to customer every time there is progress to their car via Rabbit MQ, 
- a React frontend which pairs the microservice
- lastly, deploy this application via Docker


