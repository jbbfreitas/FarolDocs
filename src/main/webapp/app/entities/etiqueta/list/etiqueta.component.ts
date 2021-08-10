import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEtiqueta } from '../etiqueta.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { EtiquetaService } from '../service/etiqueta.service';
import { EtiquetaDeleteDialogComponent } from '../delete/etiqueta-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-etiqueta',
  templateUrl: './etiqueta.component.html',
})
export class EtiquetaComponent implements OnInit {
  etiquetas: IEtiqueta[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected etiquetaService: EtiquetaService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.etiquetas = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.etiquetaService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IEtiqueta[]>) => {
            this.isLoading = false;
            this.paginateEtiquetas(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.etiquetaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IEtiqueta[]>) => {
          this.isLoading = false;
          this.paginateEtiquetas(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.etiquetas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.etiquetas = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (query) {
      this.predicate = '_score';
      this.ascending = false;
    } else {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEtiqueta): number {
    return item.id!;
  }

  delete(etiqueta: IEtiqueta): void {
    const modalRef = this.modalService.open(EtiquetaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.etiqueta = etiqueta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEtiquetas(data: IEtiqueta[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.etiquetas.push(d);
      }
    }
  }
}
