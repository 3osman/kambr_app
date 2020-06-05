

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



![alt_text](images/image1.png "image_tooltip")


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




# Second Part


### **Requirements**



*   Add the ability to import flight data into the system by sending a file blob with a varying format.
*   Raw file size can be between megabytes to gigabytes/day


### **Estimation**



1. Assume we have 20 entities
2. Assuming file size ranges between 2 megabytes and 5 gigabytes
3. Dataflow per day can be in the range of 20 * 2 megabytes till 20 * 2 gigabytes / day
4. Estimated best case scenario flow/day 40 megabytes
5. Estimated worst case scenario flow/day 40 gigabytes
6. Current row size is 168 bytes
7. Estimation number of entries/day will hugely depend on the file format and the size on disk it takes


### **API**

One endpoint will be defined, upload(File file). We will limit the size to 2GB. This is the exposed endpoint to the applications server. Our internal web server accepting this request will divide it into two calls: createMetadata, and then uploadFile. This is for handling fault tolerance, as the server can keep retrying for the same file after metadata is sent. This will be discussed in detail in the diagram.



1. uploadFile(file)
2. createMetaData(fileMetaData)
3. uploadFile(file)


### **Data Model**

As this is an extension of our system, the file will be parsed and persisted into disk storage temporarily and then streamed in-memory for parsing into Flight object and then persisted. A pointer to the current end line will be kept and the next fetch from the file will begin from it. These calls can be done in parallel.


### **High-level Design**




![alt_text](images/image2.png "image_tooltip")



### **Detailed Design**

**FileServer**

The file server accepts the metadata, then accepts the file from the web server. The file server then persists the file into storage temporarily for streaming it to the parsing service. This rescues files from failing in case the parsing service fails. The file is removed from storage once it is fully parsed. A pointed to current chunk end is kept as well. The file server streams chunks to the parsing service.

**ParsingService**

The parsing service receives a file chunk. The parsing service has one logical responsibility: parse the file chunk into a standardized JSON/XML format and make sure the file format is supported. The service then forwards it to a message queue for the persisting service to handle. To allow adaptability for multiple file formats, the file type/requested parser is sent in the metadata from the web server to the parsing service. The parsing service has a thin adapter engine that has a factory method to use the correct parser interface based on the provided type. The service throws an error of “type not supported” if the parser is not supported.

**Persisting Service**

The persisting service receives JSON/XML structure of flights, and inserts them into a database. The data model lives in this level, and the data is persisted in both Metadata storage and the full data storage. The data is first committed to the postgresql DB and then propagated to the Elasticsearch server. The service is responsible to make sure the data is reflected. Since the data size is not big or dynamic in general, normal hashing on flight ID could be used for sharding.


![alt_text](images/image3.png "image_tooltip")



### **Additional Considerations**



*   The data is not extremely dynamic, and the size is also limited. The DB is not a bottleneck perse, and we can as well remove the old and stale data and store it on a less expensive form of storage with low access abilities.
*   We persist the files on disk storage and load it chunk by chunk to memory through the file server. This protects data from being lost, and allows for retrying in case of failure.
*   Parsing service allows extensibility by providing a layer transparent to the client that parses based on file type. This layer is extensible and it makes it easy to detect unsupported data types. \
 \
