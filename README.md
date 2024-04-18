# Recipe Generation Application

This application is designed to generate recipes based on the ingredients you have in your kitchen. The application will take in a list of ingredients and output a list of recipes that can be made with those ingredients

The Idea is to have a place where people can explore new recipes and try new things without having to go to the store to buy new ingredients. This application will help people use up the ingredients they have in their kitchen and reduce food waste.
## Guidelines
We will use Conventional Commit for our Commit Messages and Branch Naming 
[Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)

## Local Setup

In order to run this application locally, you will need to have the following:
* JDK 21
* Maven
* OpenAI API Key in env (variable `OPENAI_API_KEY`)
* Google OAuth Client ID in env (file `application-local.properties`)
* Google OAuth Client Secret in env (variable `application-local.properties`)

In order to set these variables copy the `application-loca-template.properties` file to `application-local.properties` and fill in the values

```properties

Running the projects
```zsh
mvn spring-boot:run
#or
./mvnw spring-boot:run
```
Alternatively run via Spring Boot run command
But change run config to use VM Options of
```
-Dpebble.cache=false
```
And also use local Active Profile to use the secret env variable located in application-local.properties

## Project Structure
In order to keep the Code Clean we will use following Structure to sector our Code

* controllers (url endpoints to serve application)
* adapters (connect to external api)
* services (business logic)
* configs (general config of application)
* models (data representation objects)
* repositories (connect to databases)
* utils (general utils)

## API Reference

#### Generate Ripe

```http
  POST /api/recipe
```

| Parameter       | Type      | Description                   |
|:----------------|:----------|:------------------------------|
| `ingredients`   | `string`  | Comma seperated list of Ingredients |
| `generateImage` | `boolean` | Should an Image be generated  |
