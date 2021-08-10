jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITipoNorma, TipoNorma } from '../tipo-norma.model';
import { TipoNormaService } from '../service/tipo-norma.service';

import { TipoNormaRoutingResolveService } from './tipo-norma-routing-resolve.service';

describe('Service Tests', () => {
  describe('TipoNorma routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TipoNormaRoutingResolveService;
    let service: TipoNormaService;
    let resultTipoNorma: ITipoNorma | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TipoNormaRoutingResolveService);
      service = TestBed.inject(TipoNormaService);
      resultTipoNorma = undefined;
    });

    describe('resolve', () => {
      it('should return ITipoNorma returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTipoNorma = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTipoNorma).toEqual({ id: 123 });
      });

      it('should return new ITipoNorma if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTipoNorma = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTipoNorma).toEqual(new TipoNorma());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoNorma })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTipoNorma = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTipoNorma).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
