## Documentação da Realease 0.15.1

Antes de começar, certifique-se de que o seu repositório esteja atualizado até a release `0.15.0` e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a esse estado.

### O que iremos fazer nesta release? 
Nesta release vamos apenas melhorar a aparência de nossa aplicação com 2 mudanças:

#### 1-Melhorar a aparência da página de documentos.
#### 2-Alterar os arquivos de internacionalização

## Implementando as mudanças

::: :walking: Passo a passo :::

#### 1-Melhorar a aparência da página de documentos.

##### 1.1 - Adicionar um novo ícone não existente na aplicação original
Vamos adicionar um novo ícone (uma etiqueta) ao conjunto de ícones pré-existentes na aplicação para, em seguida, usá-lo.

 - Abra o arquivo `src/main/webapp/app/config/font-awesome-icons.ts`.

 Você irá alterá-lo em dois lugares, logo acima do comentário `// jhipster-needle-add-icon-import` (há dois comentários como este).

 Com as alterações o arquivo ficará assim:

 ```json
   faWrench, //Incluir a vírgula
   faTags    //Incluir o novo ícone
  // jhipster-needle-add-icon-import
 ```


##### 1.2 - Fazer uma melhor formatação na página html de documento

Com as alterações o seu arquivo deverá ficar assim:

<p align="center">
   <strong>Listagem 1.2 - Arquivo documento-update.component.html </strong> 
</p>

```html
<div class="row justify-content-center">
  <div class="col-8">
    <form name="editForm" role="form" novalidate (ngSubmit)="save()" [formGroup]="editForm">
      <h2 id="jhi-documento-heading" data-cy="DocumentoCreateUpdateHeading"
        jhiTranslate="farolDocsApp.documento.home.createOrEditLabel">
        Create or edit a Documento
      </h2>

      <div>
        <jhi-alert-error></jhi-alert-error>

        <div class="form-group" [hidden]="editForm.get('id')!.value == null">
          <label class="form-control-label" jhiTranslate="global.field.id" for="field_id">ID</label>
          <input type="number" class="form-control" name="id" id="field_id" data-cy="id" formControlName="id"
            [readonly]="true" />
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.assunto"
            for="field_assunto">Assunto</label>
          <input type="text" class="form-control" name="assunto" id="field_assunto" data-cy="assunto"
            formControlName="assunto" />
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.descricao"
            for="field_descricao">Descricao</label>
          <input type="text" class="form-control" name="descricao" id="field_descricao" data-cy="descricao"
            formControlName="descricao" />
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.ementa"
            for="field_ementa">Ementa</label>
          <textarea class="form-control" name="ementa" id="field_ementa" data-cy="ementa"
            formControlName="ementa"></textarea>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.url" for="field_url">Url</label>
          <input type="text" class="form-control" name="url" id="field_url" data-cy="url" formControlName="url" />
          <div *ngIf="editForm.get('url')!.invalid && (editForm.get('url')!.dirty || editForm.get('url')!.touched)">
            <small class="form-text text-danger" *ngIf="editForm.get('url')?.errors?.pattern"
              jhiTranslate="entity.validation.pattern" [translateValues]="{ pattern: 'Url' }">
              This field should follow pattern for "Url".
            </small>
          </div>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.numero"
            for="field_numero">Numero</label>
          <input type="text" class="form-control" name="numero" id="field_numero" data-cy="numero"
            formControlName="numero" />
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.ano" for="field_ano">Ano</label>
          <input type="number" class="form-control" name="ano" id="field_ano" data-cy="ano" formControlName="ano" />
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.situacao"
            for="field_situacao">Situacao</label>
          <select class="form-control" name="situacao" formControlName="situacao" id="field_situacao"
            data-cy="situacao">
            <option [ngValue]="null">{{ 'farolDocsApp.SituacaoDocumento.null' | translate }}</option>
            <option value="VIGENTE">{{ 'farolDocsApp.SituacaoDocumento.VIGENTE' | translate }}</option>
            <option value="SUBSTITUIDO">{{ 'farolDocsApp.SituacaoDocumento.SUBSTITUIDO' | translate }}</option>
            <option value="CANCELADO">{{ 'farolDocsApp.SituacaoDocumento.CANCELADO' | translate }}</option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.criacao"
            for="field_criacao">Criacao</label>
          <div class="d-flex">
            <input id="field_criacao" data-cy="criacao" type="datetime-local" class="form-control" name="criacao"
              formControlName="criacao" placeholder="YYYY-MM-DD HH:mm" />
          </div>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.projeto"
            for="field_projeto">Projeto</label>
          <select class="form-control" id="field_projeto" data-cy="projeto" name="projeto" formControlName="projeto">
            <option [ngValue]="null"></option>
            <option
              [ngValue]="projetoOption.id === editForm.get('projeto')!.value?.id ? editForm.get('projeto')!.value : projetoOption"
              *ngFor="let projetoOption of projetosSharedCollection; trackBy: trackProjetoById">
              {{ projetoOption.nome }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.tipo" for="field_tipo">Tipo</label>
          <select class="form-control" id="field_tipo" data-cy="tipo" name="tipo" formControlName="tipo">
            <option [ngValue]="null"></option>
            <option
              [ngValue]="tipoOption.id === editForm.get('tipo')!.value?.id ? editForm.get('tipo')!.value : tipoOption"
              *ngFor="let tipoOption of tiposSharedCollection; trackBy: trackTipoById">
              {{ tipoOption.nome }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.orgaoEmissor"
            for="field_orgaoEmissor">Orgao Emissor</label>
          <select class="form-control" id="field_orgaoEmissor" data-cy="orgaoEmissor" name="orgaoEmissor"
            formControlName="orgaoEmissor">
            <option [ngValue]="null"></option>
            <option [ngValue]="
                orgaoEmissorOption.id === editForm.get('orgaoEmissor')!.value?.id ? editForm.get('orgaoEmissor')!.value : orgaoEmissorOption
              " *ngFor="let orgaoEmissorOption of orgaoEmissorsSharedCollection; trackBy: trackOrgaoEmissorById">
              {{ orgaoEmissorOption.sigla }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.tipoNorma" for="field_tipoNorma">Tipo
            Norma</label>
          <select class="form-control" id="field_tipoNorma" data-cy="tipoNorma" name="tipoNorma"
            formControlName="tipoNorma">
            <option [ngValue]="null"></option>
            <option
              [ngValue]="tipoNormaOption.id === editForm.get('tipoNorma')!.value?.id ? editForm.get('tipoNorma')!.value : tipoNormaOption"
              *ngFor="let tipoNormaOption of tipoNormasSharedCollection; trackBy: trackTipoNormaById">
              {{ tipoNormaOption.tipo }}
            </option>
          </select>
        </div>
      </div>

      <div>
        <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary"
          (click)="previousState()">
          <fa-icon icon="ban"></fa-icon>&nbsp;<span jhiTranslate="entity.action.cancel">Cancel</span>
        </button>

        <button type="submit" id="save-entity" data-cy="entityCreateSaveButton"
          [disabled]="editForm.invalid || isSaving" class="btn btn-primary">
          <fa-icon icon="save"></fa-icon>&nbsp;<span jhiTranslate="entity.action.save">Save</span>
        </button>
        <button type="button" [routerLink]="['/documento/',documento.id,'etiqueta']" queryParamsHandling="merge"
          class="btn btn-warning" data-cy="entityEditButton">
           <fa-icon icon="tags"></fa-icon>
          &nbsp;<span jhiTranslate="farolDocsApp.documento.gerenciarEtiquetas">Back</span>
        </button>
      </div>
    </form>


  </div>
</div>

```

#### 2-Alterar os arquivos de internacionalização

##### 2.1 - Alterar o arquivo global.json 

Com as alterações nos dois idiomas os arquivos ficarão assim:

<p align="center">
   <strong>Listagem 2.1.1 - Arquivo src/main/webapp/i18n/pt-br/global.json </strong> 
</p>


 ```json
 "entity": {
    "action": {
      "addblob": "Adicionar arquivo",
      "addimage": "Adicionar imagem",
      "back": "Voltar",
      "cancel": "Cancelar",
      "delete": "",
      "edit": "",
      "open": "Abrir",
      "save": "Gravar",
      "view": "", //Adionada uma vírgula aqui
      "etiquetas": "etiquetas" //Adicionado este conjunto chave/valor
    },
    "detail": {
 ```

<p align="center">
   <strong>Listagem 2.1.2 - Arquivo src/main/webapp/i18n/en/global.json </strong> 
</p>


 ```json
  "entity": {
    "action": {
      "addblob": "Add blob",
      "addimage": "Add image",
      "back": "Back",
      "cancel": "Cancel",
      "delete": "",
      "edit": "",
      "open": "Open",
      "save": "Save",
      "view": "", //Adicionada esta vírgula
      "etiquetas": "tags" //Adicionado este conjunto chave/valor
    },
    "detail": {
 ```


##### 2.2 - Alterar o arquivo documentoEtiqueta.json

Com as alterações nos dois idiomas os arquivos ficarão assim:

<p align="center">
   <strong>Listagem 2.2.1 - Arquivo src/main/webapp/i18n/pt-br/documentoEtiqueta.json </strong> 
</p>


 ```json
    "documentoEtiqueta": {
      "home": {
        "title": " Relação de Etiquetas para este documento", //Alterar aqui
        "refreshListLabel": "Atualizar lista",
        "createLabel": "Adicionar Etiqueta ao Documento", //Alterar aqui
        "createOrEditLabel": "Criar ou editar Documento Etiqueta",
        "search": "Pesquisar por Documento Etiqueta",
        "notFound": "Nenhum Documento Etiqueta encontrado"
      },

```

<p align="center">
   <strong>Listagem 2.2.1 - Arquivo src/main/webapp/i18n/en/documentoEtiqueta.json </strong> 
</p>


 ```json
     "documentoEtiqueta": {
      "home": {
        "title": "Tags for the document", //Alterar aqui
        "refreshListLabel": "Refresh the list",
        "createLabel": "Add Tag to Document", //Alterar aqui
        "createOrEditLabel": "Create or edit a Documento Etiqueta",
        "search": "Search for Documento Etiqueta",
        "notFound": "No Documento Etiquetas found"
      },

 ```

 ##### 2.3 - Alterar o arquivo documento.json

Com as alterações nos dois idiomas os arquivos ficarão assim:

<p align="center">
   <strong>Listagem 2.3.1 - Arquivo src/main/webapp/i18n/pt-br/documento.json</strong> 
</p>


```json
      "tipoNorma": "Tipo Norma",            //Colocar a vírgula
      "gerenciarEtiquetas": "Etiquetas..."  //Adicionado
    }
```

 <p align="center">
   <strong>Listagem 2.3.1 - Arquivo src/main/webapp/i18n/en/documento.json</strong> 
</p>


 ```json
       "tipoNorma": "Tipo Norma",  //Colocar vírgula
      "gerenciarEtiquetas": "Tags..." //Adionado
    }

 ```