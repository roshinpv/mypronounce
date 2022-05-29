import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PronounceComponent } from '../list/pronounce.component';
import { PronounceDetailComponent } from '../detail/pronounce-detail.component';
import { PronounceUpdateComponent } from '../update/pronounce-update.component';
import { PronounceRoutingResolveService } from './pronounce-routing-resolve.service';

const pronounceRoute: Routes = [
  {
    path: '',
    component: PronounceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PronounceDetailComponent,
    resolve: {
      pronounce: PronounceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PronounceUpdateComponent,
    resolve: {
      pronounce: PronounceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PronounceUpdateComponent,
    resolve: {
      pronounce: PronounceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pronounceRoute)],
  exports: [RouterModule],
})
export class PronounceRoutingModule {}
