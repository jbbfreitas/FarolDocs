## Documentação da Realease 0.12.0

Antes de começar, certifique-se de que o seu repositório esteja atualizado até a release `0.11.1` e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a esse estado.

### O que iremos fazer nesta release? 

Nesta versão vamos fazer apenas uma mudança. Apesar de ser apenas uma elá terá um impacto substancial no projeto pois irá permitir o compartilhamento dos documentos com um ou mais usuários.

Observe que até o momento não foi feita nenhuma vinculação de documento com usuários. A pergunta que deveria ser feita é a seguinte: quais usuários podem ver os meus documentos?

A resposta para essa pergunta é o que iremos fazer nesta versão.

## Implementando as mudanças

Vamos criar um relacionamento na razão `m:n` com a entidade `User`. Essa entidade foi criada automaticamente pelo jHipster e não devemos alterá-la sob pena de `crash` na aplicação.

Vamos deixar o jHipster trabalhar por nós e depois fazemos os ajustes necessários:

1. Altere a entidade `Documento` para conter um relacionamento `m:n` com `User`, digitando:

```
jhipster entity Documento

Generating field #9

? Do you want to add a field to your entity? No

Generating relationships to other entities

? Do you want to add a relationship to another entity? Yes
? What is the name of the other entity? User
? What is the name of the relationship? user
? What is the type of the relationship? many-to-many
? Do you want to add any validation rules to this relationship? No

Generating relationships to other entities

? Do you want to add a relationship to another entity? No

```

2. A criação desse relacionamento irá alterar "desfazer" mudanças que fizemos, então teremos que contar com a ajuda do git para resgatar as mudanças feitas e que devem ser conservadas.

Os seguintes Arquivos deverão ser restaurados com a ajuda do git e depois enchertados manualmente para conter o relacionamento m:n com `User`

 - src/main/webapp/app/entities/documento/update/documento-update.component.html

 A versão final deste arquivo será a seguinte:

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
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.descricao" for="field_descricao">Descricao</label>
          <input type="text" class="form-control" name="descricao" id="field_descricao" data-cy="descricao" formControlName="descricao" />
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
          <label class="form-control-label" jhiTranslate="farolDocsApp.documento.ementa" for="field_ementa">Ementa</label>
          <textarea class="form-control" name="ementa" id="field_ementa" data-cy="ementa" formControlName="ementa"></textarea>
        </div>



        <div class="form-group">
          <label jhiTranslate="farolDocsApp.documento.etiqueta" for="field_etiquetas">Etiqueta</label>
          <select class="form-control" id="field_etiquetas" data-cy="etiqueta" multiple name="etiquetas" formControlName="etiquetas">
            <option
              [ngValue]="getSelectedEtiqueta(etiquetaOption, editForm.get('etiquetas')!.value)"
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
        <div class="form-group">
          <label jhiTranslate="farolDocsApp.documento.user" for="field_users">User</label>
          <select class="form-control" id="field_users" data-cy="user" multiple name="users" formControlName="users">
            <option
              [ngValue]="getSelectedUser(userOption, editForm.get('users')!.value)"
              *ngFor="let userOption of usersSharedCollection; trackBy: trackUserById"
            >
              {{ userOption.login }}
            </option>
          </select>
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
- src/main/webapp/app/entities/documento/update/documento-update.component.ts

A versão final deste arquivo será a seguinte:

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
import { IEtiqueta } from 'app/entities/etiqueta/etiqueta.model';
import { EtiquetaService } from 'app/entities/etiqueta/service/etiqueta.service';
import { IOrgaoEmissor } from 'app/entities/orgao-emissor/orgao-emissor.model';
import { OrgaoEmissorService } from 'app/entities/orgao-emissor/service/orgao-emissor.service';
import { ITipoNorma } from 'app/entities/tipo-norma/tipo-norma.model';
import { TipoNormaService } from 'app/entities/tipo-norma/service/tipo-norma.service';
import { SituacaoDocumento } from 'app/entities/enumerations/situacao-documento.model';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-documento-update',
  templateUrl: './documento-update.component.html',
})
export class DocumentoUpdateComponent implements OnInit {
  isSaving = false;

  projetosSharedCollection: IProjeto[] = [];
  tiposSharedCollection: ITipo[] = [];
  etiquetasSharedCollection: IEtiqueta[] = [];
  orgaoEmissorsSharedCollection: IOrgaoEmissor[] = [];
  tipoNormasSharedCollection: ITipoNorma[] = [];
  usersSharedCollection: IUser[] = [];

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
    etiquetas: [],
    orgaoEmissor: [],
    tipoNorma: [],
    users: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected documentoService: DocumentoService,
    protected projetoService: ProjetoService,
    protected tipoService: TipoService,
    protected etiquetaService: EtiquetaService,
    protected orgaoEmissorService: OrgaoEmissorService,
    protected tipoNormaService: TipoNormaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documento }) => {
      if (documento.id === undefined) {
        const today = dayjs().startOf('day');
        documento.criacao = today;
      }

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

  trackEtiquetaById(index: number, item: IEtiqueta): number {
    return item.id!;
  }

  trackOrgaoEmissorById(index: number, item: IOrgaoEmissor): number {
    return item.id!;
  }

  trackTipoNormaById(index: number, item: ITipoNorma): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  getSelectedEtiqueta(option: IEtiqueta, selectedVals?: IEtiqueta[]): IEtiqueta {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedUser(option: IUser, selectedVals?: IUser[]): IUser {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
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
      situacao: documento.situacao !=null?  documento.situacao : SituacaoDocumento.VIGENTE, // Default VIGENTE
      criacao: documento.criacao ? documento.criacao.format(DATE_TIME_FORMAT) : null,
      projeto: documento.projeto,
      tipo: documento.tipo,
      etiquetas: documento.etiquetas,
      orgaoEmissor: documento.orgaoEmissor,
      tipoNorma: documento.tipoNorma,
      users: documento.users,
    });

    this.projetosSharedCollection = this.projetoService.addProjetoToCollectionIfMissing(this.projetosSharedCollection, documento.projeto);
    this.tiposSharedCollection = this.tipoService.addTipoToCollectionIfMissing(this.tiposSharedCollection, documento.tipo);
    this.etiquetasSharedCollection = this.etiquetaService.addEtiquetaToCollectionIfMissing(
      this.etiquetasSharedCollection,
      ...(documento.etiquetas ?? [])
    );
    this.orgaoEmissorsSharedCollection = this.orgaoEmissorService.addOrgaoEmissorToCollectionIfMissing(
      this.orgaoEmissorsSharedCollection,
      documento.orgaoEmissor
    );
    this.tipoNormasSharedCollection = this.tipoNormaService.addTipoNormaToCollectionIfMissing(
      this.tipoNormasSharedCollection,
      documento.tipoNorma
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, ...(documento.users ?? []));
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

    this.etiquetaService
      .query()
      .pipe(map((res: HttpResponse<IEtiqueta[]>) => res.body ?? []))
      .pipe(
        map((etiquetas: IEtiqueta[]) =>
          this.etiquetaService.addEtiquetaToCollectionIfMissing(etiquetas, ...(this.editForm.get('etiquetas')!.value ?? []))
        )
      )
      .subscribe((etiquetas: IEtiqueta[]) => (this.etiquetasSharedCollection = etiquetas));

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

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, ...(this.editForm.get('users')!.value ?? []))))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
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
      etiquetas: this.editForm.get(['etiquetas'])!.value,
      orgaoEmissor: this.editForm.get(['orgaoEmissor'])!.value,
      tipoNorma: this.editForm.get(['tipoNorma'])!.value,
      users: this.editForm.get(['users'])!.value,
    };
  }
}

 ```
- src/main/webapp/app/entities/documento/list/documento.component.html

Esse arquivo deverá voltar com o mesmo valor que possuia na release 0.11.1, cuja versão final é a seguinte:

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

- src/main/webapp/i18n/en/documento.json

Esse arquivo deverá voltar com o mesmo valor que possuia na release 0.11.1 (acrescida da última linha), cuja versão final é a seguinte:

```json
{
  "farolDocsApp": {
    "documento": {
      "home": {
        "title": "Documents",
        "refreshListLabel": "Refresh list",
        "createLabel": "Create a new Document",
        "createOrEditLabel": "Create or edit a Document",
        "search": "Search for Documents",
        "notFound": "No Documents found"
      },
      "created": "A new Documento is created with identifier {{ param }}",
      "updated": "A Documento is updated with identifier {{ param }}",
      "deleted": "A Documento is deleted with identifier {{ param }}",
      "delete": {
        "question": "Are you sure you want to delete Document {{ id }}?"
      },
      "detail": {
        "title": "Document"
      },
      "id": "ID",
      "assunto": "Subject",
      "descricao": "Description",
      "ementa": "Resume",
      "url": "Url",
      "numero": "Number",
      "ano": "Year",
      "situacao": "Status",
      "criacao": "Created at",
      "projeto": "Project",
      "tipo": "Kind",
      "etiqueta": "Tag",
      "orgaoEmissor": "Agency",
      "tipoNorma": "Type of Norm",
      "user": "User"
    }
  }
}
```

- src/main/webapp/i18n/pt-br/documento.json

Esse arquivo deverá voltar com o mesmo valor que possuia na release 0.11.1 (acrescida da última linha), cuja versão final é a seguinte:

```json
{
  "farolDocsApp": {
    "documento": {
      "home": {
        "title": "Documentos",
        "refreshListLabel": "Atualizar lista",
        "createLabel": "Criar novo Documento",
        "createOrEditLabel": "Criar ou editar Documento",
        "search": "Pesquisar por Documento",
        "notFound": "Nenhum Documento encontrado"
      },
      "created": "Um novo Documento foi criado com o identificador {{ param }}",
      "updated": "Um Documento foi atualizado com o identificador {{ param }}",
      "deleted": "Um Documento foi excluído com o identificador {{ param }}",
      "delete": {
        "question": "Tem certeza de que deseja excluir Documento {{ id }}?"
      },
      "detail": {
        "title": "Documento"
      },
      "id": "ID",
      "assunto": "Assunto",
      "descricao": "Descrição",
      "ementa": "Ementa",
      "url": "Url",
      "numero": "Número",
      "ano": "Ano",
      "situacao": "Situação",
      "criacao": "Criação",
      "projeto": "Projeto",
      "tipo": "Tipo",
      "etiqueta": "Etiqueta",
      "orgaoEmissor": "Órgão Emissor",
      "tipoNorma": "Tipo de Norma",
      "user": "User"
    }
  }
}

```

- src/main/webapp/i18n/pt-br/orgaoEmissor.json

Esse arquivo deverá voltar com o mesmo valor que possuia na release 0.11.1 cuja versão final é a seguinte:

```json
{
  "farolDocsApp": {
    "orgaoEmissor": {
      "home": {
        "title": "Órgãos Emissores",
        "refreshListLabel": "Atualizar lista",
        "createLabel": "Criar novo Órgão Emissor",
        "createOrEditLabel": "Criar ou editar Órgãos Emissores",
        "search": "Pesquisar por Órgãos Emissores",
        "notFound": "Nenhum Órgão Emissor encontrado"
      },
      "created": "Um novo Órgãos Emissor foi criado com o identificador {{ param }}",
      "updated": "Um Órgão Emissor foi atualizado com o identificador {{ param }}",
      "deleted": "Um Órgão Emissor foi excluído com o identificador {{ param }}",
      "delete": {
        "question": "Tem certeza de que deseja excluir Órgão Emissor {{ id }}?"
      },
      "detail": {
        "title": "Órgãos Emissores"
      },
      "id": "ID",
      "sigla": "Sigla",
      "nome": "Nome"
    }  }
}
```


