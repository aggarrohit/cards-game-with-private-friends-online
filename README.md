# CardRift - UNO multiplayer game - play with friends online

## Table of Contents

- [About](#About)
- [Getting Started](#Getting-Started)
   - [JAR File](#JAR-File)
   - [Docker](#Docker)
   - [AWS](#Hosted-in-AWS)
- [Project Managment](#implementation-and-design)
   - [Architecture](#Architecture)
   - [Model Diagram](#model-diagram)
   - [Class Diagram](#class-diagram)
- [Technologies and tools used in project](#Technologies-and-tools-used-in-project)
- [Collaboration](#collaboration)
- [Future improvement](#future-improvement)


## About
CardRift is a online platform with backend on spring boot and frontend on react native.
2,3 or 4 players can play this game online.
It's an uno cards game with no time limitations and no restrictions.
Rules are implemented to simulate real scenarios and keep the excitement of real game.

How to play the game:

**Mobile User:**
1. Register with name 
2. Create table with entering number of players (2,3 or 4) and share the received table id
3. If someone else has created the table and you just want to join then enter table id and join the table
4. As soon as all the players joins, the game will start with each player having 7 cards

**Type of cards:**
1. Number cards with 4 colours
2. Wild cards (to change active colour)
3. Draw 2 card
4. Wild draw 4 card
5. Skip card
6. Reverse card


## Getting Started

To get started with the CardRift, follow these steps:
### JAR-File 
1. **Clone Repositories:**
- git clone https://github.com/aggarrohit/cards-game-with-private-friends-online
2. **Install Dependencies:**
- cd cardrift
- mvn install
3. **Run the JAR file:**
-~~~~ java -jar ----------------------------------
### Docker
- cd cardrift
- docker build --tag docker_image_name .
- docker run -p 8080:8080 -d docker_image_name


## Project Management

### Architecture :
![Planning.png](project_management%2Fplanning.png)



## Technologies and tools used in project
1. Java 17 
2. Spring Boot 3.1.4
3. Maven
4. Docker 
5. AWS 


## Future improvement
1. Error handling
2. Data persistence with database
3. Logs
