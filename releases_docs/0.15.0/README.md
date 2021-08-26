## Documentação da Realease 0.15.0

Antes de começar, certifique-se de que o seu repositório esteja atualizado até a release `0.14.0` e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a esse estado.

### O que iremos fazer nesta release? 
Nesta release vamos melhorar a aparência de nossa aplicação com 4 mudanças:

#### 1-Implementar um botão "Voltar" na página de Etiquetas.
#### 2-Alterar a aparência da página para só mostrar o que realmente interessa.
#### 3-Alterar os arquivos de internacionalização

## Implementando as mudanças

::: :walking: Passo a passo :::

#### 1-Implementar um botão "Voltar" na página de Etiquetas.

Isso será necessário para, a partir da página de etiquetas, voltarmos para o documento.

<p align="center">
   <strong>Listagem 1.1 - Arquivo documento-etiqueta.component.html </strong> 
</p>

```html
<div>
  <h2 id="page-heading" data-cy="DocumentoEtiquetaHeading">
    <span jhiTranslate="farolDocsApp.documentoEtiqueta.home.title">Documento Etiquetas</span>
<!--Trecho com o novo botão acrescentado -->
      <button type="submit" (click)="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
        <fa-icon icon="arrow-left"></fa-icon>&nbsp;<span jhiTranslate="entity.action.back">Back</span>
      </button>
<!--Fim da mudança -->
```
Observe que esse botão quando clicado invoca o método `previousState()`. Esse método deverá ser delcarado no arquivo TypeScript correspondente.

<p align="center">
   <strong>Listagem 1.2 - Arquivo documento-etiqueta.component.ts </strong> 
</p>

```typescript
/* código exitente */
delete(documentoEtiqueta: IDocumentoEtiqueta): void {
    const modalRef = this.modalService.open(DocumentoEtiquetaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentoEtiqueta = documentoEtiqueta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAllEtiquetasFromDocuments(1,true);
      }
    });
  }
/* Início do trecho a ser incluído <<<----*/
  previousState(): void {
    window.history.back();
  }
   /* final do trecho a ser incluído' ---->> */ 

   /* código exitente continua...*/
```

Este método simplesmente volta no histórico da navegação com `window.history.back();`

#### 2-Alterar a aparência da página para só mostrar o que realmente interessa.



##### 2.1-Excluir o botão para visualizar etiquetas
##### 2.2-Mostrar o Código, Assunto e o Nome do Documento na página de etiquetas

Aqui como são várias pequenas alterações, vamos apresentar a versão final da classe. Sugiro que você utilize o `diff` do git para fazer a comparação linha a linha do que foi alterado.

<p align="center">
   <strong>Listagem 2 - Arquivo documento-etiqueta.component.ts </strong> 
</p>

```html
<div>
  <h2 id="page-heading" data-cy="DocumentoEtiquetaHeading">
    <span jhiTranslate="farolDocsApp.documentoEtiqueta.home.title">Relação de Etiquetas para este Documento</span>
    <div>
      <h4 id="page-heading" data-cy="DocumentoEtiquetaHeading">
        {{documento?.id}}-{{documento?.assunto}} / {{documento?.descricao}} 
      </h4>
    </div>
    <hr>
    <div class="d-flex justify-content-end">
      <button type="submit" (click)="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
        <fa-icon icon="arrow-left"></fa-icon>&nbsp;<span jhiTranslate="entity.action.back">Back</span>
      </button>
      <button
        id="jh-create-entity"
        data-cy="entityCreateButton"
        class="btn btn-primary jh-create-entity create-documento-etiqueta"
        [routerLink]="['/documento-etiqueta/new']"
      >
        <fa-icon icon="plus"></fa-icon>
        <span class="hidden-sm-down" jhiTranslate="farolDocsApp.documentoEtiqueta.home.createLabel"> Create a new Documento Etiqueta </span>
      </button>
    </div>
  </h2>

  <jhi-alert-error></jhi-alert-error>

  <jhi-alert></jhi-alert>

  <div class="row">
    <div class="col-sm-12">
      <form name="searchForm" class="form-inline">
        <div class="input-group w-100 mt-3">
        </div>
      </form>
    </div>
  </div>

  <div class="alert alert-warning" id="no-result" *ngIf="documentoEtiquetas?.length === 0">
    <span jhiTranslate="farolDocsApp.documentoEtiqueta.home.notFound">No documentoEtiquetas found</span>
  </div>

  <div class="table-responsive" id="entities" *ngIf="documentoEtiquetas && documentoEtiquetas.length > 0">
    <table class="table table-striped" aria-describedby="page-heading">
      <thead>
        
        
        <tr jhiSort [(predicate)]="predicate" [(ascending)]="ascending" [callback]="loadPage.bind(this)">
          
          <th scope="col" jhiSortBy="etiqueta.nome">
            <span jhiTranslate="farolDocsApp.documentoEtiqueta.etiqueta">Etiqueta</span> <fa-icon icon="sort"></fa-icon>
          </th>
         </tr>
      </thead>
      <tbody>
        <tr *ngFor="let documentoEtiqueta of documentoEtiquetas; trackBy: trackId" data-cy="entityTable">
           <td>
            <div *ngIf="documentoEtiqueta.etiqueta">
              <a [routerLink]="['/etiqueta', documentoEtiqueta.etiqueta?.id, 'view']">{{ documentoEtiqueta.etiqueta?.nome }}</a>
            </div>
          </td>
            <td class="text-right">
            <div class="btn-group">
  
              <button
                type="submit"
                [routerLink]="['/documento-etiqueta', documentoEtiqueta.id, 'edit']"
                class="btn btn-primary btn-sm"
                data-cy="entityEditButton"
              >
                <fa-icon icon="pencil-alt"></fa-icon>
                <span class="d-none d-md-inline" jhiTranslate="entity.action.edit">Edit</span>
              </button>

              <button type="submit" (click)="delete(documentoEtiqueta)" class="btn btn-danger btn-sm" data-cy="entityDeleteButton">
                <fa-icon icon="times"></fa-icon>
                <span class="d-none d-md-inline" jhiTranslate="entity.action.delete">Delete</span>
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <div *ngIf="documentoEtiquetas && documentoEtiquetas.length > 0">
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

#### 3-Alterar os arquivos de internacionalização

Altere os arquivos `src/main/webapp/i18n/en/documentoEtiqueta.json` para que fiquem identicos aos da Listagem 3 e Listagem 4


<p align="center">
   <strong>Listagem 3 - Arquivo src/main/webapp/i18n/en/documentoEtiqueta.json </strong> 
</p>

```json
{
  "farolDocsApp": {
    "documentoEtiqueta": {
      "home": {
        "title": "Tags for this document",
        "refreshListLabel": "Refresh list",
        "createLabel": "Add Tag to Document",
        "createOrEditLabel": "Create or edit a Documento Etiqueta",
        "search": "Search for Documento Etiqueta",
        "notFound": "No Documento Etiquetas found"
      },
      "created": "A new Documento Etiqueta is created with identifier {{ param }}",
      "updated": "A Documento Etiqueta is updated with identifier {{ param }}",
      "deleted": "A Documento Etiqueta is deleted with identifier {{ param }}",
      "delete": {
        "question": "Are you sure you want to delete Documento Etiqueta {{ id }}?"
      },
      "detail": {
        "title": "Documento Etiqueta"
      },
      "id": "ID",
      "etiqueta": "Etiqueta",
      "documento": "Documento"
    }
  }
}

```


<p align="center">
   <strong>Listagem 4 - Arquivo src/main/webapp/i18n/pt-br/documentoEtiqueta.json </strong> 
</p>

```json
{
  "farolDocsApp": {
    "documentoEtiqueta": {
      "home": {
        "title": " Relação de Etiquetas para este documento",
        "refreshListLabel": "Atualizar lista",
        "createLabel": "Adicionar Etiqueta ao Documento",
        "createOrEditLabel": "Criar ou editar Documento Etiqueta",
        "search": "Pesquisar por Documento Etiqueta",
        "notFound": "Nenhum Documento Etiqueta encontrado"
      },
      "created": "Um novo Documento Etiqueta foi criado com o identificador {{ param }}",
      "updated": "Um Documento Etiqueta foi atualizado com o identificador {{ param }}",
      "deleted": "Um Documento Etiqueta foi excluído com o identificador {{ param }}",
      "delete": {
        "question": "Tem certeza de que deseja excluir Documento Etiqueta {{ id }}?"
      },
      "detail": {
        "title": "Documento Etiqueta"
      },
      "id": "ID",
      "etiqueta": "Etiqueta",
      "documento": "Documento"
    }
  }
}

```

::: :pushpin: Importante :::

Ao final de todo o processo, você terá que ter 5 mudanças no Git.


Salve, compile, execute e teste.

```
mvn
```