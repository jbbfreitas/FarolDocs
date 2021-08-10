import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrgaoEmissor } from '../orgao-emissor.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { OrgaoEmissorService } from '../service/orgao-emissor.service';
import { OrgaoEmissorDeleteDialogComponent } from '../delete/orgao-emissor-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-orgao-emissor',
  templateUrl: './orgao-emissor.component.html',
})
export class OrgaoEmissorComponent implements OnInit {
  orgaoEmissors: IOrgaoEmissor[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected orgaoEmissorService: OrgaoEmissorService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.orgaoEmissors = [];
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
      this.orgaoEmissorService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IOrgaoEmissor[]>) => {
            this.isLoading = false;
            this.paginateOrgaoEmissors(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.orgaoEmissorService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IOrgaoEmissor[]>) => {
          this.isLoading = false;
          this.paginateOrgaoEmissors(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.orgaoEmissors = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.orgaoEmissors = [];
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

  trackId(index: number, item: IOrgaoEmissor): number {
    return item.id!;
  }

  delete(orgaoEmissor: IOrgaoEmissor): void {
    const modalRef = this.modalService.open(OrgaoEmissorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orgaoEmissor = orgaoEmissor;
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

  protected paginateOrgaoEmissors(data: IOrgaoEmissor[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.orgaoEmissors.push(d);
      }
    }
  }
}
