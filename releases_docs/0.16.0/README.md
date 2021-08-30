## Documentação da Realease 0.16.0

Antes de começar, certifique-se de que o seu repositório esteja atualizado até a release `0.15.1` e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a esse estado.

### O que iremos fazer nesta release? 
Na versão `0.15.0` nós ficamos com um problema: só era possível criar registros na entidade `DocumentoEtiqueta` após salvar a entidade `Documento`. Isso porque na relação mestre detalhe é preciso primeiramente obter a chave primária do mestre para, só então, gravá-la no detalhe.

Em termos de navegação, a aplicação deixou a desejar! 

Para corrigir o problema vamos mudar um pouco a forma de trabalho para, ao invés de ter que gravar o mestre primeiro (para gerar a chave primária), vamos armazenar os valores escolhidos para etiqueta em um array e depois enviar para o back-end gravar tudo de uma só vez.
 Em outros releases vamos melhorar ainda mais essa solução, pois como você poderá observar outros passos precisão ser seguidos para "gravar" quais as etiquetas já foram selecionadas para o caso de uso de re-editar um documento para acrescentar ou diminuir uma etiqueta ao rol das selecionadas. 

Então, resumidamente, faremos:

#### 1-Ao clicar no botão Etiquetas..., mostrar o rol de etiquetas disponíveis para uso .
#### 2-Armazenar as etiquetas selecionadas em um Array para uso posterior.

## Implementando as mudanças

::: :walking: Passo a passo :::

#### 1-Ao clicar no botão Etiquetas..., mostrar o rol de etiquetas disponíveis para uso .

Para isso teremos que alterar o routerLink do botão para que o componente a ser exibido seja `Etiqueta.Component` e não mais `DocumentoEtiqueta.Component`. 


##### 1.1-Alterar o arquivo html

Com as alterações o seu arquivo deverá ficar assim:

<p align="center">
   <strong>Listagem 1.1 - Arquivo documento-update.component.html </strong> 
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
        <hr>
        <!--Aqui começam as mudanças -->
        <button type="button" [routerLink]="['/documento/',documento.id,'etiquetas']"   queryParamsHandling="merge"
          class="btn btn-warning" data-cy="entityEditButton">
          <fa-icon icon="tags"></fa-icon>
          &nbsp;<span jhiTranslate="farolDocsApp.documento.gerenciarEtiquetas">Back</span>
        </button>
        <hr>
      </div>
      <div>
        <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary"
          (click)="previousState()">
          <fa-icon icon="ban"></fa-icon>&nbsp;<span jhiTranslate="entity.action.cancel">Cancel</span>
        </button>
        <button type="submit" id="save-entity" data-cy="entityCreateSaveButton" class="btn btn-primary">
          <fa-icon icon="save"></fa-icon>&nbsp;<span jhiTranslate="entity.action.save">Save</span>
        </button>
      </div>
    </form>
    <!--Aqui terminan as mudanças-->
  </div>
</div>
```

##### 1.2-Alterar o arquivo routing

Esta alteração fará com que quando clicarmos no botão `Etiquetas...` a navegação seja roteada para `:id/etiquetas` e o componente que será mostrado seja `EtiquetaComponent`

Com as alterações o seu arquivo deverá ficar assim:

<p align="center">
   <strong>Listagem 1.2 - Arquivo src/main/webapp/app/entities/documento/route/documento-routing.module.ts </strong> 
</p>

```typescript
/* Retirar  este bloco  
  {
    path: ':id/etiqueta',
    component: DocumentoEtiquetaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    resolve: {
      documento: DocumentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  */

 /*
 Adicionar este bloco
 */
  {
    path: ':id/etiquetas',        //A mudança principal foi aqui
    component: EtiquetaComponent, //e aqui
    data: {
      defaultSort: 'id,asc',
    },
    resolve: {
      documento: DocumentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },

```




#### 2-Armazenar as etiquetas selecionadas em um Array para uso posterior.

Com as alterações seu arquivo ficará assim:


<p align="center">
   <strong>Listagem 2.1 - Arquivo src/main/webapp/app/entities/documento/update/documento-update.component.ts </strong> 
</p>



```typescript
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDocumento, Documento } from '../documento.model';
import { DocumentoService } from '../service/documento.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProjeto } from 'app/entities/projeto/projeto.model';
import { ProjetoService } from 'app/entities/projeto/service/projeto.service';
import { ITipo } from 'app/entities/tipo/tipo.model';
import { TipoService } from 'app/entities/tipo/service/tipo.service';
import { IOrgaoEmissor } from 'app/entities/orgao-emissor/orgao-emissor.model';
import { OrgaoEmissorService } from 'app/entities/orgao-emissor/service/orgao-emissor.service';
import { ITipoNorma } from 'app/entities/tipo-norma/tipo-norma.model';
import { TipoNormaService } from 'app/entities/tipo-norma/service/tipo-norma.service';
import { IEtiqueta } from 'app/entities/etiqueta/etiqueta.model';
import { EtiquetaService } from 'app/entities/etiqueta/service/etiqueta.service';

@Component({
  selector: 'jhi-documento-update',
  templateUrl: './documento-update.component.html',
})
export class DocumentoUpdateComponent implements OnInit {
  isSaving = false;

  projetosSharedCollection: IProjeto[] = [];
  tiposSharedCollection: ITipo[] = [];
  orgaoEmissorsSharedCollection: IOrgaoEmissor[] = [];
  tipoNormasSharedCollection: ITipoNorma[] = [];

  editForm = this.fb.group({
    id: [],
    assunto: [],
    descricao: [],
    ementa: [],
    url: [null, [Validators.pattern('^(https?|ftp|file)://[-a-zA-Z0-9+&amp;@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&amp;@#/%=~_|]')]],
    numero: [],
    ano: [],
    situacao: [],
    criacao: [],
    projeto: [],
    tipo: [],
    orgaoEmissor: [],
    tipoNorma: [],
  });
  documento: any;
  etiquetas: IEtiqueta[] = [];//1-Criada uma Array para armazenar as etiquetas

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected documentoService: DocumentoService,
    protected projetoService: ProjetoService,
    protected tipoService: TipoService,
    protected orgaoEmissorService: OrgaoEmissorService,
    protected tipoNormaService: TipoNormaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected etiquetaService: EtiquetaService, //2-Injetado o Etiqueta Service no construtor


  ) { }

  ngOnInit(): void {
    this.etiquetas = this.etiquetaService.etiquetas //3-Atribuindo array à variável local Etiquetas 
    //TODO 
    //Atualizar o campo Tag com as etiquetas desse documento

    this.activatedRoute.data.subscribe(({ documento }) => {
      this.documento = documento //Atribui o documento à variável local para ser usada na página
      if (documento.id === undefined) {
        const today = dayjs().startOf('day');
        documento.criacao = today;
      }
      //Provisorio
      this.atualizarEtiquetas(documento); //4-Atualiza o campo ementa (provisório)
      this.updateForm(documento);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('farolDocsApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }
//5-Atualizar o campo ementa com as etiquetas selecionadas apenas para validar a solução
  atualizarEtiquetas(documento: IDocumento): void {
    let etiquetasTXT = '';
    const tamanho = this.etiquetas.length;
    for (const etiqueta of this.etiquetas) {
      etiquetasTXT = etiquetasTXT + String(etiqueta.nome)+(etiqueta!==this.etiquetas[tamanho-1]?', ':'')
    }
    documento.ementa = etiquetasTXT;
  }

  save(): void {
    this.isSaving = true;
    const documento = this.createFromForm();
    if (documento.id !== undefined) {
      this.subscribeToSaveResponse(this.documentoService.update(documento));
    } else {
      this.subscribeToSaveResponse(this.documentoService.create(documento));
    }
  }

  trackProjetoById(index: number, item: IProjeto): number {
    return item.id!;
  }

  trackTipoById(index: number, item: ITipo): number {
    return item.id!;
  }

  trackOrgaoEmissorById(index: number, item: IOrgaoEmissor): number {
    return item.id!;
  }

  trackTipoNormaById(index: number, item: ITipoNorma): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumento>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(documento: IDocumento): void {
    this.editForm.patchValue({
      id: documento.id,
      assunto: documento.assunto,
      descricao: documento.descricao,
      ementa: documento.ementa,
      url: documento.url,
      numero: documento.numero,
      ano: documento.ano,
      situacao: documento.situacao,
      criacao: documento.criacao ? documento.criacao.format(DATE_TIME_FORMAT) : null,
      projeto: documento.projeto,
      tipo: documento.tipo,
      orgaoEmissor: documento.orgaoEmissor,
      tipoNorma: documento.tipoNorma,
    });

    this.projetosSharedCollection = this.projetoService.addProjetoToCollectionIfMissing(this.projetosSharedCollection, documento.projeto);
    this.tiposSharedCollection = this.tipoService.addTipoToCollectionIfMissing(this.tiposSharedCollection, documento.tipo);
    this.orgaoEmissorsSharedCollection = this.orgaoEmissorService.addOrgaoEmissorToCollectionIfMissing(
      this.orgaoEmissorsSharedCollection,
      documento.orgaoEmissor
    );
    this.tipoNormasSharedCollection = this.tipoNormaService.addTipoNormaToCollectionIfMissing(
      this.tipoNormasSharedCollection,
      documento.tipoNorma
    );
  }

  protected loadRelationshipsOptions(): void {
    this.projetoService
      .query()
      .pipe(map((res: HttpResponse<IProjeto[]>) => res.body ?? []))
      .pipe(
        map((projetos: IProjeto[]) => this.projetoService.addProjetoToCollectionIfMissing(projetos, this.editForm.get('projeto')!.value))
      )
      .subscribe((projetos: IProjeto[]) => (this.projetosSharedCollection = projetos));

    this.tipoService
      .query()
      .pipe(map((res: HttpResponse<ITipo[]>) => res.body ?? []))
      .pipe(map((tipos: ITipo[]) => this.tipoService.addTipoToCollectionIfMissing(tipos, this.editForm.get('tipo')!.value)))
      .subscribe((tipos: ITipo[]) => (this.tiposSharedCollection = tipos));

    this.orgaoEmissorService
      .query()
      .pipe(map((res: HttpResponse<IOrgaoEmissor[]>) => res.body ?? []))
      .pipe(
        map((orgaoEmissors: IOrgaoEmissor[]) =>
          this.orgaoEmissorService.addOrgaoEmissorToCollectionIfMissing(orgaoEmissors, this.editForm.get('orgaoEmissor')!.value)
        )
      )
      .subscribe((orgaoEmissors: IOrgaoEmissor[]) => (this.orgaoEmissorsSharedCollection = orgaoEmissors));

    this.tipoNormaService
      .query()
      .pipe(map((res: HttpResponse<ITipoNorma[]>) => res.body ?? []))
      .pipe(
        map((tipoNormas: ITipoNorma[]) =>
          this.tipoNormaService.addTipoNormaToCollectionIfMissing(tipoNormas, this.editForm.get('tipoNorma')!.value)
        )
      )
      .subscribe((tipoNormas: ITipoNorma[]) => (this.tipoNormasSharedCollection = tipoNormas));
  }

  protected createFromForm(): IDocumento {
    return {
      ...new Documento(),
      id: this.editForm.get(['id'])!.value,
      assunto: this.editForm.get(['assunto'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      ementa: this.editForm.get(['ementa'])!.value,
      url: this.editForm.get(['url'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      ano: this.editForm.get(['ano'])!.value,
      situacao: this.editForm.get(['situacao'])!.value,
      criacao: this.editForm.get(['criacao'])!.value ? dayjs(this.editForm.get(['criacao'])!.value, DATE_TIME_FORMAT) : undefined,
      projeto: this.editForm.get(['projeto'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      orgaoEmissor: this.editForm.get(['orgaoEmissor'])!.value,
      tipoNorma: this.editForm.get(['tipoNorma'])!.value,
    };
  }
}

```

Se você observar, foram feitas 5 alterações nesse componente:

- 1-Criada uma Array para armazenar as etiquetas
  etiquetas: IEtiqueta[] = [];
  
- 2-Injetado o Etiqueta Service no construtor
  protected etiquetaService: EtiquetaService, 

- 3-Atribuindo array à variável local Etiquetas 
  this.etiquetas = this.etiquetaService.etiquetas 

- 4-Atualiza o campo ementa (provisório)
  this.atualizarEtiquetas(documento); 

- 5-Atualizar o campo ementa com as etiquetas selecionadas apenas para validar a solução
  atualizarEtiquetas(documento: IDocumento): void {

#### 3-No component EtiquetaComponent criar funcionalidade para voltar e um evento para registrar a etiqueta selecionada (click).

##### 3.1-No component EtiquetaComponent criar funcionalidade para voltar .

Com as alterações seu arquivo ficará assim:


<p align="center">
   <strong>Listagem 3.1 - Arquivo src/main/webapp/app/entities/etiqueta/list/etiqueta.component.html </strong> 
</p>      


```html
<div>
  <h2 id="page-heading" data-cy="EtiquetaHeading">
    <span jhiTranslate="farolDocsApp.etiqueta.home.title">Etiquetas</span>


    <div class="d-flex justify-content-end">
      <button type="submit" (click)="previousState()" class="btn btn-info mr-2">
        <fa-icon icon="arrow-left"></fa-icon>&nbsp;
        <span jhiTranslate="entity.action.back">Voltar</span>
      </button>

      <button class="btn btn-info mr-2" (click)="loadPage()" [disabled]="isLoading">
        <fa-icon icon="sync" [spin]="isLoading"></fa-icon>
        <span jhiTranslate="farolDocsApp.etiqueta.home.refreshListLabel">Refresh List</span>
      </button>

      <button id="jh-create-entity" data-cy="entityCreateButton"
        class="btn btn-primary jh-create-entity create-etiqueta" [routerLink]="['/etiqueta/new']">
        <fa-icon icon="plus"></fa-icon>
        <span class="hidden-sm-down" jhiTranslate="farolDocsApp.etiqueta.home.createLabel"> Create a new Etiqueta
        </span>
      </button>
    </div>
  </h2>

  <jhi-alert-error></jhi-alert-error>

  <jhi-alert></jhi-alert>

  <div class="row">
    <div class="col-sm-12">
      <form name="searchForm" class="form-inline">
        <div class="input-group w-100 mt-3">
          <input type="text" class="form-control" [(ngModel)]="currentSearch" id="currentSearch" name="currentSearch"
            placeholder="{{ 'farolDocsApp.etiqueta.home.search' | translate }}" />

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

  <div class="alert alert-warning" id="no-result" *ngIf="etiquetas?.length === 0">
    <span jhiTranslate="farolDocsApp.etiqueta.home.notFound">No etiquetas found</span>
  </div>

  <div class="table-responsive" id="entities" *ngIf="etiquetas && etiquetas.length > 0">
    <table class="table table-striped" aria-describedby="page-heading">
      <thead>
        <tr jhiSort [(predicate)]="predicate" [(ascending)]="ascending" [callback]="loadPage.bind(this)">
          <th scope="col" jhiSortBy="id"><span jhiTranslate="global.field.id">ID</span>
            <fa-icon icon="sort"></fa-icon>
          </th>
          <th scope="col" jhiSortBy="nome"><span jhiTranslate="farolDocsApp.etiqueta.nome">Nome</span>
            <fa-icon icon="sort"></fa-icon>
          </th>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let etiqueta of etiquetas; trackBy: trackId" data-cy="entityTable">
          <td>
            <a [routerLink]="['/etiqueta', etiqueta.id, 'view']">{{ etiqueta.id }}</a>
          </td>
          <td>{{ etiqueta.nome }}</td>
          <td class="text-right">
            <div class="btn-group">

              <button type="submit" (click)="select(etiqueta)" class="btn btn-secondary btn-sm"
                data-cy="entityDetailsButton">
                <fa-icon icon="check"></fa-icon>
                <span class="d-none d-md-inline" jhiTranslate="entity.action.view">Select</span>
              </button>





              <button type="submit" [routerLink]="['/etiqueta', etiqueta.id, 'edit']" class="btn btn-primary btn-sm"
                data-cy="entityEditButton">
                <fa-icon icon="pencil-alt"></fa-icon>
                <span class="d-none d-md-inline" jhiTranslate="entity.action.edit">Edit</span>
              </button>

              <button type="submit" (click)="delete(etiqueta)" class="btn btn-danger btn-sm"
                data-cy="entityDeleteButton">
                <fa-icon icon="times"></fa-icon>
                <span class="d-none d-md-inline" jhiTranslate="entity.action.delete">Delete</span>
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <div *ngIf="etiquetas && etiquetas.length > 0">
    <div class="row justify-content-center">
      <jhi-item-count [params]="{ page: page, totalItems: totalItems, itemsPerPage: itemsPerPage }"></jhi-item-count>
    </div>

    <div class="row justify-content-center">
      <ngb-pagination [collectionSize]="totalItems" [(page)]="ngbPaginationPage" [pageSize]="itemsPerPage" [maxSize]="5"
        [rotate]="true" [boundaryLinks]="true" (pageChange)="loadPage($event)"></ngb-pagination>
    </div>
  </div>
</div>

```

##### 3.2-No component EtiquetaComponent criar evento para registrar a etiqueta selecionada (click).



Crie os dois métodos abaixo logo antes dos métodod `protected`


<p align="center">
   <strong>Listagem 3.2 - Arquivo src/main/webapp/app/entities/etiqueta/list/etiqueta.component.ts </strong> 
</p>      


```typescript
  select(etiqueta: IEtiqueta):void {
    this.etiquetaService.pushEtiqueta(etiqueta) 
   }
  previousState(): void {
    window.history.back();
  }
```


#### 4-No component EtiquetaService fazer as alterações para armazenar as etiquetas selecionadas em um array .

Com as alterações seu arquivo ficará assim:

<p align="center">
   <strong>Listagem 4 - Arquivo src/main/webapp/app/entities/etiqueta/service/etiqueta.service.ts </strong> 
</p>      


```typescript
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEtiqueta, getEtiquetaIdentifier } from '../etiqueta.model';

export type EntityResponseType = HttpResponse<IEtiqueta>;
export type EntityArrayResponseType = HttpResponse<IEtiqueta[]>;

@Injectable({ providedIn: 'root' })
export class EtiquetaService {

  etiquetas: IEtiqueta[] = [];//Array para armazenar as etiquetas
  

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/etiquetas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/etiquetas');
  

  constructor(protected http: HttpClient, 
    protected applicationConfigService: ApplicationConfigService,
     ) {}

  create(etiqueta: IEtiqueta): Observable<EntityResponseType> {
    return this.http.post<IEtiqueta>(this.resourceUrl, etiqueta, { observe: 'response' });
  }

  update(etiqueta: IEtiqueta): Observable<EntityResponseType> {
    return this.http.put<IEtiqueta>(`${this.resourceUrl}/${getEtiquetaIdentifier(etiqueta) as number}`, etiqueta, { observe: 'response' });
  }

  partialUpdate(etiqueta: IEtiqueta): Observable<EntityResponseType> {
    return this.http.patch<IEtiqueta>(`${this.resourceUrl}/${getEtiquetaIdentifier(etiqueta) as number}`, etiqueta, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEtiqueta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtiqueta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtiqueta[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addEtiquetaToCollectionIfMissing(etiquetaCollection: IEtiqueta[], ...etiquetasToCheck: (IEtiqueta | null | undefined)[]): IEtiqueta[] {
    const etiquetas: IEtiqueta[] = etiquetasToCheck.filter(isPresent);
    if (etiquetas.length > 0) {
      const etiquetaCollectionIdentifiers = etiquetaCollection.map(etiquetaItem => getEtiquetaIdentifier(etiquetaItem)!);
      const etiquetasToAdd = etiquetas.filter(etiquetaItem => {
        const etiquetaIdentifier = getEtiquetaIdentifier(etiquetaItem);
        if (etiquetaIdentifier == null || etiquetaCollectionIdentifiers.includes(etiquetaIdentifier)) {
          return false;
        }
        etiquetaCollectionIdentifiers.push(etiquetaIdentifier);
        return true;
      });
      return [...etiquetasToAdd, ...etiquetaCollection];
    }
    return etiquetaCollection;
  }

  pushEtiqueta(etiqueta: IEtiqueta):void { //Método criado para gravar as etiquetas em array
    this.etiquetas.push(etiqueta);
  }
}

```


::: :pushpin: Importante :::

Ao final de todo o processo, você terá que ter 6 mudanças no Git.


Salve, compile, execute e teste.

```
mvn
```