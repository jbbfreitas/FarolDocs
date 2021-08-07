# FarolDocs

Esta aplicação foi gerada usando o JHipster 7.1.0, você pode encontrar documentação e ajuda em [https://www.jhipster.tech/documentation-archive/v7.1.0](https://www.jhipster.tech/documentation-archive/v7.1.0).

## Antes de começar

Antes de fazer o build deste projeto, certifique-se que você preparou adequadamente o ambiente:

1. Instalar a versão 11 fo JDK Java. [Baixar o Java JDK 11](https://www.oracle.com/br/java/technologies/javase-jdk11-downloads.html).

2. Instalar o Maven versão 3.8+. [Baixar o Maven](https://maven.apache.org/download.cgi).
3. Instalar o gerenciador de pacotes npm 

```
$ npm install latest-version
```

4. [Node.js](https://nodejs.org/en/): O Node será utilizado para executar o lado cliente da aplicação, que está escrito em Angular, e também é usado para construir o próprio projeto.    


5. Instalar o JHipster

```
$ npm install -g generator-jhipster
```

6. Instalar o Docker Composer. O docker composer será utilizado para criar um servidor elasticSearch. [insstalar o Docker Composer](https://docs.docker.com/)


## Como a aplicação inicial foi gerada

O jhipster é um aplicativo de linha de comando para excutá-lo digite 

```
$ jhipster
```
e em seguida responsa às perguntas, conforme modelo abaixo.

 Which *type* of application would you like to create? Monolithic application (recommended for simple projects)
? What is the base name of your application? FarolDocs
? Do you want to make it reactive with Spring WebFlux? No
? What is your default Java package name? br.com.dev4u.faroldocs
? Which *type* of authentication would you like to use? JWT authentication (stateless, with a token)
? Which *type* of database would you like to use? SQL (H2, PostgreSQL, MySQL, MariaDB, Oracle, MSSQL)
? Which *production* database would you like to use? PostgreSQL
? Which *development* database would you like to use? PostgreSQL
? Which cache do you want to use? (Spring cache abstraction) Ehcache (local cache, for a single node)
? Do you want to use Hibernate 2nd level cache? Yes
? Would you like to use Maven or Gradle for building the backend? Maven
? Do you want to use the JHipster Registry to configure, monitor and scale your application? No
? Which other technologies would you like to use? Elasticsearch as search engine
? Which *Framework* would you like to use for the client? Angular
? Do you want to generate the admin UI? Yes
? Would you like to use a Bootswatch theme (https://bootswatch.com/)? Default JHipster
? Would you like to enable internationalization support? Yes
? Please choose the native language of the application Portuguese (Brazilian)
? Please choose additional languages to install English
? Besides JUnit and Jest, which testing frameworks would you like to use? 
? Would you like to install other generators from the JHipster Marketplace? (y/N) 


## Criando um banco de dados no PostGresql

1. Instale o PostGresql conforme seu SO [Baixar o Postgresql](https://www.postgresql.org/download/)

2. Instale o PGadmin conforme seu SO [Baixar o PGAdmin](https://www.pgadmin.org/download/)

3. Crie com o auxílio do PGadmin, um usuario denominado `farol`, concedendo as permissoes de admin

4. Crie com o auxílio do PGadmin, um banco de dados denominado `FarolDocs`, cujo `owner`é o usuário `farol`.

## Executando a aplicação pela primeira vez

Antes de executar a aplicação você deve fazer duas coisas:

1. Criar o servidor para o elastic search usando o docker composer. Execute a seguinte linha no shell de comandos. 

```
$ docker-compose -f src/main/docker/elasticsearch.yml up -d
```

2. Alterar o arquivo application-dev.yml, conforme abaixo:

```
datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://localhost:5432/FarolDocs
    username: farol
    password: <coloque a senha para o usuário criado por vc>
    hikari:
      poolName: Hikari
      auto-commit: false

```
Executar o maven para compilar, empacotar e executar a aplicação

```
$ mvn
```

Abrir o navegador e digitar a seguinte url

```
http://localhost:8080
```




### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

## Building for production

### Packaging as jar

To build the final jar and optimize the FarolDocs application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.1.0 archive]: https://www.jhipster.tech/documentation-archive/v7.1.0
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.1.0/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.1.0/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.1.0/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.1.0/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.1.0/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.1.0/setting-up-ci/
[node.js]: https://nodejs.org/
[webpack]: https://webpack.github.io/
[angular cli]: https://cli.angular.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[jasmine]: https://jasmine.github.io/2.0/introduction.html
[leaflet]: https://leafletjs.com/
[definitelytyped]: https://definitelytyped.org/
