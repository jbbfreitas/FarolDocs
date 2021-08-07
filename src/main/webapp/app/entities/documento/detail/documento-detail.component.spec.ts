import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentoDetailComponent } from './documento-detail.component';

describe('Component Tests', () => {
  describe('Documento Management Detail Component', () => {
    let comp: DocumentoDetailComponent;
    let fixture: ComponentFixture<DocumentoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documento: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documento on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documento).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
