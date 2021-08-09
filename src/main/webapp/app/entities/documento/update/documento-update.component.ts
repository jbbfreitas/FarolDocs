import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumento, Documento } from '../documento.model';
import { DocumentoService } from '../service/documento.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProjeto } from 'app/entities/projeto/projeto.model';
import { ProjetoService } from 'app/entities/projeto/service/projeto.service';
import { ITipo } from 'app/entities/tipo/tipo.model';
import { TipoService } from 'app/entities/tipo/service/tipo.service';

@Component({
  selector: 'jhi-documento-update',
  templateUrl: './documento-update.component.html',
})
export class DocumentoUpdateComponent implements OnInit {
  isSaving = false;

  projetosSharedCollection: IProjeto[] = [];
  tiposSharedCollection: ITipo[] = [];

  editForm = this.fb.group({
    id: [],
    assunto: [],
    descricao: [],
    etiqueta: [],
    ementa: [],
    url: [null, [Validators.pattern('^(https?|ftp|file)://[-a-zA-Z0-9+&amp;@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&amp;@#/%=~_|]')]],
    projeto: [],
    tipo: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected documentoService: DocumentoService,
    protected projetoService: ProjetoService,
    protected tipoService: TipoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documento }) => {
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
      etiqueta: documento.etiqueta,
      ementa: documento.ementa,
      url: documento.url,
      projeto: documento.projeto,
      tipo: documento.tipo,
    });

    this.projetosSharedCollection = this.projetoService.addProjetoToCollectionIfMissing(this.projetosSharedCollection, documento.projeto);
    this.tiposSharedCollection = this.tipoService.addTipoToCollectionIfMissing(this.tiposSharedCollection, documento.tipo);
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
  }

  protected createFromForm(): IDocumento {
    return {
      ...new Documento(),
      id: this.editForm.get(['id'])!.value,
      assunto: this.editForm.get(['assunto'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      etiqueta: this.editForm.get(['etiqueta'])!.value,
      ementa: this.editForm.get(['ementa'])!.value,
      url: this.editForm.get(['url'])!.value,
      projeto: this.editForm.get(['projeto'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
    };
  }
}
