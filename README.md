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

? Which *type* of authentication would you like to use? JWT authentication (stateless, 
with a token)

? Which *type* of database would you like to use? SQL (H2, PostgreSQL, MySQL, MariaDB, 
Oracle, MSSQL)

? Which *production* database would you like to use? PostgreSQL

? Which *development* database would you like to use? PostgreSQL

? Which cache do you want to use? (Spring cache abstraction) Ehcache (local cache, for 
a single node)

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

1. Instale o PostGresql conforme seu SO. [Baixar o Postgresql](https://www.postgresql.org/download/)

2. Instale o PGadmin conforme seu SO. [Baixar o PGAdmin](https://www.pgadmin.org/download/)

3. Crie, com o auxílio do PGadmin, um usuario denominado `farol`, concedendo as permissoes de admin.

4. Crie, com o auxílio do PGadmin, um banco de dados denominado `FarolDocs`, cujo `owner`é o usuário `farol`.

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
Depois de fazer as duas tarefas acima, execute o `maven` para compilar, empacotar e executar a aplicação.

```
$ mvn
```

Se tudo ocorreu bem, abra o navegador e digitar a seguinte url

```
http://localhost:8080
```
