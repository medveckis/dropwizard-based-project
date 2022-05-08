CALL mvn clean package -f .\api-gateway\pom.xml
CALL mvn clean package -f .\book-service\pom.xml
CALL mvn clean package -f .\user-service\pom.xml
CALL mvn clean package -f .\borrow-management-service\pom.xml

START "Discovery Server" zkServer
START "API Gateway" java -jar api-gateway/target/api-gateway-1.0-SNAPSHOT.jar server api-gateway/api-gateway.yml
START "Book Service" java -jar book-service/target/book-service-1.0-SNAPSHOT.jar server book-service/book-service.yml
START "User Service" java -jar user-service/target/user-service-1.0-SNAPSHOT.jar server user-service/user-service.yml
START "Borrow Management Service" java -jar borrow-management-service/target/borrow-management-service-1.0-SNAPSHOT.jar server borrow-management-service/borrow-management-service.yml
