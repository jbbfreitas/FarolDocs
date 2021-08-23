import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentoEtiquetaDetailComponent } from './documento-etiqueta-detail.component';

describe('Component Tests', () => {
  describe('DocumentoEtiqueta Management Detail Component', () => {
    let comp: DocumentoEtiquetaDetailComponent;
    let fixture: ComponentFixture<DocumentoEtiquetaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentoEtiquetaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentoEtiqueta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentoEtiquetaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentoEtiquetaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documentoEtiqueta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentoEtiqueta).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
