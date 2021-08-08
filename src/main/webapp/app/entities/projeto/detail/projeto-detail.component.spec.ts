import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProjetoDetailComponent } from './projeto-detail.component';

describe('Component Tests', () => {
  describe('Projeto Management Detail Component', () => {
    let comp: ProjetoDetailComponent;
    let fixture: ComponentFixture<ProjetoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProjetoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ projeto: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ProjetoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProjetoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load projeto on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.projeto).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
