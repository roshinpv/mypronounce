import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PronounceComponent } from './list/pronounce.component';
import { PronounceDetailComponent } from './detail/pronounce-detail.component';
import { PronounceUpdateComponent } from './update/pronounce-update.component';
import { PronounceDeleteDialogComponent } from './delete/pronounce-delete-dialog.component';
import { PronounceRoutingModule } from './route/pronounce-routing.module';

@NgModule({
  imports: [SharedModule, PronounceRoutingModule],
  declarations: [PronounceComponent, PronounceDetailComponent, PronounceUpdateComponent, PronounceDeleteDialogComponent],
  entryComponents: [PronounceDeleteDialogComponent],
})
export class PronounceModule {}
