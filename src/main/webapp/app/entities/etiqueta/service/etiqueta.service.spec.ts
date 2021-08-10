import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEtiqueta, Etiqueta } from '../etiqueta.model';

import { EtiquetaService } from './etiqueta.service';

describe('Service Tests', () => {
  describe('Etiqueta Service', () => {
    let service: EtiquetaService;
    let httpMock: HttpTestingController;
    let elemDefault: IEtiqueta;
    let expectedResult: IEtiqueta | IEtiqueta[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EtiquetaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Etiqueta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Etiqueta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Etiqueta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Etiqueta', () => {
        const patchObject = Object.assign(
          {
            nome: 'BBBBBB',
          },
          new Etiqueta()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Etiqueta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Etiqueta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEtiquetaToCollectionIfMissing', () => {
        it('should add a Etiqueta to an empty array', () => {
          const etiqueta: IEtiqueta = { id: 123 };
          expectedResult = service.addEtiquetaToCollectionIfMissing([], etiqueta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(etiqueta);
        });

        it('should not add a Etiqueta to an array that contains it', () => {
          const etiqueta: IEtiqueta = { id: 123 };
          const etiquetaCollection: IEtiqueta[] = [
            {
              ...etiqueta,
            },
            { id: 456 },
          ];
          expectedResult = service.addEtiquetaToCollectionIfMissing(etiquetaCollection, etiqueta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Etiqueta to an array that doesn't contain it", () => {
          const etiqueta: IEtiqueta = { id: 123 };
          const etiquetaCollection: IEtiqueta[] = [{ id: 456 }];
          expectedResult = service.addEtiquetaToCollectionIfMissing(etiquetaCollection, etiqueta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(etiqueta);
        });

        it('should add only unique Etiqueta to an array', () => {
          const etiquetaArray: IEtiqueta[] = [{ id: 123 }, { id: 456 }, { id: 92293 }];
          const etiquetaCollection: IEtiqueta[] = [{ id: 123 }];
          expectedResult = service.addEtiquetaToCollectionIfMissing(etiquetaCollection, ...etiquetaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const etiqueta: IEtiqueta = { id: 123 };
          const etiqueta2: IEtiqueta = { id: 456 };
          expectedResult = service.addEtiquetaToCollectionIfMissing([], etiqueta, etiqueta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(etiqueta);
          expect(expectedResult).toContain(etiqueta2);
        });

        it('should accept null and undefined values', () => {
          const etiqueta: IEtiqueta = { id: 123 };
          expectedResult = service.addEtiquetaToCollectionIfMissing([], null, etiqueta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(etiqueta);
        });

        it('should return initial array if no Etiqueta is added', () => {
          const etiquetaCollection: IEtiqueta[] = [{ id: 123 }];
          expectedResult = service.addEtiquetaToCollectionIfMissing(etiquetaCollection, undefined, null);
          expect(expectedResult).toEqual(etiquetaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
