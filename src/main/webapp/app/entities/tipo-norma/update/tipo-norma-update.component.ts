import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoNorma, TipoNorma } from '../tipo-norma.model';
import { TipoNormaService } from '../service/tipo-norma.service';

@Component({
  selector: 'jhi-tipo-norma-update',
  templateUrl: './tipo-norma-update.component.html',
})
export class TipoNormaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tipo: [null, [Validators.required]],
    descricao: [],
  });

  constructor(protected tipoNormaService: TipoNormaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoNorma }) => {
      this.updateForm(tipoNorma);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoNorma = this.createFromForm();
    if (tipoNorma.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoNormaService.update(tipoNorma));
    } else {
      this.subscribeToSaveResponse(this.tipoNormaService.create(tipoNorma));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoNorma>>): void {
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

  protected updateForm(tipoNorma: ITipoNorma): void {
    this.editForm.patchValue({
      id: tipoNorma.id,
      tipo: tipoNorma.tipo,
      descricao: tipoNorma.descricao,
    });
  }

  protected createFromForm(): ITipoNorma {
    return {
      ...new TipoNorma(),
      id: this.editForm.get(['id'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
    };
  }
}
