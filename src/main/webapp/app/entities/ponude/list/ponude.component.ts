import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPonude } from '../ponude.model';
import { PonudeService } from '../service/ponude.service';
import { PonudeDeleteDialogComponent } from '../delete/ponude-delete-dialog.component';

@Component({
  selector: 'jhi-ponude',
  templateUrl: './ponude.component.html',
})
export class PonudeComponent implements OnInit {
  ponudes?: IPonude[];
  isLoading = false;

  constructor(protected ponudeService: PonudeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ponudeService.query().subscribe({
      next: (res: HttpResponse<IPonude[]>) => {
        this.isLoading = false;
        this.ponudes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPonude): number {
    return item.id!;
  }

  delete(ponude: IPonude): void {
    const modalRef = this.modalService.open(PonudeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ponude = ponude;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
