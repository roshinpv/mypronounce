import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pronounce',
        data: { pageTitle: 'Pronounces' },
        loadChildren: () => import('./pronounce/pronounce.module').then(m => m.PronounceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
