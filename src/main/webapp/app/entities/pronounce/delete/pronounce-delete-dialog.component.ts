import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPronounce } from '../pronounce.model';
import { PronounceService } from '../service/pronounce.service';

@Component({
  templateUrl: './pronounce-delete-dialog.component.html',
})
export class PronounceDeleteDialogComponent {
  pronounce?: IPronounce;

  constructor(protected pronounceService: PronounceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pronounceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
