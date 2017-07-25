
# Building Application #
To run in development mode without building, with dummy files from `src/test/resources`: `./gradlew bootRun`. Append `-x test` to skip running unit and integration tests.
To build war and jar artifacts: `./gradlew bootRepackage`


# Running Application #
## In Development Mode ##
+ `./gradlew bootRun` (Add `-x test` to skip running unit and integration tests

## In Production Mode ##
+ In "Production" mode with project: `SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun -x test`
  + Note that the override property is passed via environment variable as the Spring convention of `--property.name=value` conflicts with passing arguments to gradle
+ In "Production" mode with standalone jar
  + If jar doesn't already exist, build with `gradlew bootRepackage`
  + Copy jar to destination
  + Launch with `java -jar <jar-name>-<version>.jar --spring.profiles.active=prod`
    + Note that prefixing with an environment value also works
