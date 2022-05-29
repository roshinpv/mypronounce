import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPronounce, Pronounce } from '../pronounce.model';
import { PronounceService } from '../service/pronounce.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-pronounce-update',
  templateUrl: './pronounce-update.component.html',
})
export class PronounceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    preferredName: [],
    pronunciation: [],
    pronunciationContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected pronounceService: PronounceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pronounce }) => {
      this.updateForm(pronounce);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('pronounceApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pronounce = this.createFromForm();
    if (pronounce.id !== undefined) {
      this.subscribeToSaveResponse(this.pronounceService.update(pronounce));
    } else {
      this.subscribeToSaveResponse(this.pronounceService.create(pronounce));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPronounce>>): void {
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

  protected updateForm(pronounce: IPronounce): void {
    this.editForm.patchValue({
      id: pronounce.id,
      preferredName: pronounce.preferredName,
      pronunciation: pronounce.pronunciation,
      pronunciationContentType: pronounce.pronunciationContentType,
    });
  }

  protected createFromForm(): IPronounce {
    return {
      ...new Pronounce(),
      id: this.editForm.get(['id'])!.value,
      preferredName: this.editForm.get(['preferredName'])!.value,
      pronunciationContentType: this.editForm.get(['pronunciationContentType'])!.value,
      pronunciation: this.editForm.get(['pronunciation'])!.value,
    };
  }
}
