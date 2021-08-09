import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipo, Tipo } from '../tipo.model';
import { TipoService } from '../service/tipo.service';

@Injectable({ providedIn: 'root' })
export class TipoRoutingResolveService implements Resolve<ITipo> {
  constructor(protected service: TipoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipo: HttpResponse<Tipo>) => {
          if (tipo.body) {
            return of(tipo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tipo());
  }
}
