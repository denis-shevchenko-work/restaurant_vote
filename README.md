# Restaurant Vote Service

## Environment:
RDBS: Postresql


## Build instructions
1. Create empty database with name "restaurant_test".
2. Install maven
3. Run
    mvn clean package
  from command line

## Run Instructions
  
1. Create empty database with name "restaurant".
2. Switch to target folder of built project
3. Run
	java -jar restaurant.war
4. Wait till message
		Started RestaurantApplication
5. Open localhost:8080/api/docs
6. Login with credentials {admin:123} or {user:123}