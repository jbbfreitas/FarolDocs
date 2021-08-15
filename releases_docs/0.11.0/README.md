## Documentação da Realease 0.11.0

Antes de começar, certifique-se de que o seu repositório esteja atualizado até a release `0.10.0` e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a esse estado.

### O que iremos fazer nesta release? 

Esta versão terá 5 mudanças:

1. Alterar radicalmente a janela principal
2. Alterar o logo e o ícone da aplicação
3. Alterar o rodapé
4. Modificar botões para exibir apenas ícones
5. Alterar a ordem de apresentação dos campos


## Implementando as mudanças

### 1. Alterar radicalmente a janela principal (logo e texto)

Vamos deixar a janela principal com um aspecto mais pessoal.

Para isso faça o seguinte:
1. Copie os arquivos `.png` que estão na pasta `Arte` para a pasta `src/main/webapp/content/images`.

2. Altere o arquivo `src/main/webapp/app/home/home.component.scss` para:

```scss
/* ==========================================================================
Main page styles
========================================================================== */

.hipster {
  display: inline-block;
  width: 347px;
  height: 497px;
  background: url('../../content/images/FarolDocs_674_480.PNG') no-repeat center top; <------Aqui
  background-size: contain;
}

/* wait autoprefixer update to allow simple generation of high pixel density media query */
@media only screen and (-webkit-min-device-pixel-ratio: 2),
  only screen and (-moz-min-device-pixel-ratio: 2),
  only screen and (-o-min-device-pixel-ratio: 2/1),
  only screen and (min-resolution: 192dpi),
  only screen and (min-resolution: 2dppx) {
  .hipster {
    background: url('../../content/images/FarolDocs_674_480.PNG') no-repeat center top; <------Aqui
    background-size: contain;
  }
}

```
3. Altere o arquivo `src/main/webapp/i18n/pt-br/home.json` para:


```json
{
  "home": {
    "title": "Seja Bem Vindo!", 
    "subtitle": "",
    "logged": {
      "message": "Você está logado como \"{{username}}\"."
    },
    "question": "O que é o FarolDocs?",
    "link": {
      "description": "É um aplicativo para encontrar facilmente os seus documentos que estão na nuvem.",
      "more": "Que tal um teste gratuíto agora?"
    },
    "like": "Se você gostar do FaroDocs, não se esqueça de nos avaliar no",
    "github": "GitHub"
  }
}

```
4. Altere o arquivo `src/main/webapp/i18n/en/home.json` para:

```json
{
  "home": {
    "title": "You are Welcome!",
    "subtitle": "",
    "logged": {
      "message": "You are logged in as user \"{{username}}\"."
    },
    "question": "What's FarolDocs?",
    "link": {
      "description": "It is an application to easily find your cloud's files.",
      "more": "What about a free test now?"

    },
    "like": "If you like FarolDocs, don't forget to give us a star on",
    "github": "GitHub"
  }
}
```

### 2. Alterar o  ícone da aplicação

1. Altere o arquivo `src/main/webapp/app/layouts/navbar/navbar.component.scss` para:

```scss
... 

/* ==========================================================================
Logo styles
========================================================================== */
.logo-img {
  height: 45px;
  width: 45px;
  display: inline-block;
  vertical-align: middle;
  background: url('../../../content/images/logo-farol.png') no-repeat center center;<----Aqui
  background-size: contain;
}
```

### 3. Alterar o rodapé

1. Altere o arquivo `src/main/webapp/i18n/pt-br/global.json` para:


```json
  "error": {
    "internalServerError": "Erro interno do servidor",
    "server.not.reachable": "Servidor indisponível",
    "url.not.found": "Não encontrado",
    "NotNull": "O Campo {{ fieldName }} não pode ser vazio!",
    "Size": "O campo {{ fieldName }} não obedece os requisitos de tamanho mínimo ou máximo!",
    "userexists": "Usuário já está cadastrado!",
    "emailexists": "E-mail já está cadastrado!",
    "idexists": "Novo(a) {{entityName}} não pode ter um identificador",
    "idnull": "Identificador inválido",
    "idinvalid": "Invalid Id",
    "idnotfound": "ID cannot be found",
    "file": {
      "could.not.extract": "Could not extract file",
      "not.image": "File was expected to be an image but was found to be \"{{ fileType }}\""
    }
  },
  "footer": "Copyright FarolDocs Ltda - Brazil" <-----Aqui
}
```

2. Altere o arquivo `src/main/webapp/i18n/en/global.json` para:

```json
  "error": {
    "internalServerError": "Internal server error",
    "server.not.reachable": "Server not reachable",
    "url.not.found": "Not found",
    "NotNull": "Field {{ fieldName }} cannot be empty!",
    "Size": "Field {{ fieldName }} does not meet min/max size requirements!",
    "userexists": "Login name already used!",
    "emailexists": "Email is already in use!",
    "idexists": "A new {{ entityName }} cannot already have an ID",
    "idnull": "Invalid ID",
    "idinvalid": "Invalid ID",
    "idnotfound": "ID cannot be found",
    "file": {
      "could.not.extract": "Could not extract file",
      "not.image": "File was expected to be an image but was found to be \"{{ fileType }}\""
    }
  },
  "footer": "Copyright FarolDocs Ltda - Brazil" <----Aqui
}
```

### 4. Modificar botões para exibir apenas ícones

1. Altere o arquivo `src/main/webapp/i18n/pt-br/global.json` para:


```json
 "entity": {
    "action": {
      "addblob": "Adicionar arquivo",
      "addimage": "Adicionar imagem",
      "back": "Voltar",
      "cancel": "Cancelar",
      "delete": "",<-----Aqui
      "edit": "",<-----Aqui
      "open": "Abrir",
      "save": "Gravar",
      "view": ""<-----Aqui
    },
```

2. Altere o arquivo `src/main/webapp/i18n/en/global.json` para:

```json
  "entity": {
    "action": {
      "addblob": "Add blob",
      "addimage": "Add image",
      "back": "Back",
      "cancel": "Cancel",
      "delete": "",<-----Aqui
      "edit": "",<-----Aqui
      "open": "Open",
      "save": "Save",
      "view": ""<-----Aqui
    },

```

### 5. Alterar a ordem de apresentação dos campos

Vamos ter que alterar 2 arquivos

1. O primeiro arquivo é o `src/main/webapp/app/entities/documento/list/documento.component.html` e deverá ficar assim. Copie-o e sobregrave:

```html
<div>
  <h2 id="page-heading" data-cy="DocumentoHeading">
    <span jhiTranslate="farolDocsApp.documento.home.title">Documentos</span>

    <div class="d-flex justify-content-end">
      <button class="btn btn-info mr-2" (click)="loadPage()" [disabled]="isLoading">
        <fa-icon icon="sync" [spin]="isLoading"></fa-icon>
        <span jhiTranslate="farolDocsApp.documento.home.refreshListLabel">Refresh List</span>
      </button>

      <button
        id="jh-create-entity"
        data-cy="entityCreateButton"
        class="btn btn-primary jh-create-entity create-documento"
        [routerLink]="['/documento/new']"
      >
        <fa-icon icon="plus"></fa-icon>
        <span class="hidden-sm-down" jhiTranslate="farolDocsApp.documento.home.createLabel"> Create a new Documento </span>
      </button>
    </div>
  </h2>

  <jhi-alert-error></jhi-alert-error>

  <jhi-alert></jhi-alert>

  <div class="row">
    <div class="col-sm-12">
      <form name="searchForm" class="form-inline">
        <div class="input-group w-100 mt-3">
          <input
            type="text"
            class="form-control"
            [(ngModel)]="currentSearch"
            id="currentSearch"
            name="currentSearch"
            placeholder="{{ 'farolDocsApp.documento.home.search' | translate }}"
          />

          <button class="input-group-append btn btn-info" (click)="search(currentSearch)">
            <fa-icon icon="search"></fa-icon>
          </button>

          <button class="input-group-append btn btn-danger" (click)="search('')" *ngIf="currentSearch">
            <fa-icon icon="trash-alt"></fa-icon>
          </button>
        </div>
      </form>
    </div>
  </div>

  <div class="alert alert-warning" id="no-result" *ngIf="documentos?.length === 0">
    <span jhiTranslate="farolDocsApp.documento.home.notFound">No documentos found</span>
  </div>

  <div class="table-responsive" id="entities" *ngIf="documentos && documentos.length > 0">
       <table class="table table-striped" aria-describedby="page-heading">
      <thead>
        <tr jhiSort [(predicate)]="predicate" [(ascending)]="ascending" [callback]="loadPage.bind(this)">
          <th scope="col" jhiSortBy="id"><span jhiTranslate="global.field.id">ID</span> <fa-icon icon="sort"></fa-icon></th>
          <th scope="col" jhiSortBy="projeto.nome">
            <span jhiTranslate="farolDocsApp.documento.projeto">Projeto</span> <fa-icon icon="sort"></fa-icon>
          </th>
          <th scope="col" jhiSortBy="assunto">
            <span jhiTranslate="farolDocsApp.documento.assunto">Assunto</span> <fa-icon icon="sort"></fa-icon>
          </th>
          <th scope="col" jhiSortBy="descricao">
            <span jhiTranslate="farolDocsApp.documento.descricao">Descricao</span> <fa-icon icon="sort"></fa-icon>
         </th>
          <th scope="col" jhiSortBy="numero">
            <span jhiTranslate="farolDocsApp.documento.numero">Numero</span> <fa-icon icon="sort"></fa-icon>
          </th>
          <th scope="col" jhiSortBy="ano"><span jhiTranslate="farolDocsApp.documento.ano">Ano</span> <fa-icon icon="sort"></fa-icon></th>
          <th scope="col" jhiSortBy="situacao">
            <span jhiTranslate="farolDocsApp.documento.situacao">Situacao</span> <fa-icon icon="sort"></fa-icon>
          </th>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let documento of documentos; trackBy: trackId" data-cy="entityTable">
          <td>
            <a [routerLink]="['/documento', documento.id, 'view']">{{ documento.id }}</a>
          </td>
          <td>
            <div *ngIf="documento.projeto">
              <a [routerLink]="['/projeto', documento.projeto?.id, 'view']">{{ documento.projeto?.nome }}</a>
            </div>
          </td>
          <td>{{ documento.assunto }}</td>
          <td>{{ documento.descricao }}</td>
          <td>{{ documento.numero }}</td>
          <td>{{ documento.ano }}</td>
          <td>{{ documento.situacao }}</td>
          <td>
          </td>
          <td class="text-right">
            <div class="btn-group">
              <button
                type="submit"
                [routerLink]="['/documento', documento.id, 'view']"
                class="btn btn-info btn-sm"
                data-cy="entityDetailsButton"
              >
                <fa-icon icon="eye"></fa-icon>
                <span class="d-none d-md-inline" jhiTranslate="entity.action.view">View</span>
              </button>

              <button
                type="submit"
                [routerLink]="['/documento', documento.id, 'edit']"
                class="btn btn-primary btn-sm"
                data-cy="entityEditButton"
              >
                <fa-icon icon="pencil-alt"></fa-icon>
                <span class="d-none d-md-inline" jhiTranslate="entity.action.edit">Edit</span>
              </button>

              <button type="submit" (click)="delete(documento)" class="btn btn-danger btn-sm" data-cy="entityDeleteButton">
                <fa-icon icon="times"></fa-icon>
                <span class="d-none d-md-inline" jhiTranslate="entity.action.delete">Delete</span>
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <div *ngIf="documentos && documentos.length > 0">
    <div class="row justify-content-center">
      <jhi-item-count [params]="{ page: page, totalItems: totalItems, itemsPerPage: itemsPerPage }"></jhi-item-count>
    </div>

    <div class="row justify-content-center">
      <ngb-pagination
        [collectionSize]="totalItems"
        [(page)]="ngbPaginationPage"
        [pageSize]="itemsPerPage"
        [maxSize]="5"
        [rotate]="true"
        [boundaryLinks]="true"
        (pageChange)="loadPage($event)"
      ></ngb-pagination>
    </div>
  </div>
</div>

```

2. O segundo arquivo é o `src/main/webapp/app/entities/documento/update/documento-update.component.html` e deverá ficar assim. Copie-o e sobregrave:

```html
<div class="row justify-content-center">
  <div class="col-8">
    <form name="editForm" role="form" novalidate (ngSubmit)="save()" [formGroup]="editForm">
      <h2 id="jhi-documento-heading" data-cy="DocumentoCreateUpdateHeading" jhiTranslate="farolDocsApp.documento.home.createOrEditLabel">
        Create or edit a Documento
      </h2>

      <div>
        <jhi-alert-error></jhi-alert-error>

        <div class="form-group" [hidden]="editForm.get('id')!.value == null">
          <label class="form-control-label" jhiTranslate="global.field.id" for="field_id">ID</label>
          <input type="number" class="form-control" name="id" id="field_id" data-cy="id" formControlName="id" [readonly]="true" />
        </div>
        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.tipo" for="field_tipo">Tipo</label>
          <select class="form-control" id="field_tipo" data-cy="tipo" name="tipo" formControlName="tipo">
            <option [ngValue]="null"></option>
            <option
              [ngValue]="tipoOption.id === editForm.get('tipo')!.value?.id ? editForm.get('tipo')!.value : tipoOption"
              *ngFor="let tipoOption of tiposSharedCollection; trackBy: trackTipoById"
            >
              {{ tipoOption.nome }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.projeto" for="field_projeto">Projeto</label>
          <select class="form-control" id="field_projeto" data-cy="projeto" name="projeto" formControlName="projeto">
            <option [ngValue]="null"></option>
            <option
              [ngValue]="projetoOption.id === editForm.get('projeto')!.value?.id ? editForm.get('projeto')!.value : projetoOption"
              *ngFor="let projetoOption of projetosSharedCollection; trackBy: trackProjetoById"
            >
              {{ projetoOption.nome }}
            </option>
          </select>
        </div>


        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.assunto" for="field_assunto">Assunto</label>
          <input type="text" class="form-control" name="assunto" id="field_assunto" data-cy="assunto" formControlName="assunto" />
        </div>
        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.orgaoEmissor" for="field_orgaoEmissor"
            >Orgao Emissor</label
          >
          <select class="form-control" id="field_orgaoEmissor" data-cy="orgaoEmissor" name="orgaoEmissor" formControlName="orgaoEmissor">
            <option [ngValue]="null"></option>
            <option
              [ngValue]="
                orgaoEmissorOption.id === editForm.get('orgaoEmissor')!.value?.id ? editForm.get('orgaoEmissor')!.value : orgaoEmissorOption
              "
              *ngFor="let orgaoEmissorOption of orgaoEmissorsSharedCollection; trackBy: trackOrgaoEmissorById"
            >
              {{ orgaoEmissorOption.sigla }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.tipoNorma" for="field_tipoNorma">Tipo Norma</label>
          <select class="form-control" id="field_tipoNorma" data-cy="tipoNorma" name="tipoNorma" formControlName="tipoNorma">
            <option [ngValue]="null"></option>
            <option
              [ngValue]="tipoNormaOption.id === editForm.get('tipoNorma')!.value?.id ? editForm.get('tipoNorma')!.value : tipoNormaOption"
              *ngFor="let tipoNormaOption of tipoNormasSharedCollection; trackBy: trackTipoNormaById"
            >
              {{ tipoNormaOption.tipo }}
            </option>
          </select>
        </div>
      </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.descricao" for="field_descricao">Descricao</label>
          <input type="text" class="form-control" name="descricao" id="field_descricao" data-cy="descricao" formControlName="descricao" />
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.ementa" for="field_ementa">Ementa</label>
          <textarea class="form-control" name="ementa" id="field_ementa" data-cy="ementa" formControlName="ementa"></textarea>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.url" for="field_url">Url</label>
          <input type="text" class="form-control" name="url" id="field_url" data-cy="url" formControlName="url" />
          <div *ngIf="editForm.get('url')!.invalid && (editForm.get('url')!.dirty || editForm.get('url')!.touched)">
            <small
              class="form-text text-danger"
              *ngIf="editForm.get('url')?.errors?.pattern"
              jhiTranslate="entity.validation.pattern"
              [translateValues]="{ pattern: 'Url' }"
            >
              This field should follow pattern for "Url".
            </small>
          </div>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.numero" for="field_numero">Numero</label>
          <input type="text" class="form-control" name="numero" id="field_numero" data-cy="numero" formControlName="numero" />
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.ano" for="field_ano">Ano</label>
          <input type="number" class="form-control" name="ano" id="field_ano" data-cy="ano" formControlName="ano" />
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.situacao" for="field_situacao">Situacao</label>
          <select class="form-control" name="situacao" formControlName="situacao" id="field_situacao" data-cy="situacao">
            <option [ngValue]="null">{{ 'farolDocsApp.SituacaoDocumento.null' | translate }}</option>
            <option value="VIGENTE">{{ 'farolDocsApp.SituacaoDocumento.VIGENTE' | translate }}</option>
            <option value="SUBSTITUIDO">{{ 'farolDocsApp.SituacaoDocumento.SUBSTITUIDO' | translate }}</option>
            <option value="CANCELADO">{{ 'farolDocsApp.SituacaoDocumento.CANCELADO' | translate }}</option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.etiqueta" for="field_etiqueta">Etiqueta</label>
          <select class="form-control" id="field_etiqueta" data-cy="etiqueta" name="etiqueta" formControlName="etiqueta">
            <option [ngValue]="null"></option>
            <option
              [ngValue]="etiquetaOption.id === editForm.get('etiqueta')!.value?.id ? editForm.get('etiqueta')!.value : etiquetaOption"
              *ngFor="let etiquetaOption of etiquetasSharedCollection; trackBy: trackEtiquetaById"
            >
              {{ etiquetaOption.nome }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.criacao" for="field_criacao">Criacao</label>
          <div class="d-flex">
            <input
              id="field_criacao"
              data-cy="criacao"
              type="datetime-local"
              class="form-control"
              name="criacao"
              formControlName="criacao"
              placeholder="YYYY-MM-DD HH:mm"
            />
          </div>
        </div>
      <div>
        <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" (click)="previousState()">
          <fa-icon icon="ban"></fa-icon>&nbsp;<span jhiTranslate="entity.action.cancel">Cancel</span>
        </button>

        <button
          type="submit"
          id="save-entity"
          data-cy="entityCreateSaveButton"
          [disabled]="editForm.invalid || isSaving"
          class="btn btn-primary"
        >
          <fa-icon icon="save"></fa-icon>&nbsp;<span jhiTranslate="entity.action.save">Save</span>
        </button>
      </div>
    </form>
  </div>
</div>
```
Altere o arquivo `src/main/webapp/app/home/home.component.html` para ficar como abaixo:

```html
<div class="row">
  <div class="col-md-3">
    <span class="hipster img-fluid rounded"></span>
  </div>

  <div class="col-md-9">
    <h1 class="display-4"><span jhiTranslate="home.title">Welcome, Java Hipster!</span></h1>

    <p class="lead" jhiTranslate="home.subtitle">This is your homepage</p>

    <div [ngSwitch]="account !== null">
      <div class="alert alert-success" *ngSwitchCase="true">
        <span id="home-logged-message" *ngIf="account" jhiTranslate="home.logged.message" [translateValues]="{ username: account.login }"
          >You are logged in as user "{{ account.login }}".</span
        >
      </div>

      <div class="alert alert-warning" *ngSwitchCase="false">
        <span jhiTranslate="global.messages.info.authenticated.prefix">If you want to </span>
        <a class="alert-link" (click)="login()" jhiTranslate="global.messages.info.authenticated.link">sign in</a
        ><span jhiTranslate="global.messages.info.authenticated.suffix"
          >, you can try the default accounts:<br />- Administrator (login="admin" and password="admin") <br />- User (login="user" and
          password="user").</span
        >
      </div>

      <div class="alert alert-warning" *ngSwitchCase="false">
        <span jhiTranslate="global.messages.info.register.noaccount">You don't have an account yet?</span>&nbsp;
        <a class="alert-link" routerLink="account/register" jhiTranslate="global.messages.info.register.link">Register a new account</a>
      </div>
    </div>

    <p jhiTranslate="home.question">If you have any question on JHipster:</p>

    <ul>
      <li>
        <a jhiTranslate="home.link.description"> Do a free test</a>
      </li>
      <li>
        <a jhiTranslate="home.link.more"> Do a free test</a>
      </li>
    </ul>

    <p>
      <span jhiTranslate="home.like">If you like FarolDocs, don't forget to give us a star on</span>
      <a href="https://github.com/jbffreitas/FarolDocs" target="_blank" rel="noopener noreferrer" jhiTranslate="home.github"
        >GitHub</a
      >!
    </p>
  </div>
</div>

```


Salve, compile, execute e teste.

```
mvn
```

*** Final da release 0.11.0 ***