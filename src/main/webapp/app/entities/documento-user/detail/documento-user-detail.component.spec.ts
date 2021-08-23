import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentoUserDetailComponent } from './documento-user-detail.component';

describe('Component Tests', () => {
  describe('DocumentoUser Management Detail Component', () => {
    let comp: DocumentoUserDetailComponent;
    let fixture: ComponentFixture<DocumentoUserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentoUserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentoUser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentoUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentoUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documentoUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentoUser).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
