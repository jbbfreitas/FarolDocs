## Documentação da Realease 0.0.6

Antes de começar, certifique-se de que o seu repositório esteja atualizado até a release `0.0.5` e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a esse estado.

Continuando com a nossa técnica da cebola, que doravante chamaremos de **desenvolvimento incremental**, que é o nome tecnicamente mais apropriado, vamos introduzir diversas melhorias no sistema.

### O que iremos fazer nesta release? 

Vamos fazer 5 melhorias na nossa aplicação.

1. Criar 3 novos atributos

Quando lidamos com documento, principalmente quando estamos lidando com normas (Leis, Decretos, Regulamentos etc), poderá ser necessário armazenar o seu **número**, **ano** e a sua **situação**.

É comum no caso de normas que um documento passe por 3 estados: **VIGENTE**, **SUBSTITUIDO** e **CANCELADO**. Esses serão os 3 estágios dos documentos.

2. Excluir na entidade `Documento` o atributo etiqueta

No passo 3 você entenderá melhor por que iremos excluir a atributo `etiqueta`. Na verdade vamos alterar esse atributo passando de **simples** para **atributo de relacioamento**. Acontece que no JHipster não tem como alterar um atributo, você deve excluí-lo e depois incluí-lo com as novas configurações.

3. Criar a entidade Etiqueta e fazer relacionamento com `Documento` na razão `m:n`

Seria conveniente que a nossa aplicação pudesse gerenciar separadamente os tipo de etiquetas, ou seja, uma vez criada uma etiqueta, ela poderia ser utilizada em inúmeras entidades ``Documento``, favorecendo a filtragem pelo atributo etiqueta. Por exemplo: suponha que desejamos saber quantos e quais os documentos estão com a etiqueta (tag) '#Indenização'. Se não tivermos um gerenciamento centralizado das etiquetas pode ocorrer que em determinado documento o usuário escreva '#Indenizacao' ou, ainda, '#Indenizaçao' e, em outro, escreva '#Indenização'. Observe como seria trabalhosa essa filtragem: teríamos que procurar todas as etiquetas que são sinônimas e fazer a filtragem usando o conector 'OU'.

Para evitar esse problema o ideal é criar uma entidade com um único atributo e com a validação **unique**, ou seja, só haverá uma única etiqueta para indenização que poderá ser utilizada em diversos documentos. 

E por que a cardinalidade deve ser m:n? É muito simples, uma instância da entidade Etiqueta poderá estar em diversas instâncias da entidade `Documento`  e uma instância da entidade `Documento` poderá ter diversas instâncias da entidade Etiqueta, ou seja, um relacionamento de muitos para muitos. 

::: :pushpin: Importante :::

No paradigma Orientado a Objetos, é perfeitamente possível fazer esse tipo de relacionamento de muitos para muitos. Mas lembre-se, os dados estão sendo persistidos em um banco de dados relacional: o Postgres. Então é necessário que haja um mapeamento do mundo dos objetos para o mundo das relações e esse é o papel dos frameworks denominados [ORM's](https://blog.geekhunter.com.br/mapeamento-objeto-relacional/). No nosso caso estamos usando o Hibernate. Observe que será criada uma tabela `rel_documento__etiqueta`para transformar o relacionamento m:n em dois relacionamentos m:1. Essa tabela contém uma chave primária composta de duas chaves estrangeiras: `etiqueta_id` e `documento_id`.   

4. Criar a entidade OrgaoEmissor e fazer relacionamento com `Documento` na razão m:1

Essa entidade será responsável por gerenciar todos os órgãos emissores, isto é, os documentos serão emitidos por algum órgão emissor: se for um carnê das Casas Bahia o órgão emissor será a própria Casas Bahia, se for uma norma do TCU o órgão emissor será o TCU etc.

A entidade `Documento` deverá estar relacionada com esta entidade na razão de `m:1`.

5. Criar a entidade TipoNorma e fazer relacionamento com `Documento` na razão m:1


Essa entidade será responsável por gerenciar os diversos tipos de normas existentes, por exemplo: Lei Federal, Lei Municipal, Constituição Federal, ISO 9001, COSO, etc. Pode ocorrer, entretanto, que um determinado documento não seja relacionado a nenhuma norma, por exemplo: carnê das Casas Bahia. Neste caso essa informação deveria ficar em branco, por essa razão o relacionamento não deverá ser obrigatório.

A entidade `Documento` deverá estar relacionada com esta entidade na razão de `m:1`.

## Implementando as mudanças

1. Criar 3 novos atributos: `numero` , `ano` e `situacao`

Na linha de comando do shell digite:

```
jhipster entity `Documento`

? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten Yes, add more fields and relationships

Generating field #6

? Do you want to add a field to your entity? Yes
? What is the name of your field? numero
? What is the type of your field? String
? Do you want to add validation rules to your field? No

Generating field #7

? Do you want to add a field to your entity? Yes
? What is the name of your field? ano
? What is the type of your field? Integer
? Do you want to add validation rules to your field? No

Generating field #8

? Do you want to add a field to your entity? Yes
? What is the name of your field? situacao
? What is the type of your field? Enumeration (Java enum type)
? What is the class name of your enumeration? SituacaoDocumento
? What are the values of your enumeration (separated by comma, no spaces)? VIGENTE,SUBSTITUIDO,CANCELADO
? Do you want to add validation rules to your field? No

Generating field #9

? Do you want to add a field to your entity? No


Generating relationships to other entities

? Do you want to add a relationship to another entity? No
```


2. Excluir em `Documento` o atributo etiqueta

```
jhipster entity `Documento`

? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten Yes, remove fields and relationships
? Please choose the fields you want to remove etiqueta
? Are you sure to remove these fields? Yes

```

3. Criar a entidade Etiqueta e fazer relacionamento com `Documento` na razão m:n

```
jhipster entity Etiqueta

Generating field #1

Generating field #1

? Do you want to add a field to your entity? Yes
? What is the name of your field? nome
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required, Unique

Generating field #2

? Do you want to add a field to your entity? No

```
Agora vamos criar o relacionamento da entidade `Documento` com a entidade Etiqueta

```
$ jhipster entity `Documento`

? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten Yes, add more fields and relationships

Generating field #8

? Do you want to add a field to your entity? No

Generating relationships to other entities

? Do you want to add a relationship to another entity? Yes
? What is the name of the other entity? Etiqueta
? What is the name of the relationship? etiqueta
? What is the type of the relationship? many-to-one
? When you display this relationship on client-side, which field from 'Etiqueta' do you want to use? This field will be displayed as a String, so it cannot be a Blob nome
? Do you want to add any validation rules to this relationship? No

```



4. Criar a entidade OrgaoEmissor e fazer relacionamento com `Documento` na razão m:1

```
jhipster entity OrgaoEmissor

Generating field #1

? Do you want to add a field to your entity? Yes
? What is the name of your field? sigla
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required, Unique

Generating field #2

? Do you want to add a field to your entity? Yes
? What is the name of your field? nome
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required

Generating field #3

? Do you want to add a field to your entity? No

? Do you want to use separate service class for your business logic? Yes, generate a separate service interface and implementation
? Do you want to use a Data Transfer Object (DTO)? No, use the entity directly
? Do you want to add filtering? Not needed
? Is this entity read-only? No
? Do you want pagination and sorting on your entity? Yes, with infinite scroll and sorting headers
```

Agora vamos criar o relacionamento da entidade `Documento` com a entidade OrgaoEmissor

```
$ jhipster entity `Documento`

? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten Yes, add more fields and relationships

Generating field #8

? Do you want to add a field to your entity? No

Generating relationships to other entities

? Do you want to add a relationship to another entity? Yes
? What is the name of the other entity? OrgaoEmissor
? What is the name of the relationship? orgaoEmissor
? What is the type of the relationship? many-to-one
? When you display this relationship on client-side, which field from 'OrgaoEmissor' do you want to use? This field will be displayed as a String, so it cannot be a Blob sigla
? Do you want to add any validation rules to this relationship? No

Generating relationships to other entities

? Do you want to add a relationship to another entity? No

```

5. Criar a entidade TipoNorma e fazer relacionamento com `Documento` na razão m:1

```
jhipster entity TipoNorma

Generating field #1

? Do you want to add a field to your entity? Yes
? What is the name of your field? tipo
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required

Generating field #2

? Do you want to add a field to your entity? Yes
? What is the name of your field? descricao
? What is the type of your field? String
? Do you want to add validation rules to your field? No

Generating field #3

? Do you want to add a field to your entity? No

? Do you want to add a relationship to another entity? No

? Do you want to use separate service class for your business logic? Yes, generate a separate service interface and implementation
? Do you want to use a Data Transfer Object (DTO)? No, use the entity directly
? Do you want to add filtering? Not needed
? Is this entity read-only? No
? Do you want pagination and sorting on your entity? Yes, with infinite scroll and sorting headers

```

Agora vamos criar o relacionamento da entidade `Documento` com a entidade TipoNorma

```
$ jhipster entity `Documento`

? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten Yes, add more fields and relationships

Generating field #8

? Do you want to add a field to your entity? No

? Do you want to add a relationship to another entity? **Yes**
? What is the name of the other entity? TipoNorma
? What is the name of the relationship? tipoNorma
? What is the type of the relationship? many-to-one
? When you display this relationship on client-side, which field from 'TipoNorma' do you want to use? This field will be displayed as a String, so it cannot be a Blob tipo
? Do you want to add any validation rules to this relationship? No

? Do you want to add a relationship to another entity? No

```

Como fizemos diversas mudanças, teremos que fazer uma pequena intervenção no banco de dados. Para isso, abra o PGAdmin, selecione o menu Tools|Query Tools e depois digite e execute:

```
delete from databasechangelog where orderexecuted >=7;
drop table documento cascade;
drop table projeto cascade;
drop table tipo cascade;
```
Recrie o docker do elastic search com:

```
docker-compose -f src/main/docker/elasticsearch.yml up -d
```

Altere a versão Snapshot para 0.0.6

Execute a aplicação e teste para verificar se as mudanças realizadas estão corretas

```
$ mvn clean install
```

```
mvn
```






*** Final da release 0.0.6 ***


