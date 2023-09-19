# KALAHA Application

## Overview 
  The implementation consists of 3 parts implemented in Java using Spring Boot:
  
   **kalaha-api:** An implementation of Kalaha Game API with two endpoints:
   - *CreateGame Endpoint:* To create a new Game
   - *Play Endpoint:* To sow specific pit index of the Game
     
   You can find several Tests created to make sure the implementation covers all possible cases within the Kalaha Game.

  **kalaha-ui:** Provides a simple User interface as a web application to consume Kalaha API

  **eureka-server:** provides a service discovery
 
## Technologies
  - Java 11
  - Spring boot
  - Spring Cloud
  - JSF
  - MongoDB
  - Maven

## Installing

1. Clone the repository:
   
       git clone https://github.com/SoheilaAskarzadeh/kalaha-game.git
   
2. Build projects :

      for each project:
    
        cd /path-to-project
   
        mvn clean package
     
     ##### As kalaha-ui using kalaha-api jar for kalaha-api run this command 'mvn clean install', notice that kalaha-ui should build after kalaha-api     
        
4. Run:
   
       cd /path-to-docker-compose
   
       docker-compose build
       docker-compose up
  
## API documentation
After running the project, browse http://localhost:7001/swagger-ui/index.html

## Play the game
After running the project, browse http://localhost:7002/kalaha.xhtml
