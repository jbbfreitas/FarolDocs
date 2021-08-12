## Documentação da Realease 0.10.0

Antes de começar, certifique-se de que o seu repositório esteja atualizado até a release `0.0.6` e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a esse estado.


Vamos pular da release 0.0.6 para 0.10.0 por que nesta versão vamos mudar um pouco mais o comportamento de nossa aplicação.

### O que iremos fazer nesta release? 

Esta versão terá 6 mudanças:

1. Alterar o tipo de paginação de `infinit scroll` para `pagination`
2. Criar o atributo `criacao` em `Documento`, tendo o default a data atual
3. Colocar como valor padrão para a situação do documento a opção VIGENTE;
4. Alterar a janela inicial
5. Colocar caracteres especiais nos labels
6. Na janela que exibe a lista de documentos, mostrar somente os campos necessários 

Na maioria delas será necessário alterar o código de nossa aplicação. Isso significa que, após essas alterações, não poderemos mais gerar o código via jhipster, como estávamos fazendo, sob pena do Jhipster sobrepor as nossas mudanças. 

Uma boa ferramenta de `scaffold` como o JHipster deve gerar códigos fonte limpos e elegantes. Isso é importante por que em algum momento você necessitará de alterar manualmente os códigos fonte se quiser uma aplicaçãop realmente profissional.  

::: :pushpin: Importante :::
Então tenha sempre em mente o seguinte: realize primeiramente todas as mudanças sem alterar os códigos-fonte para, só depois, começar a alterar o código. 

## Implementando as mudanças

1. **Alterar o tipo de paginação de `infinit scroll` para `pagination`**

Fizemos questão de criar inicialmente a aplicação com a paginação `infinit scroll`, como fizemos até a release 0.0.6 para demonstrar esse recurso fantástico do JHipster que é a sua capacidade de regerar por completo uma determinada entidade.

A paginação com `infinit scroll` só deve ser utilizada para tabelas muito pequenas (com no máximo 20 registros), visto que neste tipo de paginação a janela faz uma rolagem infinita. O ideal quando temos muitos registros é mostrar as ocorrências de uma entidade (linhas), agrupadas em página, ou seja usar `pagination`. Isso organiza melhor a informação além de possibilitar navegar pelas páginas e não pelas linhas, conferindo assim um aspecto mais profissional para a sua aplicação.

Vamos então alterar todas as entidades para permitir `pagination`. 

1. Edite o arquivo `Documento.json`. Na ante-penultima linha altere a paginação para `pagination`.

```json
"service": "serviceImpl",
  "dto": "no",
  "jpaMetamodelFiltering": false,
  "readOnly": false,
  "pagination": "pagination", <-------
  "name": "Documento",
  "changelogDate": "20210807222414"
```
2. Em seguinta regere a entidade usando:

````
jhipster entity Documento
````

3. Faça os passos 1 e 2 para as demais entidade.

4. No final, compile e execute a sua aplicação:

````
mvn
````
2. **Criar o atributo `criacao` em `Documento`, tendo o default a data atual**

```
jhipster entity Documento

? Do you want to update the entity? This will replace the existing files for this entity, all 
your custom code will be overwritten Yes, add more fields and relationships

Generating field #8

? Do you want to add a field to your entity? Yes
? What is the name of your field? criacao
? What is the type of your field? Instant
? Do you want to add validation rules to your field? No

Generating field #9

? Do you want to add a field to your entity? No

Generating relationships to other entities

? Do you want to add a relationship to another entity? No
```
Execute esat script no PGAdmin

```sql
select * from databasechangelog; 
delete from databasechangelog where orderexecuted >=7;
select * from databasechangelog;

drop table documento cascade;
drop table projeto cascade;
drop table tipo cascade;
drop table etiqueta cascade;
drop table orgao_emissor cascade;
drop table tipo_norma cascade;
```


```
mvn
```
Teste a sua aplicação e verifique se quando você cria um novo documento a data de criação vem preenchida. 


3. ** Colocar como valor padrão para a situação do documento a opção VIGENTE **

Agora vamos colocar como valor `default` para o atributo situação o valor `VIGENTE`.

Vamos lembrar que esse atributo,  gerado na release 0.0.6, é do tipo `ENUM` (enumeration em Java). E a classe que contém os 3 tipos enumerados chama-se `SituacaoDocumento`. 

Para fazer com que o valor `VIGENTE` seja o padrão para os documentos recém criados, abra o arquivo `documento-update.component.ts`. Ele está escrito na linguagem [TypeScript] (https://blog.betrybe.com/desenvolvimento-web/typescript/). 

Procute o método `protected updateForm(documento: IDocumento): void {` (linha 151)

Altere a linha 160 de:

```
situacao: documento.situacao,
```

para

```
situacao: documento.situacao !=null?  documento.situacao : SituacaoDocumento.VIGENTE, // Default VIGENTE

```
Quando você fizer essa substituição o VSC (Visual Source Code), vai sublinhar em vermelho a palavra `SituacaoDocumento`. Isso é por que ele não sabe o que vem a ser essa palavra. Porém, como toda ferramenta IDE que se preze, ele oferece a oportunidade de você corrigir o problema. Para corrigr passe o mouse em cima da palavra sublinhada em vermelho e selecione `Quick Fix`. Em seguida aceite a única sugestão dada. Vá até a linha 26 e observe que o VSC importou essa classe, veja :

```typescript
import { SituacaoDocumento } from 'app/entities/enumerations/situacao-documento.model';

```
Sem entrar muito no mérito da sintaxe do TypeScript, que por sinal é muito elegante, entenda por hora que agora a classe `SituacaoDocumento` é conhecida e pode ser usada na classe atual. 

Mas, afinal, o que fizemos? Vamos dar uma olhada novamente na linha que modificamos:

```
situacao: documento.situacao !=null?  documento.situacao : SituacaoDocumento.VIGENTE, // Default VIGENTE

```
Podemos ler essa frase da seguinte forma:

Se o valor do atributo situação do documento não for nulo, atribua à variavel `situacao` o valor do atributo `documento.situacao`. Se o valor do atributo situação do documentofor form nulo (vc está criando um novo documento) então atribua à variável situação o valor `SituacaoDocumento.VIGENTE`(default).

Salve, compile, execute e teste.

```
mvn
```

4. **Alterar a janela inicial**

Nesta release vamos alterar parcialmente a janela inicial (colocar um pouco de botox). Em outras releases vamos fazer uma plástica completa.

Abra o arquivo `home.json` e faça as seguintes alterações:

```json
{
  "home": {
    "title": "Seja Bem vindo ao FarolDocs!", 
    "subtitle": "",
    "logged": {
      "message": "Você está logado como \"{{username}}\"."
    },
    "question": "Em caso de dúvida sobre o FarolDocs:",
    "link": {
      "homepage": "Página principal JHipster",
      "stackoverflow": "JHipster no Stack Overflow",
      "bugtracker": "JHipster bug tracker",
      "chat": "Sala de bate-papo pública JHipster",
      "follow": "siga @jhipster no Twitter"
    },
    "like": "Se você gosta do FaroDocs, não se esqueça de avaliar no",
    "github": "GitHub"
  }
```


5. **Colocar caracteres especiais nos labels**

6. **Na janela que exibe a lista de documentos, mostrar somente os campos necessários**