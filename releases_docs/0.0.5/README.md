## Documentação da Realease 0.0.5

Antes de começar, certifique-se de que o seu repositório esteja atualizado até a release `0.0.4` e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a este estado.

Continuando com o projeto FarolDocs, vamos introduzir novas melhorias no sistema. Esta técnica chama-se técnica da cebola, em uma alusão às cebolas que tem diversas cascas até chegar ao núcleo. Segundo essa técnica vamos tirando as cascas, que são as imperfeições, até chegarmos ao núcleo  - uma aplicação otimizada.

Estamos na release 0.0.5, ou seja a 5ª casca da nossa cebola. Teremos que chegar à versão 1.0.0, mas não se preocupe, não serão 100 releases! Algumas releases irão alterar tão profundamente a aplicação que irão ser nomeadas no segundo dígito e não no terceiro como estamos fazendo até então.

### O que iremos fazer nesta release? 

Vamos fazer 4 melhorias na nossa aplicaçã0.

#### 1-Criar a Entidade Tipo

Vamos criar uma nova entidade, denominada `Tipo`. Nessa entidade iremos tabelar os vários tipos de documentos que podem ser gerenciados pela nossa aplicação. Classificar desta forma irá facilitar a nossa pesquisa.

Lembre-se, estamos criando uma aplicação para gerenciar documentos na nuvem, cujo objetivo principal é ter acesso rápido e seguro aos documentos sem ter que ficar guardando os documentos em infindáveis pastas no sistema de arquivos.

A entidade `Tipo`terá apenas dois atributos: `nome` e `descricao`.

#### 2-Criar na entidade Documento o relacionamento m:1  com a entidade Tipo

Já que estamos criando uma aplicação normalizada, o ideal é que façamos uma associação entre a entidade `Documento`e a entidade `Tipo` com a [cardinalidade](https://www.ic.unicamp.br/~santanch/teaching/oop/2015-1/slides/poo0401-agregacao-v02.pdf) na razão de `m:1`.

Dessa forma, a nossa entidade `Documento` terá mais um atributo, denominado `tipo`, que, na verdade, corresponde a uma instância do objeto da classe `Tipo`.

No jargão de banco de dados relacionais, podemos dizer que a tabela `Documento` tem uma chave estrangeira, denominada `tipo_id` que é, justamente, o relacionamento com a chave primária `id` da tabela `Tipo`.

#### 3- Incluir a validação por regex no atributo url 

Na versão inicial de nossa aplicação, fizemos questão de não exigir qualquer tipo de validação, exatamente para que a primeira versão fosse a mais simples possível. Agora nesta release nós iremos exigir que o usuário informe uma url que obedeça a determinadas regras de validação. No nosso caso iremos incluir uma regra de validação baseada em expressões regulares, ou `regex` (forma abreviada do ingles: regular expressions). Para saber mais sobre expressões regulares consulte esta url [que contém exemplos de expressões regulares em java.](https://www.devmedia.com.br/conceitos-basicos-sobre-expressoes-regulares-em-java/27539)


#### 4-Alterar a versão Snapshot para 0.0.5

Sempre que você disponibilzar uma versão de sua aplicação, é muito importante informar ao usuário, logo na janela principal, qual a versão ele está utilizando. Existem ferramentas de `build`, como o [jenkis](https://github.com/jenkinsci/jenkins), por exemplo, que fazem esse incremento de forma automática. Para não complicar neste momento vamos fazer isso de forma manual.  

## Implementando as mudanças

Mais uma vez, poderíamos fazer todas as mudanças diretamente no código, mas já que estamos utilizando uma ferramenta de [scaffold](https://pt.stackoverflow.com/questions/119731/o-que-%C3%A9-scaffold) como o Jhipster, vamos deixar que ela trabalhe por nós até que seja inevitável uma intervenção manual.

#### 1-Criar a Entidade Tipo

Criar uma entidade denominada `Tipo` com os seguintes atributos:

```
nome: String, validação (not null e unique)
descricao: String, sem nehuma validação
```


```
$ jhipster entity Tipo
Generating field #1

? Do you want to add a field to your entity? Yes
? What is the name of your field? nome
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Required, Unique

Generating field #2

? Do you want to add a field to your entity? Yes
? What is the name of your field? descricao
? What is the type of your field? String
? Do you want to add validation rules to your field? No

Generating field #3

? Do you want to add a field to your entity? No

? Do you want to use separate service class for your business logic? Yes, generate a separate service interface and implementation
? Do you want to use a Data Transfer Object (DTO)? No, use the entity directly
? Do you want to add filtering? Not needed
? Is this entity read-only? No
? Do you want pagination and sorting on your entity? Yes, with infinite scroll and sorting headers

Everything is configured, generating the entity...
```

#### 2-Criar na entidade Documento o relacionamento m:1  com a entidade Tipo

```
$ jhipster entity Documento
? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten Yes, add more fields and relationships
Generating field #6

? Do you want to add a field to your entity? No

Generating relationships to other entities

? Do you want to add a relationship to another entity? Yes
? What is the name of the other entity? Tipo
? What is the name of the relationship? tipo
? What is the type of the relationship? many-to-one
? When you display this relationship on client-side, which field from 'Tipo' do you want to use? This field will be displayed as a String, so it cannot be a Blob nome
? Do you want to add any validation rules to this relationship? No

```
#### 3- Incluir a validação por regex no atributo url 

Existem duas formas de fazer isso. A primeira seria alterar o arquivo `Documento.json`, para incluir a validação e, em seguida, regerar a entidade. Isso seria muito simples de fazer, mas lembre-se: por enquanto não estamos lidando diretamente com o código.

A segunda forma, é excluir o atributo `url` e incluí-lo novamente com a validação baseada em `regex`. É o que iremos fazer.


 - Excluindo a atributo url
```
$ jhipster entity Documento
? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten Yes, remove fields and relationships
? Please choose the fields you want to remove url
? Are you sure to remove these fields? Yes

```

- Incluindo o atributo url com validação por `regex`.

```
$ jhipster entity Documento
? Do you want to update the entity? This will replace the existing files for this entity, all your custom code will be overwritten Yes, add more fields and relationships
Generating field #5

? Do you want to add a field to your entity? Yes
? What is the name of your field? url
? What is the type of your field? String
? Do you want to add validation rules to your field? Yes
? Which validation rules do you want to add? Regular expression pattern
? What is the regular expression pattern you want to apply on your field? ^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]

Generating field #6

? Do you want to add a field to your entity? No

```

Como fizemos diversas mudanças, teremos que fazer uma pequena intervenção no banco de dados. Para isso, abra o PGAdmin, selecione o menu Tools|Query Tools e depois digite e execute:

```
delete from databasechangelog where orderexecuted >=7;
drop table documento cascade;
drop table projeto cascade;
drop table tipo cascade;
```

#### 4-Alterar a versão Snapshot para 0.0.5

1. Edite o arquivo `.pom` e altere, conforme abaixo:

```
<groupId>br.com.dev4u.faroldocs</groupId>
    <artifactId>farol-docs</artifactId>
    <version>0.0.5-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>Farol Docs</name>
```

Grave o arquivo `.pom` e execute a aplicação com:

```
$ mvn
```

2. Selecione uma instância da entidade Documento e  altere o tipo, o projeto e a url.




*** Final da release 0.0.5 ***