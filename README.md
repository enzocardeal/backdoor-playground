# backdoor-playground
This repo represents only the backend of an application with some backdoor vulnerabilities
## From other repository
The instrument folder is an adaptation of part of the code found in: https://github.com/rohanpadhye/JQF

## Dependencies
**java**: openJDK 17</br>
**OS**: Ubuntu 20.04 (haven' t tested on other OSs)</br>
**Maven**: 3.6.3</br>
**Desktop Docker**: 4.17.0</br>

## JUnit tests
```bash
cd backdoor-playground
mvn test-compile
mvn test
cd ..
```

For packaging:

```bash
mvn package
```

## Fuzzing
Inside the folder `/backdoor-playground/resources/fuzz-input` There are the files used as seeds for each fuzzing test. If you'd like to add more seeds, just add a new file on the corresponding folder, following the sintax of the other files.

```bash
mvn compile
./fuzz.py -f <time>
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
docker-compose up -d
./run.py
cd ..
```

## Using Application
As stated previously, this is only the backend of a web app. It as a rest API, with this calls avalible:
### POST methods:
- /api/user/signup
  - payload:
  ```JSON
  {
	"username": "username of your choice",
	"password": "password of your choice"
  }
  ```
- /api/user/login
  - playload:
  ```JSON
  {
	"username": "username of your choice",
	"password": "password of your choice"
  }
  ```

