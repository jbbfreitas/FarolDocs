jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProjetoService } from '../service/projeto.service';
import { IProjeto, Projeto } from '../projeto.model';

import { ProjetoUpdateComponent } from './projeto-update.component';

describe('Component Tests', () => {
  describe('Projeto Management Update Component', () => {
    let comp: ProjetoUpdateComponent;
    let fixture: ComponentFixture<ProjetoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let projetoService: ProjetoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProjetoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProjetoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProjetoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      projetoService = TestBed.inject(ProjetoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const projeto: IProjeto = { id: 456 };

        activatedRoute.data = of({ projeto });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(projeto));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Projeto>>();
        const projeto = { id: 123 };
        jest.spyOn(projetoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ projeto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: projeto }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(projetoService.update).toHaveBeenCalledWith(projeto);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Projeto>>();
        const projeto = new Projeto();
        jest.spyOn(projetoService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ projeto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: projeto }));
        saveSubject.complete();

        // THEN
        expect(projetoService.create).toHaveBeenCalledWith(projeto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Projeto>>();
        const projeto = { id: 123 };
        jest.spyOn(projetoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ projeto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(projetoService.update).toHaveBeenCalledWith(projeto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
