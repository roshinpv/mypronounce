import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPronounce, Pronounce } from '../pronounce.model';
import { PronounceService } from '../service/pronounce.service';

@Injectable({ providedIn: 'root' })
export class PronounceRoutingResolveService implements Resolve<IPronounce> {
  constructor(protected service: PronounceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPronounce> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pronounce: HttpResponse<Pronounce>) => {
          if (pronounce.body) {
            return of(pronounce.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pronounce());
  }
}
