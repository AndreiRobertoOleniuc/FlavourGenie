# Recipe Generation Application

This application is designed to generate recipes based on the ingredients you have in your kitchen. The application will take in a list of ingredients and output a list of recipes that can be made with those ingredients

The Idea is to have a place where people can explore new recipes and try new things without having to go to the store to buy new ingredients. This application will help people use up the ingredients they have in their kitchen and reduce food waste.
## Guidelines
We will use Conventional Commit for our Commit Messages and Branch Naming
[Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)

## Local Setup

In order to run this application locally (same rules apply for /test where `application-local.properties` also needs to be created ),
you will need to have the following:
* JDK 21
* Maven
* OpenAI API Key in env (variable `OPENAI_API_KEY`)
  * ```
    OPENAI_API_KEY=your-api-key
    ```
* Google Application Credential file and path in env (variable `GOOGLE_APPLICATION_CREDENTIALS`)
  * ```
    GOOGLE_APPLICATION_CREDENTIALS=/path/to/your/credential.json 
    ```
* Google OAuth Client ID in env (file `application-local.properties`)
* Google OAuth Client Secret in env (file `application-local.properties`)
* GitHub OAuth Client ID in env (file `application-local.properties`)
* GitHub OAuth Client Secret in env (file `application-local.properties`)


In order to set these variables copy the `application-loca-template.properties` file to `application-local.properties` and fill in the values

Running the projects, you can use the following command
```zsh
mvn spring-boot:run -Dspring.profiles.active=local -Dpebble.cache=false
#or
./mvnw spring-boot:run -Dspring.profiles.active=local -Dpebble.cache=false
```
Note if the local profile is not set, the application will not start. If these commands don't work paste contents of `application-local.properties` into `application.properties` and run the following command
```zsh
mvn spring-boot:run -Dpebble.cache=false
```

## Project Structure
In order to keep the Code Clean we will use following Structure to sector our Code

* controllers (url endpoints to serve application)
* adapters (connect to external api)
* services (business logic)
* configs (general config of application)
* models (data representation objects)
* repositories (connect to databases)
* utils (general utils)