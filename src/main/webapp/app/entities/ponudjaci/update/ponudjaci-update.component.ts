import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPonudjaci, Ponudjaci } from '../ponudjaci.model';
import { PonudjaciService } from '../service/ponudjaci.service';

@Component({
  selector: 'jhi-ponudjaci-update',
  templateUrl: './ponudjaci-update.component.html',
})
export class PonudjaciUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nazivPonudjaca: [],
  });

  constructor(protected ponudjaciService: PonudjaciService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ponudjaci }) => {
      this.updateForm(ponudjaci);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ponudjaci = this.createFromForm();
    if (ponudjaci.id !== undefined) {
      this.subscribeToSaveResponse(this.ponudjaciService.update(ponudjaci));
    } else {
      this.subscribeToSaveResponse(this.ponudjaciService.create(ponudjaci));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPonudjaci>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(ponudjaci: IPonudjaci): void {
    this.editForm.patchValue({
      id: ponudjaci.id,
      nazivPonudjaca: ponudjaci.nazivPonudjaca,
    });
  }

  protected createFromForm(): IPonudjaci {
    return {
      ...new Ponudjaci(),
      id: this.editForm.get(['id'])!.value,
      nazivPonudjaca: this.editForm.get(['nazivPonudjaca'])!.value,
    };
  }
}
