![InfoMail-logo](https://imgur.com/EyQPoMx.png)

# InfoMail

## 1. About the project
> InfoMail is an email service that will help you:
> 1) Email one or more addresses;
> 2) Schedule email sending on date;
> 3) Resend emails at specified intervals;

## Project presentation
[infomail-presentation  ](https://docs.google.com/presentation/d/1b3__UhovdmZHmPkEundb0FxifjXbJPbz/edit?usp=sharing&ouid=100645824107772636818&rtpof=true&sd=true)

## 2. Where to find front-end part of the project
Here is the front-end part of our project: [infomail-frontend](https://github.com/Lastzlo/Infomail-frontend).

## 3. Start the project locally

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
INFOMAIL_FRONT_URL=http://localhost:4200;
INFOMAIL_FRONT_REG_LINK=http://localhost:4200/#/auth/registration/;
INFOMAIL_SECURITY_SECRET=onfn923oiNkfJj32fsl230cdl3mcLNL42lfl932O23Fknlfsdlsf32f04f
```


5) Set up environmental variables in `application.yml` or in Intellij Idea (`Edit Configurations... -> Environmental variables:`)

<img src="https://imgur.com/UQ8yTCn.png" alt="environment" width="500"/>

Run application and open http://localhost:8028/
