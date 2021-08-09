import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipo, Tipo } from '../tipo.model';

import { TipoService } from './tipo.service';

describe('Service Tests', () => {
  describe('Tipo Service', () => {
    let service: TipoService;
    let httpMock: HttpTestingController;
    let elemDefault: ITipo;
    let expectedResult: ITipo | ITipo[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TipoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
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

      it('should create a Tipo', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Tipo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Tipo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
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

      it('should partial update a Tipo', () => {
        const patchObject = Object.assign({}, new Tipo());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Tipo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
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

      it('should delete a Tipo', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTipoToCollectionIfMissing', () => {
        it('should add a Tipo to an empty array', () => {
          const tipo: ITipo = { id: 123 };
          expectedResult = service.addTipoToCollectionIfMissing([], tipo);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tipo);
        });

        it('should not add a Tipo to an array that contains it', () => {
          const tipo: ITipo = { id: 123 };
          const tipoCollection: ITipo[] = [
            {
              ...tipo,
            },
            { id: 456 },
          ];
          expectedResult = service.addTipoToCollectionIfMissing(tipoCollection, tipo);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Tipo to an array that doesn't contain it", () => {
          const tipo: ITipo = { id: 123 };
          const tipoCollection: ITipo[] = [{ id: 456 }];
          expectedResult = service.addTipoToCollectionIfMissing(tipoCollection, tipo);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tipo);
        });

        it('should add only unique Tipo to an array', () => {
          const tipoArray: ITipo[] = [{ id: 123 }, { id: 456 }, { id: 49776 }];
          const tipoCollection: ITipo[] = [{ id: 123 }];
          expectedResult = service.addTipoToCollectionIfMissing(tipoCollection, ...tipoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tipo: ITipo = { id: 123 };
          const tipo2: ITipo = { id: 456 };
          expectedResult = service.addTipoToCollectionIfMissing([], tipo, tipo2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tipo);
          expect(expectedResult).toContain(tipo2);
        });

        it('should accept null and undefined values', () => {
          const tipo: ITipo = { id: 123 };
          expectedResult = service.addTipoToCollectionIfMissing([], null, tipo, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tipo);
        });

        it('should return initial array if no Tipo is added', () => {
          const tipoCollection: ITipo[] = [{ id: 123 }];
          expectedResult = service.addTipoToCollectionIfMissing(tipoCollection, undefined, null);
          expect(expectedResult).toEqual(tipoCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
