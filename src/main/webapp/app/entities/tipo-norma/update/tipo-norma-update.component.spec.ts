jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TipoNormaService } from '../service/tipo-norma.service';
import { ITipoNorma, TipoNorma } from '../tipo-norma.model';

import { TipoNormaUpdateComponent } from './tipo-norma-update.component';

describe('Component Tests', () => {
  describe('TipoNorma Management Update Component', () => {
    let comp: TipoNormaUpdateComponent;
    let fixture: ComponentFixture<TipoNormaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tipoNormaService: TipoNormaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TipoNormaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TipoNormaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TipoNormaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tipoNormaService = TestBed.inject(TipoNormaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tipoNorma: ITipoNorma = { id: 456 };

        activatedRoute.data = of({ tipoNorma });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tipoNorma));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TipoNorma>>();
        const tipoNorma = { id: 123 };
        jest.spyOn(tipoNormaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tipoNorma });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tipoNorma }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tipoNormaService.update).toHaveBeenCalledWith(tipoNorma);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TipoNorma>>();
        const tipoNorma = new TipoNorma();
        jest.spyOn(tipoNormaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tipoNorma });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tipoNorma }));
        saveSubject.complete();

        // THEN
        expect(tipoNormaService.create).toHaveBeenCalledWith(tipoNorma);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TipoNorma>>();
        const tipoNorma = { id: 123 };
        jest.spyOn(tipoNormaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tipoNorma });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tipoNormaService.update).toHaveBeenCalledWith(tipoNorma);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
