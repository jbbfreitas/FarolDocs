import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumentoUser, DocumentoUser } from '../documento-user.model';
import { DocumentoUserService } from '../service/documento-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDocumento } from 'app/entities/documento/documento.model';
import { DocumentoService } from 'app/entities/documento/service/documento.service';

@Component({
  selector: 'jhi-documento-user-update',
  templateUrl: './documento-user-update.component.html',
})
export class DocumentoUserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  documentosSharedCollection: IDocumento[] = [];

  editForm = this.fb.group({
    id: [],
    user: [],
    documento: [],
  });

  constructor(
    protected documentoUserService: DocumentoUserService,
    protected userService: UserService,
    protected documentoService: DocumentoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentoUser }) => {
      this.updateForm(documentoUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentoUser = this.createFromForm();
    if (documentoUser.id !== undefined) {
      this.subscribeToSaveResponse(this.documentoUserService.update(documentoUser));
    } else {
      this.subscribeToSaveResponse(this.documentoUserService.create(documentoUser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackDocumentoById(index: number, item: IDocumento): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentoUser>>): void {
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

  protected updateForm(documentoUser: IDocumentoUser): void {
    this.editForm.patchValue({
      id: documentoUser.id,
      user: documentoUser.user,
      documento: documentoUser.documento,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, documentoUser.user);
    this.documentosSharedCollection = this.documentoService.addDocumentoToCollectionIfMissing(
      this.documentosSharedCollection,
      documentoUser.documento
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

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

  protected createFromForm(): IDocumentoUser {
    return {
      ...new DocumentoUser(),
      id: this.editForm.get(['id'])!.value,
      user: this.editForm.get(['user'])!.value,
      documento: this.editForm.get(['documento'])!.value,
    };
  }
}
