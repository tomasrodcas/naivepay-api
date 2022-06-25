# Getting Started

### Reference Documentation
To start working with the repository you must execute the following steps:

1. Clone repository
   
        git clone URL

2. enter the folder.
   
        cd repo

3. initialize git flow operation
    
        git flow init

4. You have to do your duties...


5. Push changes to remote repository
    
        git flow feature publish my_feature

6. Check in changes from the repository
7. 
        git flow feature pull origin my_feature

### Login and Security Config
The API has an authentication and authorization system using JWT and Spring Security - Auth0.

 Currently the API /login endpoint returns and access_token and a refresh_token on the response headers (In the future this could change for security purposes). To correctly log in,  /login accepts a POST request with x-www-form-urlencoded keys username and password. 
 
 To make an authenticated call to the API the token must be send as a Bearer token in the Authorization header of each request. Further Authorization depends on the session roles (customer - admin). Resource ownership should be checked in the service or controller layer, using the AuthenticationFacade to access the authenticated customer basic data (Similar to CustomerController - getCustomerById). 

To ignore security for an endpoint, it can be added to the SecurityConfig.configure() - ignoring.antMatchers( endpoint matcher). This is only recommended for the developing cycle to access tools such as h2 web console (already configured).

### Modifying JWT
In order to modify the JWT you can use the jwt.io webapp and sign the token using the same secret key used in the application, which is defined under 

`` sprint.jwt.secret = secret ``
  
  

### Guides

The following guides illustrate how to use some features concretely:

  

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)

* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)

* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)