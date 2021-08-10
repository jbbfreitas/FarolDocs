import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrgaoEmissor, OrgaoEmissor } from '../orgao-emissor.model';
import { OrgaoEmissorService } from '../service/orgao-emissor.service';

@Injectable({ providedIn: 'root' })
export class OrgaoEmissorRoutingResolveService implements Resolve<IOrgaoEmissor> {
  constructor(protected service: OrgaoEmissorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrgaoEmissor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orgaoEmissor: HttpResponse<OrgaoEmissor>) => {
          if (orgaoEmissor.body) {
            return of(orgaoEmissor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrgaoEmissor());
  }
}
