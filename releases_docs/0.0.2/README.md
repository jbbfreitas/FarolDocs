## Documentação da Realease 0.0.2

Nesta release iremos criar a nossa primeira entidade, denominada `Documento`.
Observe que não estamos preocupados em normalização, nem com validação, pois a idéia é criar as melhorias passo a passo.

Para isto digite:

```
jhipster entity Documento
```
A entidade Documento terá os seguintes atributos:

```
================= Documento =================
Fields
projeto (String) 
assunto (String) 
descricao (String) 
etiqueta (String) 
url (String) 
```
Responda às seguintes questões, conforme abaixo:
```
? Do you want to use separate service class for your business logic? Yes, generate a separate service interface and implementation
? Do you want to use a Data Transfer Object (DTO)? No, use the entity directly
? Do you want to add filtering? Not needed
? Is this entity read-only? No
? Do you want pagination and sorting on your entity? Yes, with infinite scroll and sorting headers
````

Execute a aplicação com:

```
$ mvn 
```

Teste todas as suas funcionalidades.

Atualize o índice do ElasticSearch, editando todos os documentos e salvando. Não se preocupe que, posteriomente, iremos fazer uma rotina de atualização automática.


