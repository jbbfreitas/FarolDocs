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
    etiqueta: [],
    orgaoEmissor: [],
    tipoNorma: [],
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
      etiqueta: documento.etiqueta,
      orgaoEmissor: documento.orgaoEmissor,
      tipoNorma: documento.tipoNorma,
    });

    this.projetosSharedCollection = this.projetoService.addProjetoToCollectionIfMissing(this.projetosSharedCollection, documento.projeto);
    this.tiposSharedCollection = this.tipoService.addTipoToCollectionIfMissing(this.tiposSharedCollection, documento.tipo);
    this.etiquetasSharedCollection = this.etiquetaService.addEtiquetaToCollectionIfMissing(
      this.etiquetasSharedCollection,
      documento.etiqueta
    );
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

    this.etiquetaService
      .query()
      .pipe(map((res: HttpResponse<IEtiqueta[]>) => res.body ?? []))
      .pipe(
        map((etiquetas: IEtiqueta[]) =>
          this.etiquetaService.addEtiquetaToCollectionIfMissing(etiquetas, this.editForm.get('etiqueta')!.value)
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
      etiqueta: this.editForm.get(['etiqueta'])!.value,
      orgaoEmissor: this.editForm.get(['orgaoEmissor'])!.value,
      tipoNorma: this.editForm.get(['tipoNorma'])!.value,
    };
  }
}
