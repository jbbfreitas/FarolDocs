jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentoService } from '../service/documento.service';
import { IDocumento, Documento } from '../documento.model';

import { DocumentoUpdateComponent } from './documento-update.component';

describe('Component Tests', () => {
  describe('Documento Management Update Component', () => {
    let comp: DocumentoUpdateComponent;
    let fixture: ComponentFixture<DocumentoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentoService: DocumentoService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const documento: IDocumento = { id: 456 };

        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(documento));
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
  });
});
