import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDocumento, Documento } from '../documento.model';
import { DocumentoService } from '../service/documento.service';

@Component({
  selector: 'jhi-documento-update',
  templateUrl: './documento-update.component.html',
})
export class DocumentoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    projeto: [],
    assunto: [],
    descricao: [],
    etiqueta: [],
    url: [],
  });

  constructor(protected documentoService: DocumentoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documento }) => {
      this.updateForm(documento);
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
      projeto: documento.projeto,
      assunto: documento.assunto,
      descricao: documento.descricao,
      etiqueta: documento.etiqueta,
      url: documento.url,
    });
  }

  protected createFromForm(): IDocumento {
    return {
      ...new Documento(),
      id: this.editForm.get(['id'])!.value,
      projeto: this.editForm.get(['projeto'])!.value,
      assunto: this.editForm.get(['assunto'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      etiqueta: this.editForm.get(['etiqueta'])!.value,
      url: this.editForm.get(['url'])!.value,
    };
  }
}
