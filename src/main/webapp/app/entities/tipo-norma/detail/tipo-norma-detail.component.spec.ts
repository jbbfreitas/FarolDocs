import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoNormaDetailComponent } from './tipo-norma-detail.component';

describe('Component Tests', () => {
  describe('TipoNorma Management Detail Component', () => {
    let comp: TipoNormaDetailComponent;
    let fixture: ComponentFixture<TipoNormaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TipoNormaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tipoNorma: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TipoNormaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TipoNormaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tipoNorma on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tipoNorma).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
