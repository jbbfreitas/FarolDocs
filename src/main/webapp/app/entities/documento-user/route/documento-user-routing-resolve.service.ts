import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentoUser, DocumentoUser } from '../documento-user.model';
import { DocumentoUserService } from '../service/documento-user.service';

@Injectable({ providedIn: 'root' })
export class DocumentoUserRoutingResolveService implements Resolve<IDocumentoUser> {
  constructor(protected service: DocumentoUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentoUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentoUser: HttpResponse<DocumentoUser>) => {
          if (documentoUser.body) {
            return of(documentoUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentoUser());
  }
}
