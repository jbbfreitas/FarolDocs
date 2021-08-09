import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITipo, getTipoIdentifier } from '../tipo.model';

export type EntityResponseType = HttpResponse<ITipo>;
export type EntityArrayResponseType = HttpResponse<ITipo[]>;

@Injectable({ providedIn: 'root' })
export class TipoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/tipos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipo: ITipo): Observable<EntityResponseType> {
    return this.http.post<ITipo>(this.resourceUrl, tipo, { observe: 'response' });
  }

  update(tipo: ITipo): Observable<EntityResponseType> {
    return this.http.put<ITipo>(`${this.resourceUrl}/${getTipoIdentifier(tipo) as number}`, tipo, { observe: 'response' });
  }

  partialUpdate(tipo: ITipo): Observable<EntityResponseType> {
    return this.http.patch<ITipo>(`${this.resourceUrl}/${getTipoIdentifier(tipo) as number}`, tipo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipo[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTipoToCollectionIfMissing(tipoCollection: ITipo[], ...tiposToCheck: (ITipo | null | undefined)[]): ITipo[] {
    const tipos: ITipo[] = tiposToCheck.filter(isPresent);
    if (tipos.length > 0) {
      const tipoCollectionIdentifiers = tipoCollection.map(tipoItem => getTipoIdentifier(tipoItem)!);
      const tiposToAdd = tipos.filter(tipoItem => {
        const tipoIdentifier = getTipoIdentifier(tipoItem);
        if (tipoIdentifier == null || tipoCollectionIdentifiers.includes(tipoIdentifier)) {
          return false;
        }
        tipoCollectionIdentifiers.push(tipoIdentifier);
        return true;
      });
      return [...tiposToAdd, ...tipoCollection];
    }
    return tipoCollection;
  }
}
