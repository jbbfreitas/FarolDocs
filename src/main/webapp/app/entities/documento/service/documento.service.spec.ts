import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDocumento, Documento } from '../documento.model';

import { DocumentoService } from './documento.service';

describe('Service Tests', () => {
  describe('Documento Service', () => {
    let service: DocumentoService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumento;
    let expectedResult: IDocumento | IDocumento[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        assunto: 'AAAAAAA',
        descricao: 'AAAAAAA',
        etiqueta: 'AAAAAAA',
        url: 'AAAAAAA',
        ementa: 'AAAAAAA',
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

      it('should create a Documento', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Documento()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Documento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            assunto: 'BBBBBB',
            descricao: 'BBBBBB',
            etiqueta: 'BBBBBB',
            url: 'BBBBBB',
            ementa: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Documento', () => {
        const patchObject = Object.assign(
          {
            etiqueta: 'BBBBBB',
          },
          new Documento()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Documento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            assunto: 'BBBBBB',
            descricao: 'BBBBBB',
            etiqueta: 'BBBBBB',
            url: 'BBBBBB',
            ementa: 'BBBBBB',
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

      it('should delete a Documento', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentoToCollectionIfMissing', () => {
        it('should add a Documento to an empty array', () => {
          const documento: IDocumento = { id: 123 };
          expectedResult = service.addDocumentoToCollectionIfMissing([], documento);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documento);
        });

        it('should not add a Documento to an array that contains it', () => {
          const documento: IDocumento = { id: 123 };
          const documentoCollection: IDocumento[] = [
            {
              ...documento,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentoToCollectionIfMissing(documentoCollection, documento);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Documento to an array that doesn't contain it", () => {
          const documento: IDocumento = { id: 123 };
          const documentoCollection: IDocumento[] = [{ id: 456 }];
          expectedResult = service.addDocumentoToCollectionIfMissing(documentoCollection, documento);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documento);
        });

        it('should add only unique Documento to an array', () => {
          const documentoArray: IDocumento[] = [{ id: 123 }, { id: 456 }, { id: 5974 }];
          const documentoCollection: IDocumento[] = [{ id: 123 }];
          expectedResult = service.addDocumentoToCollectionIfMissing(documentoCollection, ...documentoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documento: IDocumento = { id: 123 };
          const documento2: IDocumento = { id: 456 };
          expectedResult = service.addDocumentoToCollectionIfMissing([], documento, documento2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documento);
          expect(expectedResult).toContain(documento2);
        });

        it('should accept null and undefined values', () => {
          const documento: IDocumento = { id: 123 };
          expectedResult = service.addDocumentoToCollectionIfMissing([], null, documento, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documento);
        });

        it('should return initial array if no Documento is added', () => {
          const documentoCollection: IDocumento[] = [{ id: 123 }];
          expectedResult = service.addDocumentoToCollectionIfMissing(documentoCollection, undefined, null);
          expect(expectedResult).toEqual(documentoCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
