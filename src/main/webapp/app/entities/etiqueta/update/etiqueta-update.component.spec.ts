jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EtiquetaService } from '../service/etiqueta.service';
import { IEtiqueta, Etiqueta } from '../etiqueta.model';

import { EtiquetaUpdateComponent } from './etiqueta-update.component';

describe('Component Tests', () => {
  describe('Etiqueta Management Update Component', () => {
    let comp: EtiquetaUpdateComponent;
    let fixture: ComponentFixture<EtiquetaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let etiquetaService: EtiquetaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EtiquetaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EtiquetaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EtiquetaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      etiquetaService = TestBed.inject(EtiquetaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const etiqueta: IEtiqueta = { id: 456 };

        activatedRoute.data = of({ etiqueta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(etiqueta));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Etiqueta>>();
        const etiqueta = { id: 123 };
        jest.spyOn(etiquetaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ etiqueta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: etiqueta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(etiquetaService.update).toHaveBeenCalledWith(etiqueta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Etiqueta>>();
        const etiqueta = new Etiqueta();
        jest.spyOn(etiquetaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ etiqueta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: etiqueta }));
        saveSubject.complete();

        // THEN
        expect(etiquetaService.create).toHaveBeenCalledWith(etiqueta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Etiqueta>>();
        const etiqueta = { id: 123 };
        jest.spyOn(etiquetaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ etiqueta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(etiquetaService.update).toHaveBeenCalledWith(etiqueta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
