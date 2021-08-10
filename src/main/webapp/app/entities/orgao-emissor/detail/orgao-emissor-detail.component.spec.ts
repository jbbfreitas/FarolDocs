import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrgaoEmissorDetailComponent } from './orgao-emissor-detail.component';

describe('Component Tests', () => {
  describe('OrgaoEmissor Management Detail Component', () => {
    let comp: OrgaoEmissorDetailComponent;
    let fixture: ComponentFixture<OrgaoEmissorDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OrgaoEmissorDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ orgaoEmissor: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OrgaoEmissorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrgaoEmissorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load orgaoEmissor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.orgaoEmissor).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
