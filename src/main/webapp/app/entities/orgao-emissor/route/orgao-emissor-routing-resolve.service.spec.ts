jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrgaoEmissor, OrgaoEmissor } from '../orgao-emissor.model';
import { OrgaoEmissorService } from '../service/orgao-emissor.service';

import { OrgaoEmissorRoutingResolveService } from './orgao-emissor-routing-resolve.service';

describe('Service Tests', () => {
  describe('OrgaoEmissor routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrgaoEmissorRoutingResolveService;
    let service: OrgaoEmissorService;
    let resultOrgaoEmissor: IOrgaoEmissor | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrgaoEmissorRoutingResolveService);
      service = TestBed.inject(OrgaoEmissorService);
      resultOrgaoEmissor = undefined;
    });

    describe('resolve', () => {
      it('should return IOrgaoEmissor returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrgaoEmissor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrgaoEmissor).toEqual({ id: 123 });
      });

      it('should return new IOrgaoEmissor if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrgaoEmissor = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrgaoEmissor).toEqual(new OrgaoEmissor());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrgaoEmissor })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrgaoEmissor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrgaoEmissor).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
