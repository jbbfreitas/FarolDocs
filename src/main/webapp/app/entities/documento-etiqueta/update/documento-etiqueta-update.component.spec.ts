jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentoEtiquetaService } from '../service/documento-etiqueta.service';
import { IDocumentoEtiqueta, DocumentoEtiqueta } from '../documento-etiqueta.model';
import { IEtiqueta } from 'app/entities/etiqueta/etiqueta.model';
import { EtiquetaService } from 'app/entities/etiqueta/service/etiqueta.service';
import { IDocumento } from 'app/entities/documento/documento.model';
import { DocumentoService } from 'app/entities/documento/service/documento.service';

import { DocumentoEtiquetaUpdateComponent } from './documento-etiqueta-update.component';

describe('Component Tests', () => {
  describe('DocumentoEtiqueta Management Update Component', () => {
    let comp: DocumentoEtiquetaUpdateComponent;
    let fixture: ComponentFixture<DocumentoEtiquetaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentoEtiquetaService: DocumentoEtiquetaService;
    let etiquetaService: EtiquetaService;
    let documentoService: DocumentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentoEtiquetaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentoEtiquetaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentoEtiquetaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentoEtiquetaService = TestBed.inject(DocumentoEtiquetaService);
      etiquetaService = TestBed.inject(EtiquetaService);
      documentoService = TestBed.inject(DocumentoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Etiqueta query and add missing value', () => {
        const documentoEtiqueta: IDocumentoEtiqueta = { id: 456 };
        const etiqueta: IEtiqueta = { id: 95942 };
        documentoEtiqueta.etiqueta = etiqueta;

        const etiquetaCollection: IEtiqueta[] = [{ id: 50001 }];
        jest.spyOn(etiquetaService, 'query').mockReturnValue(of(new HttpResponse({ body: etiquetaCollection })));
        const additionalEtiquetas = [etiqueta];
        const expectedCollection: IEtiqueta[] = [...additionalEtiquetas, ...etiquetaCollection];
        jest.spyOn(etiquetaService, 'addEtiquetaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentoEtiqueta });
        comp.ngOnInit();

        expect(etiquetaService.query).toHaveBeenCalled();
        expect(etiquetaService.addEtiquetaToCollectionIfMissing).toHaveBeenCalledWith(etiquetaCollection, ...additionalEtiquetas);
        expect(comp.etiquetasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Documento query and add missing value', () => {
        const documentoEtiqueta: IDocumentoEtiqueta = { id: 456 };
        const documento: IDocumento = { id: 36881 };
        documentoEtiqueta.documento = documento;

        const documentoCollection: IDocumento[] = [{ id: 17477 }];
        jest.spyOn(documentoService, 'query').mockReturnValue(of(new HttpResponse({ body: documentoCollection })));
        const additionalDocumentos = [documento];
        const expectedCollection: IDocumento[] = [...additionalDocumentos, ...documentoCollection];
        jest.spyOn(documentoService, 'addDocumentoToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documentoEtiqueta });
        comp.ngOnInit();

        expect(documentoService.query).toHaveBeenCalled();
        expect(documentoService.addDocumentoToCollectionIfMissing).toHaveBeenCalledWith(documentoCollection, ...additionalDocumentos);
        expect(comp.documentosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const documentoEtiqueta: IDocumentoEtiqueta = { id: 456 };
        const etiqueta: IEtiqueta = { id: 72072 };
        documentoEtiqueta.etiqueta = etiqueta;
        const documento: IDocumento = { id: 50167 };
        documentoEtiqueta.documento = documento;

        activatedRoute.data = of({ documentoEtiqueta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(documentoEtiqueta));
        expect(comp.etiquetasSharedCollection).toContain(etiqueta);
        expect(comp.documentosSharedCollection).toContain(documento);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentoEtiqueta>>();
        const documentoEtiqueta = { id: 123 };
        jest.spyOn(documentoEtiquetaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentoEtiqueta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentoEtiqueta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(documentoEtiquetaService.update).toHaveBeenCalledWith(documentoEtiqueta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentoEtiqueta>>();
        const documentoEtiqueta = new DocumentoEtiqueta();
        jest.spyOn(documentoEtiquetaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentoEtiqueta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentoEtiqueta }));
        saveSubject.complete();

        // THEN
        expect(documentoEtiquetaService.create).toHaveBeenCalledWith(documentoEtiqueta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DocumentoEtiqueta>>();
        const documentoEtiqueta = { id: 123 };
        jest.spyOn(documentoEtiquetaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentoEtiqueta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(documentoEtiquetaService.update).toHaveBeenCalledWith(documentoEtiqueta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEtiquetaById', () => {
        it('Should return tracked Etiqueta primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEtiquetaById(0, entity);
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
