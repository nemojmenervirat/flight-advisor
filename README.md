<p align="center"><img width=60% src="https://github.com/nemojmenervirat/flight-advisor/blob/master/media/flight-advisor.png"></p>

![Java](https://img.shields.io/badge/java-11-blue)
![Spring Boot](https://img.shields.io/badge/spring%20boot-2.3.4.RELEASE-blue)
![Database](https://img.shields.io/badge/database-h2-brightgreen)

# Basic Overview

Flight advisor API for finding the cheapest flights.

# Endpoints

#### SignUp
```
POST /app-user/sign-up

{
	"firstName":"John",
	"lastName":"Smith",
	"username":"john.smith",
	"password":"12345"
}
```

#### Login
```
POST /app-user/login

{
	"username":"john.smith",
	"password":"12345"
}
```
Returns token.

#### Get Cities (user)
```
GET /cities?name=value1&limitComments=value2
```
Parameters name and limitComments are optional

#### Add City (administrator)
```
POST /cities

{
	"name":"Banja Luka",
	"counry":"Bosnia and Herzegovina",
	"description":"Some description..."
}
```

#### Import Cities (administrator)
```
POST /cities/import
```
Imports cities from airports data set. Relevant columns city in 2nd column and country in 3rd (0 indexed).

#### Remove City (administrator)
```
DELETE /cities/{cityId}
```

#### Add Comment For City (user)
```
POST /cities/{cityId}

{
	"description":"lorem ipsum"
}
```

#### Change Comment For City (user)
```
PUT /cities/{cityId}

{
	"description":"lorem ipsum dolor"
}
```

#### Remove Comment For City (user)
```
DELETE /cities/{cityId}
```

#### Import Airports (administrator)
```
POST /airports/import
```

#### Import Routes (administrator)
```
POST /routes/import
```

#### Find Cheapest Flight (user)
```
GET /flight/cheapest?sourceCountry=value1&sourceCity=value2&destinationCountry=value3&destinationCity=value4
```

# Usage and examples

Sign up or if you need use existing administrator credentials 
```
POST /app-user/login

{
	"username":"admin",
	"password":"admin"
}
```
After successful login take token from response body and use it in next requests in `Authorization` header with prefix `Bearer `. As administrator you can add or import cities, import airports and import routes.
As user you can add, change and remove comments on cities, get all cities with comments or search for specific city, also you can find cheapest route between two cities.

# Demo

https://nemojmenervirat-flight-advisor.herokuapp.com/
