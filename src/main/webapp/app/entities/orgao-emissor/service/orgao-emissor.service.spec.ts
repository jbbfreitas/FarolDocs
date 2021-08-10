import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrgaoEmissor, OrgaoEmissor } from '../orgao-emissor.model';

import { OrgaoEmissorService } from './orgao-emissor.service';

describe('Service Tests', () => {
  describe('OrgaoEmissor Service', () => {
    let service: OrgaoEmissorService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrgaoEmissor;
    let expectedResult: IOrgaoEmissor | IOrgaoEmissor[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrgaoEmissorService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        sigla: 'AAAAAAA',
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

      it('should create a OrgaoEmissor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OrgaoEmissor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrgaoEmissor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sigla: 'BBBBBB',
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

      it('should partial update a OrgaoEmissor', () => {
        const patchObject = Object.assign({}, new OrgaoEmissor());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OrgaoEmissor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sigla: 'BBBBBB',
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

      it('should delete a OrgaoEmissor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrgaoEmissorToCollectionIfMissing', () => {
        it('should add a OrgaoEmissor to an empty array', () => {
          const orgaoEmissor: IOrgaoEmissor = { id: 123 };
          expectedResult = service.addOrgaoEmissorToCollectionIfMissing([], orgaoEmissor);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(orgaoEmissor);
        });

        it('should not add a OrgaoEmissor to an array that contains it', () => {
          const orgaoEmissor: IOrgaoEmissor = { id: 123 };
          const orgaoEmissorCollection: IOrgaoEmissor[] = [
            {
              ...orgaoEmissor,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrgaoEmissorToCollectionIfMissing(orgaoEmissorCollection, orgaoEmissor);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrgaoEmissor to an array that doesn't contain it", () => {
          const orgaoEmissor: IOrgaoEmissor = { id: 123 };
          const orgaoEmissorCollection: IOrgaoEmissor[] = [{ id: 456 }];
          expectedResult = service.addOrgaoEmissorToCollectionIfMissing(orgaoEmissorCollection, orgaoEmissor);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(orgaoEmissor);
        });

        it('should add only unique OrgaoEmissor to an array', () => {
          const orgaoEmissorArray: IOrgaoEmissor[] = [{ id: 123 }, { id: 456 }, { id: 32148 }];
          const orgaoEmissorCollection: IOrgaoEmissor[] = [{ id: 123 }];
          expectedResult = service.addOrgaoEmissorToCollectionIfMissing(orgaoEmissorCollection, ...orgaoEmissorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const orgaoEmissor: IOrgaoEmissor = { id: 123 };
          const orgaoEmissor2: IOrgaoEmissor = { id: 456 };
          expectedResult = service.addOrgaoEmissorToCollectionIfMissing([], orgaoEmissor, orgaoEmissor2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(orgaoEmissor);
          expect(expectedResult).toContain(orgaoEmissor2);
        });

        it('should accept null and undefined values', () => {
          const orgaoEmissor: IOrgaoEmissor = { id: 123 };
          expectedResult = service.addOrgaoEmissorToCollectionIfMissing([], null, orgaoEmissor, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(orgaoEmissor);
        });

        it('should return initial array if no OrgaoEmissor is added', () => {
          const orgaoEmissorCollection: IOrgaoEmissor[] = [{ id: 123 }];
          expectedResult = service.addOrgaoEmissorToCollectionIfMissing(orgaoEmissorCollection, undefined, null);
          expect(expectedResult).toEqual(orgaoEmissorCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
