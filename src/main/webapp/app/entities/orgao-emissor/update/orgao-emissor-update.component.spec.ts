jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrgaoEmissorService } from '../service/orgao-emissor.service';
import { IOrgaoEmissor, OrgaoEmissor } from '../orgao-emissor.model';

import { OrgaoEmissorUpdateComponent } from './orgao-emissor-update.component';

describe('Component Tests', () => {
  describe('OrgaoEmissor Management Update Component', () => {
    let comp: OrgaoEmissorUpdateComponent;
    let fixture: ComponentFixture<OrgaoEmissorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let orgaoEmissorService: OrgaoEmissorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrgaoEmissorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrgaoEmissorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrgaoEmissorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      orgaoEmissorService = TestBed.inject(OrgaoEmissorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const orgaoEmissor: IOrgaoEmissor = { id: 456 };

        activatedRoute.data = of({ orgaoEmissor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(orgaoEmissor));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrgaoEmissor>>();
        const orgaoEmissor = { id: 123 };
        jest.spyOn(orgaoEmissorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orgaoEmissor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orgaoEmissor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(orgaoEmissorService.update).toHaveBeenCalledWith(orgaoEmissor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrgaoEmissor>>();
        const orgaoEmissor = new OrgaoEmissor();
        jest.spyOn(orgaoEmissorService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orgaoEmissor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orgaoEmissor }));
        saveSubject.complete();

        // THEN
        expect(orgaoEmissorService.create).toHaveBeenCalledWith(orgaoEmissor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrgaoEmissor>>();
        const orgaoEmissor = { id: 123 };
        jest.spyOn(orgaoEmissorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orgaoEmissor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(orgaoEmissorService.update).toHaveBeenCalledWith(orgaoEmissor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
