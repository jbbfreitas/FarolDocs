import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentoEtiqueta, DocumentoEtiqueta } from '../documento-etiqueta.model';
import { DocumentoEtiquetaService } from '../service/documento-etiqueta.service';

@Injectable({ providedIn: 'root' })
export class DocumentoEtiquetaRoutingResolveService implements Resolve<IDocumentoEtiqueta> {
  constructor(protected service: DocumentoEtiquetaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentoEtiqueta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentoEtiqueta: HttpResponse<DocumentoEtiqueta>) => {
          if (documentoEtiqueta.body) {
            return of(documentoEtiqueta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentoEtiqueta());
  }
}
