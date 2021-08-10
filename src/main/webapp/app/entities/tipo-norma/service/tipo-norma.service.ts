import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITipoNorma, getTipoNormaIdentifier } from '../tipo-norma.model';

export type EntityResponseType = HttpResponse<ITipoNorma>;
export type EntityArrayResponseType = HttpResponse<ITipoNorma[]>;

@Injectable({ providedIn: 'root' })
export class TipoNormaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-normas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/tipo-normas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoNorma: ITipoNorma): Observable<EntityResponseType> {
    return this.http.post<ITipoNorma>(this.resourceUrl, tipoNorma, { observe: 'response' });
  }

  update(tipoNorma: ITipoNorma): Observable<EntityResponseType> {
    return this.http.put<ITipoNorma>(`${this.resourceUrl}/${getTipoNormaIdentifier(tipoNorma) as number}`, tipoNorma, {
      observe: 'response',
    });
  }

  partialUpdate(tipoNorma: ITipoNorma): Observable<EntityResponseType> {
    return this.http.patch<ITipoNorma>(`${this.resourceUrl}/${getTipoNormaIdentifier(tipoNorma) as number}`, tipoNorma, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoNorma>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoNorma[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoNorma[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTipoNormaToCollectionIfMissing(
    tipoNormaCollection: ITipoNorma[],
    ...tipoNormasToCheck: (ITipoNorma | null | undefined)[]
  ): ITipoNorma[] {
    const tipoNormas: ITipoNorma[] = tipoNormasToCheck.filter(isPresent);
    if (tipoNormas.length > 0) {
      const tipoNormaCollectionIdentifiers = tipoNormaCollection.map(tipoNormaItem => getTipoNormaIdentifier(tipoNormaItem)!);
      const tipoNormasToAdd = tipoNormas.filter(tipoNormaItem => {
        const tipoNormaIdentifier = getTipoNormaIdentifier(tipoNormaItem);
        if (tipoNormaIdentifier == null || tipoNormaCollectionIdentifiers.includes(tipoNormaIdentifier)) {
          return false;
        }
        tipoNormaCollectionIdentifiers.push(tipoNormaIdentifier);
        return true;
      });
      return [...tipoNormasToAdd, ...tipoNormaCollection];
    }
    return tipoNormaCollection;
  }
}
