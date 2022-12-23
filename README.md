1. First I faced with how to structure models. Especially relation between Film type and Price type, 
   because actual price of rent depends on both types, so it's 1 to 1 relation, in case with DB.
2. So, assuming that inflation won't affect us, fixed price for rent will be set in Enum. 
   In case inflation will affect us, instead of changing every time enum value, the global property can be added, 
   which will be used in price calculators for compensating inflation.

3. Also, assuming we won't need to change default amount of days for rental for each type of Film, 
   this also can be set as Enum value.

4. Also, assuming our film inventory will have unique names. In case we have same names, but different films, 
   we will need to add an ID field. Regarding DB, we will have composite key.

5. Also skipping details, such as year, description, etc. of film. This details aren't important to us.

6. I have not focused too much on request validation, it can be easily done by adding hibernate validator
   and marking with corresponding annotation.
7. Exception Handling is done in a user friendly way
8. Also I decided to store data in memory, simulating tables.
9. Totally skipped security, because of time limit.
10. Covered with IT tests flow of renting films
11. Covered with unit tests `RentService` and `FilmRentCalculator`
12. Please don't pay attention to naming of methods with postfix Old. 

* I started with implementing base endpoints, required by the task. 
  Had a question regarding calculation of late returning films and decided to add both, "simple calculation" and "advanced"
  By "simple calculation" I mean sending existing films from system and calculating charges, based on request.
  This can and need to be validated against date of renting films, so that client(another application) won't trick us 
  with wrong data.
  "Advanced calculation" has also validation and Order "table", which stores order, when renting film and checks
  if all films from that order where returned. For the history of orders, the "DB" should be more complex, having more
  tables, which will soft delete orders, by marking them inactive.

* I've also added endpoint, with all possible films, our system has. This should be done as Pageable, 
but I decided not to focus on this.

Application exposes next endpoints:
`GET:/api/films` - queries all films <br/>
`POST:/api/films` - creates new film and adds to "db"<br/>
`POST:/api/rent/price` - calculates price for selected films, Does not rents film.<br/>
`POST:/api/rent/` - rents films<br/>
`POST:/api/rent/return` - returns films, validating if entire order is returned<br/>
`POST:/api/rent/return/old` - calculates price for exceeded days of rent<br/>

I have attached postman collection so you can try application.

Important note! when renting films by calling _POST:/api/rent/_, Id of order is returned for reference with a summary
of ordered films. This id should be used in request, which returns films. Otherwise, validation will fail. 
