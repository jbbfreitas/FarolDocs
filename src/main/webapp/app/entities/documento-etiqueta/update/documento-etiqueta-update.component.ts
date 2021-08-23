import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumentoEtiqueta, DocumentoEtiqueta } from '../documento-etiqueta.model';
import { DocumentoEtiquetaService } from '../service/documento-etiqueta.service';
import { IEtiqueta } from 'app/entities/etiqueta/etiqueta.model';
import { EtiquetaService } from 'app/entities/etiqueta/service/etiqueta.service';
import { IDocumento } from 'app/entities/documento/documento.model';
import { DocumentoService } from 'app/entities/documento/service/documento.service';

@Component({
  selector: 'jhi-documento-etiqueta-update',
  templateUrl: './documento-etiqueta-update.component.html',
})
export class DocumentoEtiquetaUpdateComponent implements OnInit {
  isSaving = false;

  etiquetasSharedCollection: IEtiqueta[] = [];
  documentosSharedCollection: IDocumento[] = [];

  editForm = this.fb.group({
    id: [],
    etiqueta: [],
    documento: [],
  });

  constructor(
    protected documentoEtiquetaService: DocumentoEtiquetaService,
    protected etiquetaService: EtiquetaService,
    protected documentoService: DocumentoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentoEtiqueta }) => {
      this.updateForm(documentoEtiqueta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentoEtiqueta = this.createFromForm();
    if (documentoEtiqueta.id !== undefined) {
      this.subscribeToSaveResponse(this.documentoEtiquetaService.update(documentoEtiqueta));
    } else {
      this.subscribeToSaveResponse(this.documentoEtiquetaService.create(documentoEtiqueta));
    }
  }

  trackEtiquetaById(index: number, item: IEtiqueta): number {
    return item.id!;
  }

  trackDocumentoById(index: number, item: IDocumento): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentoEtiqueta>>): void {
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

  protected updateForm(documentoEtiqueta: IDocumentoEtiqueta): void {
    this.editForm.patchValue({
      id: documentoEtiqueta.id,
      etiqueta: documentoEtiqueta.etiqueta,
      documento: documentoEtiqueta.documento,
    });

    this.etiquetasSharedCollection = this.etiquetaService.addEtiquetaToCollectionIfMissing(
      this.etiquetasSharedCollection,
      documentoEtiqueta.etiqueta
    );
    this.documentosSharedCollection = this.documentoService.addDocumentoToCollectionIfMissing(
      this.documentosSharedCollection,
      documentoEtiqueta.documento
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etiquetaService
      .query()
      .pipe(map((res: HttpResponse<IEtiqueta[]>) => res.body ?? []))
      .pipe(
        map((etiquetas: IEtiqueta[]) =>
          this.etiquetaService.addEtiquetaToCollectionIfMissing(etiquetas, this.editForm.get('etiqueta')!.value)
        )
      )
      .subscribe((etiquetas: IEtiqueta[]) => (this.etiquetasSharedCollection = etiquetas));

    this.documentoService
      .query()
      .pipe(map((res: HttpResponse<IDocumento[]>) => res.body ?? []))
      .pipe(
        map((documentos: IDocumento[]) =>
          this.documentoService.addDocumentoToCollectionIfMissing(documentos, this.editForm.get('documento')!.value)
        )
      )
      .subscribe((documentos: IDocumento[]) => (this.documentosSharedCollection = documentos));
  }

  protected createFromForm(): IDocumentoEtiqueta {
    return {
      ...new DocumentoEtiqueta(),
      id: this.editForm.get(['id'])!.value,
      etiqueta: this.editForm.get(['etiqueta'])!.value,
      documento: this.editForm.get(['documento'])!.value,
    };
  }
}
