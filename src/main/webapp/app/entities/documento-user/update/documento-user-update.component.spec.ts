jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentoUserService } from '../service/documento-user.service';
import { IDocumentoUser, DocumentoUser } from '../documento-user.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDocumento } from 'app/entities/documento/documento.model';
import { DocumentoService } from 'app/entities/documento/service/documento.service';

import { DocumentoUserUpdateComponent } from './documento-user-update.component';

describe('Component Tests', () => {
  describe('DocumentoUser Management Update Component', () => {
    let comp: DocumentoUserUpdateComponent;
    let fixture: ComponentFixture<DocumentoUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentoUserService: DocumentoUserService;
    let userService: UserService;
    let documentoService: DocumentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentoUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentoUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentoUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentoUserService = TestBed.inject(DocumentoUserService);
      userService = TestBed.inject(UserService);
      documentoService = TestBed.inject(DocumentoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const documentoUser: IDocumentoUser = { id: 456 };
        const user: IUser = { id: 14967 };
        documentoUser.user = user;

        const userCollection: IUser[] = [{ id: 93508 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentoUser });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Documento query and add missing value', () => {
        const documentoUser: IDocumentoUser = { id: 456 };
        const documento: IDocumento = { id: 47793 };
        documentoUser.documento = documento;

        const documentoCollection: IDocumento[] = [{ id: 89226 }];
        jest.spyOn(documentoService, 'query').mockReturnValue(of(new HttpResponse({ body: documentoCollection })));
        const additionalDocumentos = [documento];
        const expectedCollection: IDocumento[] = [...additionalDocumentos, ...documentoCollection];
        jest.spyOn(documentoService, 'addDocumentoToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentoUser });
        comp.ngOnInit();

        expect(documentoService.query).toHaveBeenCalled();
        expect(documentoService.addDocumentoToCollectionIfMissing).toHaveBeenCalledWith(documentoCollection, ...additionalDocumentos);
        expect(comp.documentosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const documentoUser: IDocumentoUser = { id: 456 };
        const user: IUser = { id: 70044 };
        documentoUser.user = user;
        const documento: IDocumento = { id: 14150 };
        documentoUser.documento = documento;

        activatedRoute.data = of({ documentoUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(documentoUser));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.documentosSharedCollection).toContain(documento);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentoUser>>();
        const documentoUser = { id: 123 };
        jest.spyOn(documentoUserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentoUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentoUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(documentoUserService.update).toHaveBeenCalledWith(documentoUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentoUser>>();
        const documentoUser = new DocumentoUser();
        jest.spyOn(documentoUserService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentoUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentoUser }));
        saveSubject.complete();

        // THEN
        expect(documentoUserService.create).toHaveBeenCalledWith(documentoUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentoUser>>();
        const documentoUser = { id: 123 };
        jest.spyOn(documentoUserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentoUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(documentoUserService.update).toHaveBeenCalledWith(documentoUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDocumentoById', () => {
        it('Should return tracked Documento primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDocumentoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
