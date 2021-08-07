import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumento, getDocumentoIdentifier } from '../documento.model';

export type EntityResponseType = HttpResponse<IDocumento>;
export type EntityArrayResponseType = HttpResponse<IDocumento[]>;

@Injectable({ providedIn: 'root' })
export class DocumentoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/documentos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/documentos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documento: IDocumento): Observable<EntityResponseType> {
    return this.http.post<IDocumento>(this.resourceUrl, documento, { observe: 'response' });
  }

  update(documento: IDocumento): Observable<EntityResponseType> {
    return this.http.put<IDocumento>(`${this.resourceUrl}/${getDocumentoIdentifier(documento) as number}`, documento, {
      observe: 'response',
    });
  }

  partialUpdate(documento: IDocumento): Observable<EntityResponseType> {
    return this.http.patch<IDocumento>(`${this.resourceUrl}/${getDocumentoIdentifier(documento) as number}`, documento, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumento[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addDocumentoToCollectionIfMissing(
    documentoCollection: IDocumento[],
    ...documentosToCheck: (IDocumento | null | undefined)[]
  ): IDocumento[] {
    const documentos: IDocumento[] = documentosToCheck.filter(isPresent);
    if (documentos.length > 0) {
      const documentoCollectionIdentifiers = documentoCollection.map(documentoItem => getDocumentoIdentifier(documentoItem)!);
      const documentosToAdd = documentos.filter(documentoItem => {
        const documentoIdentifier = getDocumentoIdentifier(documentoItem);
        if (documentoIdentifier == null || documentoCollectionIdentifiers.includes(documentoIdentifier)) {
          return false;
        }
        documentoCollectionIdentifiers.push(documentoIdentifier);
        return true;
      });
      return [...documentosToAdd, ...documentoCollection];
    }
    return documentoCollection;
  }
}
