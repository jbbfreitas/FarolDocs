jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITipo, Tipo } from '../tipo.model';
import { TipoService } from '../service/tipo.service';

import { TipoRoutingResolveService } from './tipo-routing-resolve.service';

describe('Service Tests', () => {
  describe('Tipo routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TipoRoutingResolveService;
    let service: TipoService;
    let resultTipo: ITipo | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TipoRoutingResolveService);
      service = TestBed.inject(TipoService);
      resultTipo = undefined;
    });

    describe('resolve', () => {
      it('should return ITipo returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTipo = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTipo).toEqual({ id: 123 });
      });

      it('should return new ITipo if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTipo = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTipo).toEqual(new Tipo());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Tipo })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTipo = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTipo).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
