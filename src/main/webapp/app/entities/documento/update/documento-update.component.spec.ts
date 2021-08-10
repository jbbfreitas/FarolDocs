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
import { ITipo } from 'app/entities/tipo/tipo.model';
import { TipoService } from 'app/entities/tipo/service/tipo.service';
import { IEtiqueta } from 'app/entities/etiqueta/etiqueta.model';
import { EtiquetaService } from 'app/entities/etiqueta/service/etiqueta.service';
import { IOrgaoEmissor } from 'app/entities/orgao-emissor/orgao-emissor.model';
import { OrgaoEmissorService } from 'app/entities/orgao-emissor/service/orgao-emissor.service';
import { ITipoNorma } from 'app/entities/tipo-norma/tipo-norma.model';
import { TipoNormaService } from 'app/entities/tipo-norma/service/tipo-norma.service';

import { DocumentoUpdateComponent } from './documento-update.component';

describe('Component Tests', () => {
  describe('Documento Management Update Component', () => {
    let comp: DocumentoUpdateComponent;
    let fixture: ComponentFixture<DocumentoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentoService: DocumentoService;
    let projetoService: ProjetoService;
    let tipoService: TipoService;
    let etiquetaService: EtiquetaService;
    let orgaoEmissorService: OrgaoEmissorService;
    let tipoNormaService: TipoNormaService;

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
      tipoService = TestBed.inject(TipoService);
      etiquetaService = TestBed.inject(EtiquetaService);
      orgaoEmissorService = TestBed.inject(OrgaoEmissorService);
      tipoNormaService = TestBed.inject(TipoNormaService);

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

      it('Should call Tipo query and add missing value', () => {
        const documento: IDocumento = { id: 456 };
        const tipo: ITipo = { id: 4836 };
        documento.tipo = tipo;

        const tipoCollection: ITipo[] = [{ id: 49713 }];
        jest.spyOn(tipoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoCollection })));
        const additionalTipos = [tipo];
        const expectedCollection: ITipo[] = [...additionalTipos, ...tipoCollection];
        jest.spyOn(tipoService, 'addTipoToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        expect(tipoService.query).toHaveBeenCalled();
        expect(tipoService.addTipoToCollectionIfMissing).toHaveBeenCalledWith(tipoCollection, ...additionalTipos);
        expect(comp.tiposSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Etiqueta query and add missing value', () => {
        const documento: IDocumento = { id: 456 };
        const etiqueta: IEtiqueta = { id: 11604 };
        documento.etiqueta = etiqueta;

        const etiquetaCollection: IEtiqueta[] = [{ id: 87088 }];
        jest.spyOn(etiquetaService, 'query').mockReturnValue(of(new HttpResponse({ body: etiquetaCollection })));
        const additionalEtiquetas = [etiqueta];
        const expectedCollection: IEtiqueta[] = [...additionalEtiquetas, ...etiquetaCollection];
        jest.spyOn(etiquetaService, 'addEtiquetaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        expect(etiquetaService.query).toHaveBeenCalled();
        expect(etiquetaService.addEtiquetaToCollectionIfMissing).toHaveBeenCalledWith(etiquetaCollection, ...additionalEtiquetas);
        expect(comp.etiquetasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrgaoEmissor query and add missing value', () => {
        const documento: IDocumento = { id: 456 };
        const orgaoEmissor: IOrgaoEmissor = { id: 46848 };
        documento.orgaoEmissor = orgaoEmissor;

        const orgaoEmissorCollection: IOrgaoEmissor[] = [{ id: 52925 }];
        jest.spyOn(orgaoEmissorService, 'query').mockReturnValue(of(new HttpResponse({ body: orgaoEmissorCollection })));
        const additionalOrgaoEmissors = [orgaoEmissor];
        const expectedCollection: IOrgaoEmissor[] = [...additionalOrgaoEmissors, ...orgaoEmissorCollection];
        jest.spyOn(orgaoEmissorService, 'addOrgaoEmissorToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        expect(orgaoEmissorService.query).toHaveBeenCalled();
        expect(orgaoEmissorService.addOrgaoEmissorToCollectionIfMissing).toHaveBeenCalledWith(
          orgaoEmissorCollection,
          ...additionalOrgaoEmissors
        );
        expect(comp.orgaoEmissorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call TipoNorma query and add missing value', () => {
        const documento: IDocumento = { id: 456 };
        const tipoNorma: ITipoNorma = { id: 77139 };
        documento.tipoNorma = tipoNorma;

        const tipoNormaCollection: ITipoNorma[] = [{ id: 47529 }];
        jest.spyOn(tipoNormaService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoNormaCollection })));
        const additionalTipoNormas = [tipoNorma];
        const expectedCollection: ITipoNorma[] = [...additionalTipoNormas, ...tipoNormaCollection];
        jest.spyOn(tipoNormaService, 'addTipoNormaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        expect(tipoNormaService.query).toHaveBeenCalled();
        expect(tipoNormaService.addTipoNormaToCollectionIfMissing).toHaveBeenCalledWith(tipoNormaCollection, ...additionalTipoNormas);
        expect(comp.tipoNormasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const documento: IDocumento = { id: 456 };
        const projeto: IProjeto = { id: 29505 };
        documento.projeto = projeto;
        const tipo: ITipo = { id: 99704 };
        documento.tipo = tipo;
        const etiqueta: IEtiqueta = { id: 13102 };
        documento.etiqueta = etiqueta;
        const orgaoEmissor: IOrgaoEmissor = { id: 55985 };
        documento.orgaoEmissor = orgaoEmissor;
        const tipoNorma: ITipoNorma = { id: 92102 };
        documento.tipoNorma = tipoNorma;

        activatedRoute.data = of({ documento });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(documento));
        expect(comp.projetosSharedCollection).toContain(projeto);
        expect(comp.tiposSharedCollection).toContain(tipo);
        expect(comp.etiquetasSharedCollection).toContain(etiqueta);
        expect(comp.orgaoEmissorsSharedCollection).toContain(orgaoEmissor);
        expect(comp.tipoNormasSharedCollection).toContain(tipoNorma);
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

      describe('trackTipoById', () => {
        it('Should return tracked Tipo primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTipoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEtiquetaById', () => {
        it('Should return tracked Etiqueta primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEtiquetaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackOrgaoEmissorById', () => {
        it('Should return tracked OrgaoEmissor primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrgaoEmissorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTipoNormaById', () => {
        it('Should return tracked TipoNorma primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTipoNormaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
