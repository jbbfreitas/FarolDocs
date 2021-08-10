import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoNorma, TipoNorma } from '../tipo-norma.model';

import { TipoNormaService } from './tipo-norma.service';

describe('Service Tests', () => {
  describe('TipoNorma Service', () => {
    let service: TipoNormaService;
    let httpMock: HttpTestingController;
    let elemDefault: ITipoNorma;
    let expectedResult: ITipoNorma | ITipoNorma[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TipoNormaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        tipo: 'AAAAAAA',
        descricao: 'AAAAAAA',
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

      it('should create a TipoNorma', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TipoNorma()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TipoNorma', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tipo: 'BBBBBB',
            descricao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TipoNorma', () => {
        const patchObject = Object.assign(
          {
            tipo: 'BBBBBB',
          },
          new TipoNorma()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TipoNorma', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tipo: 'BBBBBB',
            descricao: 'BBBBBB',
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

      it('should delete a TipoNorma', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTipoNormaToCollectionIfMissing', () => {
        it('should add a TipoNorma to an empty array', () => {
          const tipoNorma: ITipoNorma = { id: 123 };
          expectedResult = service.addTipoNormaToCollectionIfMissing([], tipoNorma);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tipoNorma);
        });

        it('should not add a TipoNorma to an array that contains it', () => {
          const tipoNorma: ITipoNorma = { id: 123 };
          const tipoNormaCollection: ITipoNorma[] = [
            {
              ...tipoNorma,
            },
            { id: 456 },
          ];
          expectedResult = service.addTipoNormaToCollectionIfMissing(tipoNormaCollection, tipoNorma);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TipoNorma to an array that doesn't contain it", () => {
          const tipoNorma: ITipoNorma = { id: 123 };
          const tipoNormaCollection: ITipoNorma[] = [{ id: 456 }];
          expectedResult = service.addTipoNormaToCollectionIfMissing(tipoNormaCollection, tipoNorma);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tipoNorma);
        });

        it('should add only unique TipoNorma to an array', () => {
          const tipoNormaArray: ITipoNorma[] = [{ id: 123 }, { id: 456 }, { id: 64176 }];
          const tipoNormaCollection: ITipoNorma[] = [{ id: 123 }];
          expectedResult = service.addTipoNormaToCollectionIfMissing(tipoNormaCollection, ...tipoNormaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tipoNorma: ITipoNorma = { id: 123 };
          const tipoNorma2: ITipoNorma = { id: 456 };
          expectedResult = service.addTipoNormaToCollectionIfMissing([], tipoNorma, tipoNorma2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tipoNorma);
          expect(expectedResult).toContain(tipoNorma2);
        });

        it('should accept null and undefined values', () => {
          const tipoNorma: ITipoNorma = { id: 123 };
          expectedResult = service.addTipoNormaToCollectionIfMissing([], null, tipoNorma, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tipoNorma);
        });

        it('should return initial array if no TipoNorma is added', () => {
          const tipoNormaCollection: ITipoNorma[] = [{ id: 123 }];
          expectedResult = service.addTipoNormaToCollectionIfMissing(tipoNormaCollection, undefined, null);
          expect(expectedResult).toEqual(tipoNormaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
