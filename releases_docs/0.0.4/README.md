## Documentação da Realease 0.0.4

Nesta release nós iremos:

1. Modificar a Entidade Documento para conter mais um atributo denominado `ementa` do tipo CLOB. Este campo será utilizado para informar um resumo do documento, de tal modo que não seja preciso abrir o documento para se saber do que se trata.

2. Criar uma entidade denominada `Projeto` com os seguintes atributos:

```
nome: String, validação (not null e unique)
inicio:Instant, validação (not null)
fim:Instant
situacao: Enum com os seguintes tipos: INICIADO, PAUSADO, CANCELADO, FINALIZADO
```

3. Alterar a entidade Documento, excluindo o atributo `projeto`

4. Alterar a entidade Documento para conter um relacionamento com a entidade Projeto (m:1)

## Implementando as mudanças

Poderíamos fazer todas essas mudanças diretamente no código fonte. Essa opção, entretanto, seria muito trabalhosa pois teríamos que alterar as várias classes Java e também as várias classes do Angular.

Para implementar as mudanças com um mínimo de intervenção vamos deixar o jhipster trabalhar por nós.

1. Certifique-se de que o seu repositório esteja atualizado e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a este estado. 

2. Criando o atributo `ementa`

```
$ jhipster entity Documento
```
o Jhipster irá responder com:

```
? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten 
  Yes, re generate the entity 
❯ Yes, add more fields and relationships 
  Yes, remove fields and relationships 
  No, exit 
  ```
Selecione a segunda linha, visto que queremos adicionar um novo artibuto (`ememnta`).

Responda às questões seguintes conforme o texto abaixo:

```
Generating field #6
? Do you want to add a field to your entity? Yes
? What is the name of your field? ementa
? What is the type of your field? [BETA] Blob
? What is the content of the Blob field? A CLOB (Text field)
? Do you want to add validation rules to your field? No
```

Observe que o tipo do campo é BLOB e CLOB pois queremos poder armazenar um texto longo e com formatação rica (negrito, itálico, sublinhado etc).

3. Criando a entidade `Projeto`

::: :pushpin: Importante :::

Neste tópico, preste atenção nas letras MAIÚCULAS e minúsculas, pois o Java é `Case Sensitive`  

```
$ jhipster entity Projeto
```

````
Generating field #1

? Do you want to add a field to your entity? Yes
? What is the name of your field? nome
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required, Unique
````

```
Generating field #2

? Do you want to add a field to your entity? Yes
? What is the name of your field? inicio
? What is the type of your field? Instant
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required

```

```
Generating field #3

? Do you want to add a field to your entity? Yes
? What is the name of your field? fim
? What is the type of your field? Instant
? Do you want to add validation rules to your field? No
```


```
Generating field #4

? Do you want to add a field to your entity? Yes
? What is the name of your field? situacao
? What is the type of your field? Enumeration (Java enum type)
? What is the class name of your enumeration? SituacaoProjeto
? What are the values of your enumeration (separated by comma, no spaces)? INICIADO,PAUSADO,CANCELADO,FINALIZADO
? Do you want to add validation rules to your field? No
```

```
Generating field #5

? Do you want to add a field to your entity? No
```

```
? Do you want to use separate service class for your business logic? Yes, generate a separate service interface and implementation
? Do you want to use a Data Transfer Object (DTO)? No, use the entity directly
? Do you want to add filtering? Not needed
? Is this entity read-only? No
? Do you want pagination and sorting on your entity? Yes, with infinite scroll and sorting headers
```
3. Alterar a entidade Documento, excluindo o atributo `projeto`
```
$ Jhipster entity Documento
```

```
? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten 
  Yes, re generate the entity 
  Yes, add more fields and relationships 
❯ Yes, remove fields and relationships 
  No, exit 
```

```
? Please choose the fields you want to remove 
❯◉ projeto
 ◯ assunto
 ◯ descricao
 ◯ etiqueta
 ◯ url
 ◯ ementa
 ```

```
? Are you sure to remove these fields? Yes
```

4. Alterar a entidade Documento para conter um relacionamento com a entidade Projeto (m:1)

```
$ jhipster entity Documento
```

```
? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten 
  Yes, re generate the entity 
❯ Yes, add more fields and relationships 
  Yes, remove fields and relationships 
  No, exit 
```
```
? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten Yes, add more fields and relationships

```
```
Generating field #6

? Do you want to add a field to your entity? No

```
```
Generating relationships to other entities

? Do you want to add a relationship to another entity? Yes
? What is the name of the other entity? Projeto
? What is the name of the relationship? projeto
? What is the type of the relationship? many-to-one
? When you display this relationship on client-side, which field from 'Projeto' do you want to use? This field will be displayed as a String, so it cannot be a Blob nome
? Do you want to add any validation rules to this relationship? No
```

5. Como fizemos várias alterações na entidade `Documento` teremos que fazer duas intervenções no banco de dados.

- Excluir a tabela `Documento` no banco de dados. Isso é necessário para que o `liquibase` recrie a tabela com os campos conforme nossa alteração. 

::: :pushpin:  Importante :::

> Atenção em um projeto grande com dados já inseridos na tabela, lembre-se de preservar  os seus dados antes de excluir a tabela. Uma sugestão é criar uma tabela temporária (uma cópia da tabela Documento) para depois fazer um comando `insert from`.

Para excluir a tabela Documento, no PGAdmin selecione o menu Tools|Query Tools e depois digite:

```
DROP TABLE DOCUMENTO CASCADE;
```

- Excluir as duas ultimas linhas da tabela `databasechangelog`

Para excluir as duas ultimas linhas da tabela `databasechangelog`, no PGAdmin selecione o menu Tools|Query Tools e depois digite:

```
select * from databasechangelog;
```

Anote o valor do conteúdo do campo `orderexecuted` na penúltima linha :

```
delete from databasechangelog where orderexecuted >= **coloque aqui o  valor anotado por você**;

select * from databasechangelog;

```
6. Execute a aplicação

```
$ mvn
```

7. Selecione a Entidade Projeto e exclua os 7 últimos registros.

8. Altere a Entidade Projeto, para que a situação de todos os projetos seja `INICIADO`

9. Teste a entidyade Documento, alterando a `ementa` e o `projeto`.

10. Edite e salve todas os registros de Documento (para atualizar o ElasticSearch)

11. Pesquise um determinado documento










