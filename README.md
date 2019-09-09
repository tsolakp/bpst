Provide REST service to manage Blood sample data for patients. 
The `/sample` endpoint provides CRUD operation to add/get/delete blood sample markers.
The `/sample-report` endpoint will provide the percentage of patients that have similar blood marker levels against a single blood sample.

To run the application:

1. Build it with `mvn package`.
2. Run it with `java -jar target/bpst-1.0-SNAPSHOT.jar`.
3. See [swagger](http://localhost:8080/bpst-api/swagger-ui.html) for complete documentation.
4. You can also access [H2 embedded database](http://localhost:8080/bpst-api/h2-console) console for basic queries.
   Connection params are : `url=jdbc:h2:./data/db`, `username=sa`, `password=password`.