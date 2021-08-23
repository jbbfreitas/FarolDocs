import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDocumentoUser, DocumentoUser } from '../documento-user.model';

import { DocumentoUserService } from './documento-user.service';

describe('Service Tests', () => {
  describe('DocumentoUser Service', () => {
    let service: DocumentoUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentoUser;
    let expectedResult: IDocumentoUser | IDocumentoUser[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentoUserService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
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

      it('should create a DocumentoUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DocumentoUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentoUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DocumentoUser', () => {
        const patchObject = Object.assign({}, new DocumentoUser());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocumentoUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

      it('should delete a DocumentoUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentoUserToCollectionIfMissing', () => {
        it('should add a DocumentoUser to an empty array', () => {
          const documentoUser: IDocumentoUser = { id: 123 };
          expectedResult = service.addDocumentoUserToCollectionIfMissing([], documentoUser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentoUser);
        });

        it('should not add a DocumentoUser to an array that contains it', () => {
          const documentoUser: IDocumentoUser = { id: 123 };
          const documentoUserCollection: IDocumentoUser[] = [
            {
              ...documentoUser,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentoUserToCollectionIfMissing(documentoUserCollection, documentoUser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentoUser to an array that doesn't contain it", () => {
          const documentoUser: IDocumentoUser = { id: 123 };
          const documentoUserCollection: IDocumentoUser[] = [{ id: 456 }];
          expectedResult = service.addDocumentoUserToCollectionIfMissing(documentoUserCollection, documentoUser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentoUser);
        });

        it('should add only unique DocumentoUser to an array', () => {
          const documentoUserArray: IDocumentoUser[] = [{ id: 123 }, { id: 456 }, { id: 66354 }];
          const documentoUserCollection: IDocumentoUser[] = [{ id: 123 }];
          expectedResult = service.addDocumentoUserToCollectionIfMissing(documentoUserCollection, ...documentoUserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentoUser: IDocumentoUser = { id: 123 };
          const documentoUser2: IDocumentoUser = { id: 456 };
          expectedResult = service.addDocumentoUserToCollectionIfMissing([], documentoUser, documentoUser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentoUser);
          expect(expectedResult).toContain(documentoUser2);
        });

        it('should accept null and undefined values', () => {
          const documentoUser: IDocumentoUser = { id: 123 };
          expectedResult = service.addDocumentoUserToCollectionIfMissing([], null, documentoUser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentoUser);
        });

        it('should return initial array if no DocumentoUser is added', () => {
          const documentoUserCollection: IDocumentoUser[] = [{ id: 123 }];
          expectedResult = service.addDocumentoUserToCollectionIfMissing(documentoUserCollection, undefined, null);
          expect(expectedResult).toEqual(documentoUserCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
