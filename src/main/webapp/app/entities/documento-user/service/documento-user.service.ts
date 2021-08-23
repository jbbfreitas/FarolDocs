import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentoUser, getDocumentoUserIdentifier } from '../documento-user.model';

export type EntityResponseType = HttpResponse<IDocumentoUser>;
export type EntityArrayResponseType = HttpResponse<IDocumentoUser[]>;

@Injectable({ providedIn: 'root' })
export class DocumentoUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/documento-users');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/documento-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentoUser: IDocumentoUser): Observable<EntityResponseType> {
    return this.http.post<IDocumentoUser>(this.resourceUrl, documentoUser, { observe: 'response' });
  }

  update(documentoUser: IDocumentoUser): Observable<EntityResponseType> {
    return this.http.put<IDocumentoUser>(`${this.resourceUrl}/${getDocumentoUserIdentifier(documentoUser) as number}`, documentoUser, {
      observe: 'response',
    });
  }

  partialUpdate(documentoUser: IDocumentoUser): Observable<EntityResponseType> {
    return this.http.patch<IDocumentoUser>(`${this.resourceUrl}/${getDocumentoUserIdentifier(documentoUser) as number}`, documentoUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentoUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentoUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentoUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addDocumentoUserToCollectionIfMissing(
    documentoUserCollection: IDocumentoUser[],
    ...documentoUsersToCheck: (IDocumentoUser | null | undefined)[]
  ): IDocumentoUser[] {
    const documentoUsers: IDocumentoUser[] = documentoUsersToCheck.filter(isPresent);
    if (documentoUsers.length > 0) {
      const documentoUserCollectionIdentifiers = documentoUserCollection.map(
        documentoUserItem => getDocumentoUserIdentifier(documentoUserItem)!
      );
      const documentoUsersToAdd = documentoUsers.filter(documentoUserItem => {
        const documentoUserIdentifier = getDocumentoUserIdentifier(documentoUserItem);
        if (documentoUserIdentifier == null || documentoUserCollectionIdentifiers.includes(documentoUserIdentifier)) {
          return false;
        }
        documentoUserCollectionIdentifiers.push(documentoUserIdentifier);
        return true;
      });
      return [...documentoUsersToAdd, ...documentoUserCollection];
    }
    return documentoUserCollection;
  }
}
