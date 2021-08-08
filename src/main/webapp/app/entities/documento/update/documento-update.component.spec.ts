jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentoService } from '../service/documento.service';
import { IDocumento, Documento } from '../documento.model';
import { IProjeto } from 'app/entities/projeto/projeto.model';
import { ProjetoService } from 'app/entities/projeto/service/projeto.service';

import { DocumentoUpdateComponent } from './documento-update.component';

describe('Component Tests', () => {
  describe('Documento Management Update Component', () => {
    let comp: DocumentoUpdateComponent;
    let fixture: ComponentFixture<DocumentoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentoService: DocumentoService;
    let projetoService: ProjetoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentoService = TestBed.inject(DocumentoService);
      projetoService = TestBed.inject(ProjetoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Projeto query and add missing value', () => {
        const documento: IDocumento = { id: 456 };
        const projeto: IProjeto = { id: 36456 };
        documento.projeto = projeto;

        const projetoCollection: IProjeto[] = [{ id: 29048 }];
        jest.spyOn(projetoService, 'query').mockReturnValue(of(new HttpResponse({ body: projetoCollection })));
        const additionalProjetos = [projeto];
        const expectedCollection: IProjeto[] = [...additionalProjetos, ...projetoCollection];
        jest.spyOn(projetoService, 'addProjetoToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        expect(projetoService.query).toHaveBeenCalled();
        expect(projetoService.addProjetoToCollectionIfMissing).toHaveBeenCalledWith(projetoCollection, ...additionalProjetos);
        expect(comp.projetosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const documento: IDocumento = { id: 456 };
        const projeto: IProjeto = { id: 29505 };
        documento.projeto = projeto;

        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(documento));
        expect(comp.projetosSharedCollection).toContain(projeto);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Documento>>();
        const documento = { id: 123 };
        jest.spyOn(documentoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documento }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(documentoService.update).toHaveBeenCalledWith(documento);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Documento>>();
        const documento = new Documento();
        jest.spyOn(documentoService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documento }));
        saveSubject.complete();

        // THEN
        expect(documentoService.create).toHaveBeenCalledWith(documento);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Documento>>();
        const documento = { id: 123 };
        jest.spyOn(documentoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(documentoService.update).toHaveBeenCalledWith(documento);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProjetoById', () => {
        it('Should return tracked Projeto primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProjetoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
