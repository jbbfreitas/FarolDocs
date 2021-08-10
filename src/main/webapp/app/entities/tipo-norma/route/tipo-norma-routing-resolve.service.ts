import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoNorma, TipoNorma } from '../tipo-norma.model';
import { TipoNormaService } from '../service/tipo-norma.service';

@Injectable({ providedIn: 'root' })
export class TipoNormaRoutingResolveService implements Resolve<ITipoNorma> {
  constructor(protected service: TipoNormaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoNorma> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoNorma: HttpResponse<TipoNorma>) => {
          if (tipoNorma.body) {
            return of(tipoNorma.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoNorma());
  }
}
