jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDocumentoEtiqueta, DocumentoEtiqueta } from '../documento-etiqueta.model';
import { DocumentoEtiquetaService } from '../service/documento-etiqueta.service';

import { DocumentoEtiquetaRoutingResolveService } from './documento-etiqueta-routing-resolve.service';

describe('Service Tests', () => {
  describe('DocumentoEtiqueta routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DocumentoEtiquetaRoutingResolveService;
    let service: DocumentoEtiquetaService;
    let resultDocumentoEtiqueta: IDocumentoEtiqueta | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DocumentoEtiquetaRoutingResolveService);
      service = TestBed.inject(DocumentoEtiquetaService);
      resultDocumentoEtiqueta = undefined;
    });

    describe('resolve', () => {
      it('should return IDocumentoEtiqueta returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentoEtiqueta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentoEtiqueta).toEqual({ id: 123 });
      });

      it('should return new IDocumentoEtiqueta if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentoEtiqueta = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDocumentoEtiqueta).toEqual(new DocumentoEtiqueta());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DocumentoEtiqueta })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentoEtiqueta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentoEtiqueta).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
