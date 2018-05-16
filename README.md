# N26 Code Challenge #
This application resolves the proposed code challenge. 
### My approach to the problem ###
Since I need to keep tracking of the statistic from the last 60 seconds, I decided to utilize an array as the data structure with capacity of 60.
Each spot in the array corresponds to a second of the last minute. So, each spot has the statistic related to a second in the last minute.
To illustrate, below is a sequence of events with the corresponding results:
``` 
POST transactions 
{"amount": 12.89, timestamp: long corresponding to 10:50:57} 
```
`Result: array[56] = 12.89`
``` 
POST transactions 
{"amount": 45.77, timestamp: long corresponding to 10:50:57} 
```
`Result: array[56] = 69.66`
``` 
POST transactions 
{"amount": 22.74, timestamp: long corresponding to 10:51:57} 
```
`Result: array[56] = 22.74`
``` 
POST transactions 
{"amount": 8.42, timestamp: long corresponding to 10:51:49} 
```
`Result: array[49] = 8.42`
`Result: array[56] = 22.74`

In this manner the operation to add a transaction has constant time complexity O(1) since I only need to get an element in the array.
To get the current statistic we need to make some calculations: iterate over the array and, for each element, aggregate the value in the result only if that statistic is related to a timestamp within the last minute. Thus, It has constant time complexity O(1) as the array has fixed size..
The solution has constant memory complexity O(1) also since the array has fixed size.

### Running the Application ###
To run the project execute the command (the project executes on port 8080):
``` 
./mvnw spring-boot:run
```
