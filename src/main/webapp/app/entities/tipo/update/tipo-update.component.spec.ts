jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TipoService } from '../service/tipo.service';
import { ITipo, Tipo } from '../tipo.model';

import { TipoUpdateComponent } from './tipo-update.component';

describe('Component Tests', () => {
  describe('Tipo Management Update Component', () => {
    let comp: TipoUpdateComponent;
    let fixture: ComponentFixture<TipoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tipoService: TipoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TipoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TipoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TipoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tipoService = TestBed.inject(TipoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tipo: ITipo = { id: 456 };

        activatedRoute.data = of({ tipo });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tipo));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tipo>>();
        const tipo = { id: 123 };
        jest.spyOn(tipoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tipo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tipo }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tipoService.update).toHaveBeenCalledWith(tipo);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tipo>>();
        const tipo = new Tipo();
        jest.spyOn(tipoService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tipo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tipo }));
        saveSubject.complete();

        // THEN
        expect(tipoService.create).toHaveBeenCalledWith(tipo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tipo>>();
        const tipo = { id: 123 };
        jest.spyOn(tipoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tipo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tipoService.update).toHaveBeenCalledWith(tipo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
