import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IOrgaoEmissor, getOrgaoEmissorIdentifier } from '../orgao-emissor.model';

export type EntityResponseType = HttpResponse<IOrgaoEmissor>;
export type EntityArrayResponseType = HttpResponse<IOrgaoEmissor[]>;

@Injectable({ providedIn: 'root' })
export class OrgaoEmissorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/orgao-emissors');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/orgao-emissors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orgaoEmissor: IOrgaoEmissor): Observable<EntityResponseType> {
    return this.http.post<IOrgaoEmissor>(this.resourceUrl, orgaoEmissor, { observe: 'response' });
  }

  update(orgaoEmissor: IOrgaoEmissor): Observable<EntityResponseType> {
    return this.http.put<IOrgaoEmissor>(`${this.resourceUrl}/${getOrgaoEmissorIdentifier(orgaoEmissor) as number}`, orgaoEmissor, {
      observe: 'response',
    });
  }

  partialUpdate(orgaoEmissor: IOrgaoEmissor): Observable<EntityResponseType> {
    return this.http.patch<IOrgaoEmissor>(`${this.resourceUrl}/${getOrgaoEmissorIdentifier(orgaoEmissor) as number}`, orgaoEmissor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrgaoEmissor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrgaoEmissor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrgaoEmissor[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addOrgaoEmissorToCollectionIfMissing(
    orgaoEmissorCollection: IOrgaoEmissor[],
    ...orgaoEmissorsToCheck: (IOrgaoEmissor | null | undefined)[]
  ): IOrgaoEmissor[] {
    const orgaoEmissors: IOrgaoEmissor[] = orgaoEmissorsToCheck.filter(isPresent);
    if (orgaoEmissors.length > 0) {
      const orgaoEmissorCollectionIdentifiers = orgaoEmissorCollection.map(
        orgaoEmissorItem => getOrgaoEmissorIdentifier(orgaoEmissorItem)!
      );
      const orgaoEmissorsToAdd = orgaoEmissors.filter(orgaoEmissorItem => {
        const orgaoEmissorIdentifier = getOrgaoEmissorIdentifier(orgaoEmissorItem);
        if (orgaoEmissorIdentifier == null || orgaoEmissorCollectionIdentifiers.includes(orgaoEmissorIdentifier)) {
          return false;
        }
        orgaoEmissorCollectionIdentifiers.push(orgaoEmissorIdentifier);
        return true;
      });
      return [...orgaoEmissorsToAdd, ...orgaoEmissorCollection];
    }
    return orgaoEmissorCollection;
  }
}
