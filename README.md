![InfoMail-logo](https://imgur.com/EyQPoMx.png)

# InfoMail

## About the project

> InfoMail is an email service that will help you:
> 1) Email one or more addresses;
> 2) Schedule email sending on date;
> 3) Resend emails at specified intervals;
> 4) Creating and sharing templates

## Specifics

1) **JWT**-token authentication
2) _Two-factor_ authentication
3) Real _emails_ sending
4) _Docker-compose_  with all services

### Project links

* [infomail-presentation  ](https://docs.google.com/presentation/d/1b3__UhovdmZHmPkEundb0FxifjXbJPbz/edit?usp=sharing&ouid=100645824107772636818&rtpof=true&sd=true)
  - project presentation
* [infomail](http://195.250.62.211:8027/) - InfoMail site
* [mailHog](http://195.250.62.211:8026/) - MailHog mail service (when you register on the site or send a message)
* [swagger-ui](http://195.250.62.211:8028/swagger-ui/index.html?configUrl=/api/v1/api-docs/swagger-config) - API
* [infomail-back](https://hub.docker.com/repository/docker/shutuper/infomail-back) - DockerHub

### Front-end part of the project

* [infomail-frontend](https://github.com/Lastzlo/Infomail-frontend) - GitHub
* [infomail-frontend](https://hub.docker.com/repository/docker/shutuper/infomail-front) - DockerHub

## Start the project locally

### Required to install

* Java 17
* PostgreSQL (9.5.9 or higher)
* Docker
* Mail sender Gmail SMTP or MailHog

### Installing

1) Clone this repository to your local machine using:

```shell
git clone https://github.com/shutuper/InfoMail.git
```

2) Create a database
3) Set-up Gmail SMTP or MailHog
4) Create environmental variables that are defined in `application.yml`

```properties
MAIL_HOST=localhost;MAIL_PORT=1025;
MAIL_EMAIL=infomail;MAIL_PASSWORD=infomail;
DB_HOST=localhost:8026;
DB_USERNAME=infomail;
DB_PASSWORD=infomail;
INFOMAIL_DEFAULT_TIMEZONE=Europe/Kiev
INFOMAIL_FRONT_URL=http://localhost:4200;
INFOMAIL_FRONT_REG_LINK=http://localhost:4200/#/auth/registration/;
INFOMAIL_SECURITY_SECRET=onfn923oiNkdsdfsl23dasdasNL42lfl932O23Fknlfsdlsf32f04f
```

5) Set up environmental variables in `application.yml` or in Intellij
   Idea (`Edit Configurations... -> Environmental variables:`)

<img src="https://imgur.com/UQ8yTCn.png" alt="environment" width="500"/>

Run application and open http://localhost:8028/
