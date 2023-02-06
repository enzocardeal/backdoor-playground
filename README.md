# backdoor-playground

## Dependencies
**java**: openJDK 17</br>
**OS**: Ubuntu 20.04 (haven' t tested on other OSs)</br>
**Maven**: 3.6.3</br>
**IDEA**: IntelliJ Community Edition 2022.3.1

## JUnit tests
```bash
mvn test-compile
mvn test
```

## Run Application
```bash
mvn package -DskipTests
java -jar target/backdoor-playground-0.0.1-SNAPSHOT.jar
```

## Fuzzing
Inside the folder `target/fuzz-input` put files containing the initial seeds as a string. One seed for each file, with the format `<username>,<password>`.

```bash
mvn compile -DskipTests
mvn jqf:fuzz -Dclass=br.usp.pcs.control.UserFuzzTest -Dmethod=testGetUser -Din=target/fuzz-input/ -Dtime=<time>
```
where `time` should be something like `10m` for 10 minutes of fuzzing and so on.
