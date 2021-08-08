import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IProjeto, getProjetoIdentifier } from '../projeto.model';

export type EntityResponseType = HttpResponse<IProjeto>;
export type EntityArrayResponseType = HttpResponse<IProjeto[]>;

@Injectable({ providedIn: 'root' })
export class ProjetoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/projetos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/projetos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(projeto: IProjeto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projeto);
    return this.http
      .post<IProjeto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(projeto: IProjeto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projeto);
    return this.http
      .put<IProjeto>(`${this.resourceUrl}/${getProjetoIdentifier(projeto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(projeto: IProjeto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projeto);
    return this.http
      .patch<IProjeto>(`${this.resourceUrl}/${getProjetoIdentifier(projeto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProjeto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProjeto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProjeto[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addProjetoToCollectionIfMissing(projetoCollection: IProjeto[], ...projetosToCheck: (IProjeto | null | undefined)[]): IProjeto[] {
    const projetos: IProjeto[] = projetosToCheck.filter(isPresent);
    if (projetos.length > 0) {
      const projetoCollectionIdentifiers = projetoCollection.map(projetoItem => getProjetoIdentifier(projetoItem)!);
      const projetosToAdd = projetos.filter(projetoItem => {
        const projetoIdentifier = getProjetoIdentifier(projetoItem);
        if (projetoIdentifier == null || projetoCollectionIdentifiers.includes(projetoIdentifier)) {
          return false;
        }
        projetoCollectionIdentifiers.push(projetoIdentifier);
        return true;
      });
      return [...projetosToAdd, ...projetoCollection];
    }
    return projetoCollection;
  }

  protected convertDateFromClient(projeto: IProjeto): IProjeto {
    return Object.assign({}, projeto, {
      inicio: projeto.inicio?.isValid() ? projeto.inicio.toJSON() : undefined,
      fim: projeto.fim?.isValid() ? projeto.fim.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.inicio = res.body.inicio ? dayjs(res.body.inicio) : undefined;
      res.body.fim = res.body.fim ? dayjs(res.body.fim) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((projeto: IProjeto) => {
        projeto.inicio = projeto.inicio ? dayjs(projeto.inicio) : undefined;
        projeto.fim = projeto.fim ? dayjs(projeto.fim) : undefined;
      });
    }
    return res;
  }
}
