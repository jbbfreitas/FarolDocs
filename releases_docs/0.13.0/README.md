## Documentação da Realease 0.13.0

Antes de começar, certifique-se de que o seu repositório esteja atualizado até a release `0.12.0` e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a esse estado.

### O que iremos fazer nesta release? 

Nesta release precisaremos alterar a relação de `Documento` com `User` passando a ser de `M:1` e, também, a relação de `Documento` com `Etqieta` passando a ser de `M:1`.

Por que isso?

No modelo atual, ou seja, `M:N`, apesar de ser criada uma tabela no banco de dados, não são criadas, realmente, classes. Isso dificulta a pesquisa usando a tecnologia elastic search.

Portanto, vamos excluir usando o Jhipster as duas relações e recriá-las usando a razão `M:1`.

## Implementando as mudanças

### 1-Excluindo as relações `M:N`

```
jhipster entity Documento


? Do you want to update the entity? This will replace the existing files f
or this entity, all your custom code will be overwritten Yes, remove field
s and relationships
? Please choose the fields you want to remove 
? Please choose the relationships you want to remove etiqueta:many-to-many
, user:many-to-many
? Are you sure to remove these relationships? Yes
```

### 2-Criando duas novas entidades `DocumentoEtiqueta` e `DocumentoUser`

Na verdade vamos substituir dois relacionamentos `M:N` por quatro relacioamentos `M:1`, ou seja, cada relacionamento `M:N` será substituido por dois relacionamentos `M:1`. 

Desta vez vamos usar uma técnica diferente, criando as entidades por intermédio de dois arquivos `.json`.

#### 2.1 Crie dois novos arquivo na pasta `.jhipster`: 

`DocumentoEtiqueta.json`

```json
{
  "fields": [],
  "relationships": [
    {
      "relationshipName": "etiqueta",
      "otherEntityName": "etiqueta",
      "relationshipType": "many-to-one",
      "otherEntityField": "nome"
    },
    {
      "relationshipName": "documento",
      "otherEntityName": "documento",
      "relationshipType": "many-to-one",
      "otherEntityField": "descricao"
    }
  ],
  "service": "serviceImpl",
  "dto": "no",
  "jpaMetamodelFiltering": false,
  "readOnly": false,
  "pagination": "pagination",
  "name": "DocumentoEtiqueta",
  "changelogDate": "20210818190834"
}

```

`DocumentoUser.json`

```json
{
  "fields": [],
  "relationships": [
    {
      "relationshipName": "user",
      "otherEntityName": "user",
      "relationshipType": "many-to-one",
      "otherEntityField": "login",
      "ownerSide": true,
      "otherEntityRelationshipName": "documentoUser"
    },
    {
      "relationshipName": "documento",
      "otherEntityName": "documento",
      "relationshipType": "many-to-one",
      "otherEntityField": "descricao"
    }
  ],
  "service": "serviceImpl",
  "dto": "no",
  "jpaMetamodelFiltering": false,
  "readOnly": false,
  "pagination": "pagination",
  "name": "DocumentoUser",
  "changelogDate": "20210818191506"
}

```

#### 2.2 Execute o comando abaixo para que o Jhipster possa recriar a entidade `DocumentoEtiqueta` 

```
jhipster entity DocumentoEtiqueta

? Do you want to update the entity? This will replace the existing files f
or this entity, all your custom code will be overwritten Yes, re generate 
the entity

```


#### 2.3 Execute o comando abaixo para que o Jhipster possa recriar a entidade `DocumentoUser` 

```
jhipster entity DocumentoUser

? Do you want to update the entity? This will replace the existing files f
or this entity, all your custom code will be overwritten Yes, re generate 
the entity

```

::: :pushpin: Importante :::

Ao final de todo o processo, você terá que ter 93 mudanças no Git.


Salve, compile, execute e teste.

```
mvn
```
