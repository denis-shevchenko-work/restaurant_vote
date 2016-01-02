# Restaurant Vote Service


Build instructions

1. Install maven
2. Run
    mvn clean package
  from command line

Run Instructions
  
1. Install Postgresql server
2. Create empty database with name "restaurant".
3. Switch to target folder of built project
4. Run
	java -jar restaurant.war
5. Wait till message
		Started RestaurantApplication
6. Open localhost:8080/api/docs
7. Login with credentials {admin:123} or {user:123}