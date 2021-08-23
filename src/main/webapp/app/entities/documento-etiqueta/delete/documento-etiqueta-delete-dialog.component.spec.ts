jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DocumentoEtiquetaService } from '../service/documento-etiqueta.service';

import { DocumentoEtiquetaDeleteDialogComponent } from './documento-etiqueta-delete-dialog.component';

describe('Component Tests', () => {
  describe('DocumentoEtiqueta Management Delete Component', () => {
    let comp: DocumentoEtiquetaDeleteDialogComponent;
    let fixture: ComponentFixture<DocumentoEtiquetaDeleteDialogComponent>;
    let service: DocumentoEtiquetaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentoEtiquetaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(DocumentoEtiquetaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentoEtiquetaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentoEtiquetaService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
