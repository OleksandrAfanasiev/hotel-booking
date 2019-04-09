This is simple booking rest application based on Spring Boot, Spring MVC, Spring Data and H2. For launching app you need to copy repository and run Application.class, when application starts in DB automatically insert default data for testing.
Application capability: show free rooms for specified days, show free rooms for specified days filtered by room category, show all rooms filtered by room category, book room for user on specified days, show all information about bookings by user, show total price for booking by user,show total price by reservation, show all hotel reservations, create user.

#curl requests for testing

#get free rooms between 2019-08-10 and 2019-08-16
curl -H "Content-Type: application/json" -X POST -d '{"startDate":"2019-08-10", "endDate": "2019-08-16"}' localhost:8080/room/filter/by/period

#get rooms filtered by period: benween 2019-08-10 and 2019-08-16 and category: SINGLE
curl -H "Content-Type: application/json" -X POST -d '{"period":{"startDate":"2019-08-10", "endDate": "2019-08-16"}, "categoryType": "SINGLE"}' localhost:8080/room/filter/by/period/category

#get rooms filtered by category: SINGLE 
curl localhost:8080/room/filter/by/category/SINGLE

#book room with id: 16 for user with id: 2 for 6 nights from 2019-08-10 to 2019-08-16
curl -H "Content-Type: application/json" -X POST -d '{"roomId": 16, "userId": 2, "period":{"startDate":"2019-08-10", "endDate": "2019-08-16"}}' localhost:8080/booked/create

#get all booked rooms by user with id: 1
curl localhost:8080/booked/by/user/1

#get total price for user with id: 1
curl localhost:8080/booked/total/price/by/user/1

#get total price by reservation with id: 1
curl localhost:8080/booked/total/price/by/reservation/1

#get all hotel reservations
curl localhost:8080/booked

#create new user with username: Ralph
curl -H "Content-Type: application/json" -X POST -d 'Ralph' localhost:8080/user/create
