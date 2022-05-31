import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, Observer, ReplaySubject } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPronounce, Pronounce } from '../pronounce.model';
import { PronounceService } from '../service/pronounce.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { DomSanitizer } from '@angular/platform-browser';


@Component({
  selector: 'jhi-pronounce-update',
  templateUrl: './pronounce-update.component.html',
})
export class PronounceUpdateComponent implements OnInit {
  isSaving = false;

  mediaRecorder: any | null = null;
  audioChunks: any = [];
  audioFiles: any;

  editForm = this.fb.group({
    id: [],
    login: [],
    firstName: [],
    lastName: [],
    preferredName: [],
    country: [],
    language: [],
    pronunciation: [],
    pronunciationContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected pronounceService: PronounceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private cd: ChangeDetectorRef, public domSanitizer: DomSanitizer
  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pronounce }) => {
      this.updateForm(pronounce);
    });

    navigator.mediaDevices.getUserMedia(
      { audio: true }).then
      (stream => {



        this.mediaRecorder = new MediaRecorder(stream);
        this.mediaRecorder.ondataavailable = (e: any) => {
          this.audioChunks.push(e.data);
        }


        this.mediaRecorder.onstop = (e: any) => {

          const blob = new Blob(this.audioChunks, { type: 'audio/mpeg; codecs=opus' });
          const audioURL = URL.createObjectURL(blob);
          this.audioFiles = this.domSanitizer.bypassSecurityTrustUrl(audioURL);

          this.cd.detectChanges();

        }
      })
      .catch(e => { alert(e); });
  }

  stop(): void {
    this.mediaRecorder.stop();
    alert("Stopped");
  }
  start(): void {
    this.mediaRecorder.start();
    this.audioChunks = [];
    this.audioFiles = null;
    alert("started");
  }

   use(): void {

    const frm = this.editForm;
    const blob = new Blob(this.audioChunks, { type: 'audio/wav; codecs=opus' });


    const reader = new FileReader();
    reader.readAsDataURL(blob); 
    reader.onloadend = function() {
      const base64String = reader.result!.toString().replace(/^data:(.*,)?/, "");             
      frm.patchValue({ pronunciation: base64String , pronunciationContentType : "audio/mpeg" });
      
      alert(base64String);
    }

    


  }

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  convertBlobToBase64 = (blob: Blob) =>
    new Promise((resolve, reject) => {
      const reader = new FileReader;
      reader.onerror = reject;

      reader.onloadend = () => {
        alert(this.base64MimeType(reader.result));
        resolve(reader.result);
      };
      reader.readAsDataURL(blob);
    });


  base64MimeType(encoded: any): any {
    if (!encoded) { return ""; }
    const mime = encoded.match(/data:([a-zA-Z0-9]+\/[a-zA-Z0-9-.+]+).*,.*/);

    if (mime?.length) { return mime[1]; }

    return "";

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
      login: pronounce.login,
      firstName: pronounce.firstName,
      lastName: pronounce.lastName,
      preferredName: pronounce.preferredName,
      country: pronounce.country,
      language: pronounce.language,
      pronunciation: pronounce.pronunciation,
      pronunciationContentType: pronounce.pronunciationContentType,
    });
  }

  protected createFromForm(): IPronounce {
    return {
      ...new Pronounce(),
      id: this.editForm.get(['id'])!.value,
      login: this.editForm.get(['login'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      preferredName: this.editForm.get(['preferredName'])!.value,
      country: this.editForm.get(['country'])!.value,
      language: this.editForm.get(['language'])!.value,
      pronunciationContentType: this.editForm.get(['pronunciationContentType'])!.value,
      pronunciation: this.editForm.get(['pronunciation'])!.value,
    };
  }


}
