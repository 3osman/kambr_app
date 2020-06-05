

# Kambr Challenge


### **Technology Choice**

As much as I would have preferred a framework that values convention over configuration (i.e Rails) for an interview task, I followed the requirements and wrote in Java. I opted for **Spring Boot (configuration all the way)**, so the learning curve was interesting, as I used Java but not in the context of Spring Boot before. Using spring boot data allows for extensibility to any database driver, by just extending their interface and adding small configuration (application.properties).


### **Use Cases**



1. Client searches for flights, gets back a list of flights.
2. Client searches for flights, including data. Client gets a list of flights. Client gets flight Ids, along with cabin type, and class type, and sends a batch update request.
3. Client searches for flights, including data. Client gets a list of flights. Client picks a specific flight, and gets the flight Id, and cabin type, can class type, and sends a single class update.


### **Approach**

The problem consists of 3 parts: an API endpoint to find flights, update flight class, update batch of flight classes. My approach was as follows:



*   Define classes
*   Choose database
*   Define Repositories
*   Define Services
*   Define APIs
*   Define DTO (if needed)
*   Optimization and concurrency handling 

**Classes**

Flight _(has_many cabins)_



1. Cabin _(has_one flight and has_many flight classes)_
2. Flight Class _(has_one cabin)_
3. Flight metadata

**Database**

Choice for the database was obviously relational, since we will be most probably dealing with many joins. However, for search purposes, elasticsearch is used to store metadata of the flight (searchable fields). Flight deletion (transaction in postgresql and propagation to Elasticsearch) is not handled in this implementation. A full database schema analysis is available through **<code>schema/index.html</code></strong>



![alt_text](images/image1.png "models")


**Repositories**

Repositories extend ElasticSearchRepository and JpaRepository. Built-in functions are used for querying.

**Services**

Services defined in general follow the Single responsibility paradigm. I have four services:



1. FlightService
2. FlightMetaDataService
3. CabinService
4. FlightClassService

If any other model layer is added (i.e seats), this could be extended easily.

**APIs**



*   Three endpoints are defined:_ “/flights”, “/update”, “/batchUpdate”_. More info on the APIs can be found on “<code>[http://{host}/swagger-ui.html](http://localhost:9000/swagger-ui.html)</code>”
*   PUT is the choice for update, while PUT is the choice for batchUpdate
*   Search endpoint has an optional param (includeData), to allow fetching of flights cabins and classes. 
*   Search is done in a fuzzy manner for fields other than date. Edit distance is set to 2.

<strong>DTO</strong>

Two DTOs are defined to handle flight search response, including children (cabins/classes) or without. 


### **API**

Swagger API UI is used to document the API with examples, see  <code>[http://{host}/swagger-ui.html](http://localhost:9000/swagger-ui.html)<strong>. Also included an json snapshot of it.</strong></code>


### **Optimization**



*   Flight Id is formed by appending the unique fields together (origin + ":" + destination + ":" + departureDate.getTime() + ":" + flightNumber). This ensures no duplicate insertions. Insertion handling is not covered in depth in the implementation.
*   Aircraft type, cabin, and class values are all built into Enum values for better handling .
*   Flight class update is protected by a pessimistic lock through a Transactional function in the service.
*   Search operations are cached. The query is not run again if the request comes again and it is still cached.
*   If the “includeData” option is false/not present in the search call, the join queries for flights, cabins and classes are not performed. This is a design choice, as this might result in n+1 query, but I opted out for that instead of eagerly loading for both request types as there is no possible dynamic configuration in Spring boot as of now.
*   Search requests are automatically mapped to a Class (FlightSearchRequest), this allows catching of Bad Requests easily.


### **Tests**

Two types of tests are provided:

Integration tests tests all database and transaction functions through repository. Tests can be run through `./mvnw test`


### **Extensions**



1. Better error handling, currently API errors are handled with JSON. Generalizing to errorhandler class would have been nice
2. Since we protect and lock on writing, it would have been nice to retry on concurrency errors. This could be handled by the load balancer or any layer before the web server.
3. Update endpoints were not tested. Search Update operations were tested in repositories tests. Testing search update endpoints is crucial as well, but due to time constraints, it was not achieved. The project structure allows for extensibility of tests easily.


### **Appendix**



1. Swagger JSON can be viewed here: [https://editor.swagger.io/](https://editor.swagger.io/)
2. Application settings (database, ports, username, password, elasticsearch server) are all present in `application.properties`. I used maven for dependencies. Simply run ./mvnw spring-boot:run to start the Tomcat server
3. Simple Postgresql and ElasticServer queries logging is enabled through the configuration. This allows seeing the exact queries run. You can disable in `application.properties`
4. A full database schema analysis is available through `schema/index.html`

