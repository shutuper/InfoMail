![InfoMail-logo](https://imgur.com/EyQPoMx.png)

# InfoMail  [![Build Status](https://travis-ci.com/ita-social-projects/GreenCity.svg?branch=master)](https://travis-ci.com/ita-social-projects/GreenCity) [![Github Issues](https://img.shields.io/github/issues/ita-social-projects/GreenCity?style=flat-square)](https://github.com/ita-social-projects/GreenCity/issues) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ita-social-projects-green-city&metric=coverage)](https://sonarcloud.io/dashboard?id=ita-social-projects-green-city) [![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

## 1. About the project
> InfoMail is an email service that will help you:
> 1) Email one or more addresses;
> 2) Schedule email sending on date;
> 3) Resend emails at specified intervals;
> 4) Import email list using format files: `CSV, XLSX`
> 5) Import message format: `MSG`

## 2. Where to find front-end part of the project
Here is the front-end part of our project: [infomail-client]().

## 3. Start the project locally

These instructions will get you a copy of the project up and running backend on your local machine for development and testing purposes.

Also in our **[wiki]()** we can find: 
* [how to start the project with front-end](); 
* [how to start the project at the Docker]().


### Required to install
* Java 17
* PostgreSQL (9.5.9 or higher)
* Google email with opened SMTP ([how to open SMTP]())

### Installing
1) Clone this repository to your local machine using:
```shell
git clone https://github.com/shutuper/InfoMail.git
```
2) Create a database
3) Open SMTP on Google email [link]()
4) You should create environmental variables that are defined in `application.yml`
```properties
spring.mail.username=${EMAIL_ADDRESS}
spring.mail.password=${EMAIL_PASSWORD}
spring.datasource.url=${DATASOURCE_URL}
spring.datasource.username=${DATASOURCE_USER}
spring.datasource.password=${DATASOURCE_PASSWORD}
application.email.sentFrom=${EMAIL_ADDRESS}
application.security.secret=${INFOMAIL_SECRET}
```

5) Set up environmental variables in `application.yml` or in Intellij Idea (`Edit Configurations... -> Environmental variables:`)

<img src="https://imgur.com/UQ8yTCn.png" alt="environment" width="500"/>

Run application and open http://localhost:8080/
