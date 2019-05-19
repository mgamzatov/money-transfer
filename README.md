# Money Transfer App
##### Sample REST application for transferring money between accounts

## API
get all Accounts
```
curl -X GET \
  http://host:port/accounts
``` 
##
get Account by id
```
curl -X GET \
  http://host:port/accounts/:id
```
##
transfer money between accounts
```
curl -X POST \
  http://host:port/accounts/transfer \
  -H 'Content-Type: application/json' \
  -d '{
	"fromAccountId": 1,
	"toAccountId": 2,
	"amount": 100
}'
```

## Run App
By default app runs on port 8080
#### With JAR
```java -jar MoneyTransferTest-1.0-SNAPSHOT.jar```

or to change port

```java -jar MoneyTransferTest-1.0-SNAPSHOT.jar --port 8081```

#### With Gradle
```./gradlew runServer```

or to change port

```./gradlew runServer -Pport=8081```