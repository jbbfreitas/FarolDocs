jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEtiqueta, Etiqueta } from '../etiqueta.model';
import { EtiquetaService } from '../service/etiqueta.service';

import { EtiquetaRoutingResolveService } from './etiqueta-routing-resolve.service';

describe('Service Tests', () => {
  describe('Etiqueta routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EtiquetaRoutingResolveService;
    let service: EtiquetaService;
    let resultEtiqueta: IEtiqueta | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EtiquetaRoutingResolveService);
      service = TestBed.inject(EtiquetaService);
      resultEtiqueta = undefined;
    });

    describe('resolve', () => {
      it('should return IEtiqueta returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEtiqueta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEtiqueta).toEqual({ id: 123 });
      });

      it('should return new IEtiqueta if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEtiqueta = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEtiqueta).toEqual(new Etiqueta());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Etiqueta })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEtiqueta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEtiqueta).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
