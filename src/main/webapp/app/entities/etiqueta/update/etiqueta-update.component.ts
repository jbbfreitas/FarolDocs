import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEtiqueta, Etiqueta } from '../etiqueta.model';
import { EtiquetaService } from '../service/etiqueta.service';

@Component({
  selector: 'jhi-etiqueta-update',
  templateUrl: './etiqueta-update.component.html',
})
export class EtiquetaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
  });

  constructor(protected etiquetaService: EtiquetaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etiqueta }) => {
      this.updateForm(etiqueta);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etiqueta = this.createFromForm();
    if (etiqueta.id !== undefined) {
      this.subscribeToSaveResponse(this.etiquetaService.update(etiqueta));
    } else {
      this.subscribeToSaveResponse(this.etiquetaService.create(etiqueta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtiqueta>>): void {
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

  protected updateForm(etiqueta: IEtiqueta): void {
    this.editForm.patchValue({
      id: etiqueta.id,
      nome: etiqueta.nome,
    });
  }

  protected createFromForm(): IEtiqueta {
    return {
      ...new Etiqueta(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
    };
  }
}
