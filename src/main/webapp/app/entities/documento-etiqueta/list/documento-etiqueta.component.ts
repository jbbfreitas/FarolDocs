import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentoEtiqueta } from '../documento-etiqueta.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { DocumentoEtiquetaService } from '../service/documento-etiqueta.service';
import { DocumentoEtiquetaDeleteDialogComponent } from '../delete/documento-etiqueta-delete-dialog.component';
import { IDocumento } from 'app/entities/documento/documento.model';

@Component({
  selector: 'jhi-documento-etiqueta',
  templateUrl: './documento-etiqueta.component.html',
})
export class DocumentoEtiquetaComponent implements OnInit {
  documentoEtiquetas?: IDocumentoEtiqueta[];
  currentSearch: string;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  documento!: IDocumento;

  constructor(
    protected documentoEtiquetaService: DocumentoEtiquetaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    if (this.currentSearch) {
      this.documentoEtiquetaService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IDocumentoEtiqueta[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          () => {
            this.isLoading = false;
            this.onError();
          }
        );
      return;
    }
    this.loadAllEtiquetasFromDocuments(1, true)//alterar aqui
  return   
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAllEtiquetasFromDocuments(1, true)//alterar aqui

  }

  ngOnInit(): void {
    this.handleNavigation();
    this.loadAllEtiquetasFromDocuments(1, true)

  }
  

    loadAllEtiquetasFromDocuments(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;    
    this.documentoEtiquetaService
    .queryAllEtiquetasFromDocuments({
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      //sort: this.sort(),
      id: this.documento.id
    })
    .subscribe(
      (res: HttpResponse<IDocumentoEtiqueta[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );

  }

  trackId(index: number, item: IDocumentoEtiqueta): number {
    return item.id!;
  }

  delete(documentoEtiqueta: IDocumentoEtiqueta): void {
    const modalRef = this.modalService.open(DocumentoEtiquetaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentoEtiqueta = documentoEtiqueta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAllEtiquetasFromDocuments(1,true);
      }
    });
  }
  previousState(): void {
    window.history.back();
  }
  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      this.documento = data.documento;//
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadAllEtiquetasFromDocuments(pageNumber, true)//alterar aqui

      }
    });
  }

  protected onSuccess(data: IDocumentoEtiqueta[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    if (navigate) {
      this.router.navigate(['/documento-etiqueta'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          search: this.currentSearch,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.documentoEtiquetas = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  
}
