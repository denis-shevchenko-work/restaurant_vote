# Restaurant Vote Service
## Decription

This service provides a voting system API for deciding where to have lunch.

There are 2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote on which restaurant they want to have lunch at
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we asume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed


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
	java -jar restaurant.jar
4. Wait till message
		Started RestaurantApplication
5. Open http://localhost:8080/api/docs
6. Login with credentials {admin:123} or {user:123}