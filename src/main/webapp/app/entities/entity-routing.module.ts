import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ponude',
        data: { pageTitle: 'Ponudes' },
        loadChildren: () => import('./ponude/ponude.module').then(m => m.PonudeModule),
      },
      {
        path: 'ponudjaci',
        data: { pageTitle: 'Ponudjacis' },
        loadChildren: () => import('./ponudjaci/ponudjaci.module').then(m => m.PonudjaciModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
