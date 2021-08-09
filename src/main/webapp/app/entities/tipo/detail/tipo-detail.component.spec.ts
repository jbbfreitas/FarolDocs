import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoDetailComponent } from './tipo-detail.component';

describe('Component Tests', () => {
  describe('Tipo Management Detail Component', () => {
    let comp: TipoDetailComponent;
    let fixture: ComponentFixture<TipoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TipoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tipo: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TipoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TipoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tipo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tipo).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
