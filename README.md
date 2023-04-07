# backdoor-playground
## From other repository
The instrument folder is an adaptation of part of the code found in: https://github.com/rohanpadhye/JQF

## Dependencies
**java**: openJDK 17</br>
**OS**: Ubuntu 20.04 (haven' t tested on other OSs)</br>
**Maven**: 3.6.3</br>
**IDEA**: IntelliJ Community Edition 2022.3.1</br>
**Desktop Docker**: 4.17.0</br>

## JUnit tests
```bash
cd backdoor-playground
mvn test-compile
mvn test
cd ..
```

For correct packaging, Maven lifecycle `package` should be run via IntelliJ because we used it tools for creating the frontend.

```bash
mvn package
```

## Run Application
```bash
cd backdoor-playground
docker-compose up -d
java -jar target/backdoor-playground.jar
cd ..
```

## Fuzzing
Inside the folder `target/fuzz-input` put files containing the initial seeds as a string. One seed for each file, with the format `<username>,<password>`.

```bash
cd backdoor-playground
mvn compile
mvn jqf:fuzz -Dclass=br.usp.pcs.control.UserFuzzTest -Dmethod=testGetUser -Din=target/fuzz-input/ -Dtime=<time>
cd ..
```
where `time` should be something like `10m` for 10 minutes of fuzzing and so on.

## Get Coverage
```bash
./run.py -s <packages_in_scope> -i <packages_to_ignore>
```
Exemple: `./run.py -s br/usp/pcs -i br/usp/pcs/view br/usp/pcs/main`

## Run Application Instrumented After Getting Coverage
```bash
cd backdoor-playground
./run.py
cd ..
```
