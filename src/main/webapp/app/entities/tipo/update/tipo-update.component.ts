import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipo, Tipo } from '../tipo.model';
import { TipoService } from '../service/tipo.service';

@Component({
  selector: 'jhi-tipo-update',
  templateUrl: './tipo-update.component.html',
})
export class TipoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    descricao: [],
  });

  constructor(protected tipoService: TipoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipo }) => {
      this.updateForm(tipo);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipo = this.createFromForm();
    if (tipo.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoService.update(tipo));
    } else {
      this.subscribeToSaveResponse(this.tipoService.create(tipo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipo>>): void {
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

  protected updateForm(tipo: ITipo): void {
    this.editForm.patchValue({
      id: tipo.id,
      nome: tipo.nome,
      descricao: tipo.descricao,
    });
  }

  protected createFromForm(): ITipo {
    return {
      ...new Tipo(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
    };
  }
}
