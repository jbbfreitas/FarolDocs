import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOrgaoEmissor, OrgaoEmissor } from '../orgao-emissor.model';
import { OrgaoEmissorService } from '../service/orgao-emissor.service';

@Component({
  selector: 'jhi-orgao-emissor-update',
  templateUrl: './orgao-emissor-update.component.html',
})
export class OrgaoEmissorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sigla: [null, [Validators.required]],
    nome: [null, [Validators.required]],
  });

  constructor(protected orgaoEmissorService: OrgaoEmissorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orgaoEmissor }) => {
      this.updateForm(orgaoEmissor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orgaoEmissor = this.createFromForm();
    if (orgaoEmissor.id !== undefined) {
      this.subscribeToSaveResponse(this.orgaoEmissorService.update(orgaoEmissor));
    } else {
      this.subscribeToSaveResponse(this.orgaoEmissorService.create(orgaoEmissor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgaoEmissor>>): void {
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

  protected updateForm(orgaoEmissor: IOrgaoEmissor): void {
    this.editForm.patchValue({
      id: orgaoEmissor.id,
      sigla: orgaoEmissor.sigla,
      nome: orgaoEmissor.nome,
    });
  }

  protected createFromForm(): IOrgaoEmissor {
    return {
      ...new OrgaoEmissor(),
      id: this.editForm.get(['id'])!.value,
      sigla: this.editForm.get(['sigla'])!.value,
      nome: this.editForm.get(['nome'])!.value,
    };
  }
}
