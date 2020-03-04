# Czujka
Slack Bot for notification of office closings

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
This project is a simple notification application connected to the [Slack](https://slack.com/intl/en-pl/?eu_nc=1) communicator.
Application sends information about the room status and allows you to sign up for the queue. The last person is informed 
that he's closing the room. 
For proper operation ESP8266 microcontroller (or another with WiFi module) with light sensor is required. 
The microcontroller is used to notify the server of the room status.


![]( src/main/resources/unknown.png)

### Available endpoints
* /zamykam {hh/mm} - subscribing to the queue
* /lista - queue display
* /wypisz - unsubscribing from the queue
* /biuro - checking room status
	
## Technologies
Project is created with:
* Java version 8
* SpringBoot version 2.1.4
* Maven version 3.6.3
* MySQL version 8.0.15
	
## Setup
To run this project, install it locally using maven:

```
$ mvn package
$ cd target
$ java -jar <projectName>
```


## Authors
* Piotr Piasecki - [Vattier56](https://github.com/Vattier56)
* Tobiasz Nartowski - [TobyNartowski](https://github.com/TobyNartowski)
* Michał Młodawski - [SimpleMethod](https://github.com/SimpleMethod)
    
