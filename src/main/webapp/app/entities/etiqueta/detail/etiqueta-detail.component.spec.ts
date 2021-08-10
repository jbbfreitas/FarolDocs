import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EtiquetaDetailComponent } from './etiqueta-detail.component';

describe('Component Tests', () => {
  describe('Etiqueta Management Detail Component', () => {
    let comp: EtiquetaDetailComponent;
    let fixture: ComponentFixture<EtiquetaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EtiquetaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ etiqueta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EtiquetaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EtiquetaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load etiqueta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.etiqueta).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
