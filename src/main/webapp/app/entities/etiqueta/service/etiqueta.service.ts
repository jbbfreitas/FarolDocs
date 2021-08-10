import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEtiqueta, getEtiquetaIdentifier } from '../etiqueta.model';

export type EntityResponseType = HttpResponse<IEtiqueta>;
export type EntityArrayResponseType = HttpResponse<IEtiqueta[]>;

@Injectable({ providedIn: 'root' })
export class EtiquetaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/etiquetas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/etiquetas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(etiqueta: IEtiqueta): Observable<EntityResponseType> {
    return this.http.post<IEtiqueta>(this.resourceUrl, etiqueta, { observe: 'response' });
  }

  update(etiqueta: IEtiqueta): Observable<EntityResponseType> {
    return this.http.put<IEtiqueta>(`${this.resourceUrl}/${getEtiquetaIdentifier(etiqueta) as number}`, etiqueta, { observe: 'response' });
  }

  partialUpdate(etiqueta: IEtiqueta): Observable<EntityResponseType> {
    return this.http.patch<IEtiqueta>(`${this.resourceUrl}/${getEtiquetaIdentifier(etiqueta) as number}`, etiqueta, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEtiqueta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtiqueta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtiqueta[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addEtiquetaToCollectionIfMissing(etiquetaCollection: IEtiqueta[], ...etiquetasToCheck: (IEtiqueta | null | undefined)[]): IEtiqueta[] {
    const etiquetas: IEtiqueta[] = etiquetasToCheck.filter(isPresent);
    if (etiquetas.length > 0) {
      const etiquetaCollectionIdentifiers = etiquetaCollection.map(etiquetaItem => getEtiquetaIdentifier(etiquetaItem)!);
      const etiquetasToAdd = etiquetas.filter(etiquetaItem => {
        const etiquetaIdentifier = getEtiquetaIdentifier(etiquetaItem);
        if (etiquetaIdentifier == null || etiquetaCollectionIdentifiers.includes(etiquetaIdentifier)) {
          return false;
        }
        etiquetaCollectionIdentifiers.push(etiquetaIdentifier);
        return true;
      });
      return [...etiquetasToAdd, ...etiquetaCollection];
    }
    return etiquetaCollection;
  }
}
