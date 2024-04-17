# Currency Conversion API integration Test

## Introduction 
This repository serves as a demonstration of my skills in Spring WebFlux. Please note that all implementations adhere to the provided instructions.

 ## required tools
  jdk 11, postman for testing the api.
  
## How to set up locally
After cloning this repository, open it in your preferred IDE, 
such as IntelliJ IDEA. 
I recommend running `mvn clean install`. 
Once the test cases have been executed and passed, proceed to the terminal and enter the command `mvn spring-boot:run`
to start the application. Next, import the Postman collection located in the resources folder into your Postman application.
Finally, send a conversion request using the imported collection.

## About external API authentication
The first step in authentication and security is to store credentials in the most secure location available, such as secret engines like Vault, which is my favorite choice. Once securely stored, we can utilize these credentials during API calls using various mechanisms, such as passing the required credentials in headers.
## how can we Improve the application
Using caching is indeed an ideal use case for improving performance. I prefer leveraging external caching solutions like Redis. Configuring Redis to store conversion rates allows us to serve incoming requests directly from the cache, significantly improving response times.
However, it's crucial to remember to refresh the cache periodically by fetching the latest rates from APIs. Implementing a refresh interval, such as every 6 hours, ensures that the cached data remains up-to-date and accurate.

In addition to caching, I'm considering making parallel calls to two endpoints and selecting the response from the endpoint that responds first. 
This approach aims to enhance the application's performance, and thanks to Spring WebFlux, we can easily achieve this.

 