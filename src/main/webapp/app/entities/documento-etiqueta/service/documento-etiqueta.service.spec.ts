import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDocumentoEtiqueta, DocumentoEtiqueta } from '../documento-etiqueta.model';

import { DocumentoEtiquetaService } from './documento-etiqueta.service';

describe('Service Tests', () => {
  describe('DocumentoEtiqueta Service', () => {
    let service: DocumentoEtiquetaService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentoEtiqueta;
    let expectedResult: IDocumentoEtiqueta | IDocumentoEtiqueta[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentoEtiquetaService);
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

      it('should create a DocumentoEtiqueta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DocumentoEtiqueta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentoEtiqueta', () => {
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

      it('should partial update a DocumentoEtiqueta', () => {
        const patchObject = Object.assign({}, new DocumentoEtiqueta());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocumentoEtiqueta', () => {
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

      it('should delete a DocumentoEtiqueta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentoEtiquetaToCollectionIfMissing', () => {
        it('should add a DocumentoEtiqueta to an empty array', () => {
          const documentoEtiqueta: IDocumentoEtiqueta = { id: 123 };
          expectedResult = service.addDocumentoEtiquetaToCollectionIfMissing([], documentoEtiqueta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentoEtiqueta);
        });

        it('should not add a DocumentoEtiqueta to an array that contains it', () => {
          const documentoEtiqueta: IDocumentoEtiqueta = { id: 123 };
          const documentoEtiquetaCollection: IDocumentoEtiqueta[] = [
            {
              ...documentoEtiqueta,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentoEtiquetaToCollectionIfMissing(documentoEtiquetaCollection, documentoEtiqueta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentoEtiqueta to an array that doesn't contain it", () => {
          const documentoEtiqueta: IDocumentoEtiqueta = { id: 123 };
          const documentoEtiquetaCollection: IDocumentoEtiqueta[] = [{ id: 456 }];
          expectedResult = service.addDocumentoEtiquetaToCollectionIfMissing(documentoEtiquetaCollection, documentoEtiqueta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentoEtiqueta);
        });

        it('should add only unique DocumentoEtiqueta to an array', () => {
          const documentoEtiquetaArray: IDocumentoEtiqueta[] = [{ id: 123 }, { id: 456 }, { id: 47528 }];
          const documentoEtiquetaCollection: IDocumentoEtiqueta[] = [{ id: 123 }];
          expectedResult = service.addDocumentoEtiquetaToCollectionIfMissing(documentoEtiquetaCollection, ...documentoEtiquetaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentoEtiqueta: IDocumentoEtiqueta = { id: 123 };
          const documentoEtiqueta2: IDocumentoEtiqueta = { id: 456 };
          expectedResult = service.addDocumentoEtiquetaToCollectionIfMissing([], documentoEtiqueta, documentoEtiqueta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentoEtiqueta);
          expect(expectedResult).toContain(documentoEtiqueta2);
        });

        it('should accept null and undefined values', () => {
          const documentoEtiqueta: IDocumentoEtiqueta = { id: 123 };
          expectedResult = service.addDocumentoEtiquetaToCollectionIfMissing([], null, documentoEtiqueta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentoEtiqueta);
        });

        it('should return initial array if no DocumentoEtiqueta is added', () => {
          const documentoEtiquetaCollection: IDocumentoEtiqueta[] = [{ id: 123 }];
          expectedResult = service.addDocumentoEtiquetaToCollectionIfMissing(documentoEtiquetaCollection, undefined, null);
          expect(expectedResult).toEqual(documentoEtiquetaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
