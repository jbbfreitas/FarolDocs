import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEtiqueta, Etiqueta } from '../etiqueta.model';
import { EtiquetaService } from '../service/etiqueta.service';

@Injectable({ providedIn: 'root' })
export class EtiquetaRoutingResolveService implements Resolve<IEtiqueta> {
  constructor(protected service: EtiquetaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEtiqueta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((etiqueta: HttpResponse<Etiqueta>) => {
          if (etiqueta.body) {
            return of(etiqueta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Etiqueta());
  }
}
