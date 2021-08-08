import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProjeto, Projeto } from '../projeto.model';
import { ProjetoService } from '../service/projeto.service';

@Component({
  selector: 'jhi-projeto-update',
  templateUrl: './projeto-update.component.html',
})
export class ProjetoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    inicio: [null, [Validators.required]],
    fim: [],
    situacao: [],
  });

  constructor(protected projetoService: ProjetoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projeto }) => {
      if (projeto.id === undefined) {
        const today = dayjs().startOf('day');
        projeto.inicio = today;
        projeto.fim = today;
      }

      this.updateForm(projeto);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const projeto = this.createFromForm();
    if (projeto.id !== undefined) {
      this.subscribeToSaveResponse(this.projetoService.update(projeto));
    } else {
      this.subscribeToSaveResponse(this.projetoService.create(projeto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjeto>>): void {
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

  protected updateForm(projeto: IProjeto): void {
    this.editForm.patchValue({
      id: projeto.id,
      nome: projeto.nome,
      inicio: projeto.inicio ? projeto.inicio.format(DATE_TIME_FORMAT) : null,
      fim: projeto.fim ? projeto.fim.format(DATE_TIME_FORMAT) : null,
      situacao: projeto.situacao,
    });
  }

  protected createFromForm(): IProjeto {
    return {
      ...new Projeto(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      inicio: this.editForm.get(['inicio'])!.value ? dayjs(this.editForm.get(['inicio'])!.value, DATE_TIME_FORMAT) : undefined,
      fim: this.editForm.get(['fim'])!.value ? dayjs(this.editForm.get(['fim'])!.value, DATE_TIME_FORMAT) : undefined,
      situacao: this.editForm.get(['situacao'])!.value,
    };
  }
}
