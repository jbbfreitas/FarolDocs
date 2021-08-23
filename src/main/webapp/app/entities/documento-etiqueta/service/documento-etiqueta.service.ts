import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentoEtiqueta, getDocumentoEtiquetaIdentifier } from '../documento-etiqueta.model';

export type EntityResponseType = HttpResponse<IDocumentoEtiqueta>;
export type EntityArrayResponseType = HttpResponse<IDocumentoEtiqueta[]>;

@Injectable({ providedIn: 'root' })
export class DocumentoEtiquetaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/documento-etiquetas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/documento-etiquetas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentoEtiqueta: IDocumentoEtiqueta): Observable<EntityResponseType> {
    return this.http.post<IDocumentoEtiqueta>(this.resourceUrl, documentoEtiqueta, { observe: 'response' });
  }

  update(documentoEtiqueta: IDocumentoEtiqueta): Observable<EntityResponseType> {
    return this.http.put<IDocumentoEtiqueta>(
      `${this.resourceUrl}/${getDocumentoEtiquetaIdentifier(documentoEtiqueta) as number}`,
      documentoEtiqueta,
      { observe: 'response' }
    );
  }

  partialUpdate(documentoEtiqueta: IDocumentoEtiqueta): Observable<EntityResponseType> {
    return this.http.patch<IDocumentoEtiqueta>(
      `${this.resourceUrl}/${getDocumentoEtiquetaIdentifier(documentoEtiqueta) as number}`,
      documentoEtiqueta,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentoEtiqueta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentoEtiqueta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentoEtiqueta[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addDocumentoEtiquetaToCollectionIfMissing(
    documentoEtiquetaCollection: IDocumentoEtiqueta[],
    ...documentoEtiquetasToCheck: (IDocumentoEtiqueta | null | undefined)[]
  ): IDocumentoEtiqueta[] {
    const documentoEtiquetas: IDocumentoEtiqueta[] = documentoEtiquetasToCheck.filter(isPresent);
    if (documentoEtiquetas.length > 0) {
      const documentoEtiquetaCollectionIdentifiers = documentoEtiquetaCollection.map(
        documentoEtiquetaItem => getDocumentoEtiquetaIdentifier(documentoEtiquetaItem)!
      );
      const documentoEtiquetasToAdd = documentoEtiquetas.filter(documentoEtiquetaItem => {
        const documentoEtiquetaIdentifier = getDocumentoEtiquetaIdentifier(documentoEtiquetaItem);
        if (documentoEtiquetaIdentifier == null || documentoEtiquetaCollectionIdentifiers.includes(documentoEtiquetaIdentifier)) {
          return false;
        }
        documentoEtiquetaCollectionIdentifiers.push(documentoEtiquetaIdentifier);
        return true;
      });
      return [...documentoEtiquetasToAdd, ...documentoEtiquetaCollection];
    }
    return documentoEtiquetaCollection;
  }
}
