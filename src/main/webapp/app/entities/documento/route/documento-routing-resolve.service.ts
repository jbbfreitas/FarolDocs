import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumento, Documento } from '../documento.model';
import { DocumentoService } from '../service/documento.service';

@Injectable({ providedIn: 'root' })
export class DocumentoRoutingResolveService implements Resolve<IDocumento> {
  constructor(protected service: DocumentoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documento: HttpResponse<Documento>) => {
          if (documento.body) {
            return of(documento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Documento());
  }
}
