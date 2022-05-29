import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPronounce, getPronounceIdentifier } from '../pronounce.model';

export type EntityResponseType = HttpResponse<IPronounce>;
export type EntityArrayResponseType = HttpResponse<IPronounce[]>;

@Injectable({ providedIn: 'root' })
export class PronounceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pronounces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pronounce: IPronounce): Observable<EntityResponseType> {
    return this.http.post<IPronounce>(this.resourceUrl, pronounce, { observe: 'response' });
  }

  update(pronounce: IPronounce): Observable<EntityResponseType> {
    return this.http.put<IPronounce>(`${this.resourceUrl}/${getPronounceIdentifier(pronounce) as number}`, pronounce, {
      observe: 'response',
    });
  }

  partialUpdate(pronounce: IPronounce): Observable<EntityResponseType> {
    return this.http.patch<IPronounce>(`${this.resourceUrl}/${getPronounceIdentifier(pronounce) as number}`, pronounce, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPronounce>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPronounce[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPronounceToCollectionIfMissing(
    pronounceCollection: IPronounce[],
    ...pronouncesToCheck: (IPronounce | null | undefined)[]
  ): IPronounce[] {
    const pronounces: IPronounce[] = pronouncesToCheck.filter(isPresent);
    if (pronounces.length > 0) {
      const pronounceCollectionIdentifiers = pronounceCollection.map(pronounceItem => getPronounceIdentifier(pronounceItem)!);
      const pronouncesToAdd = pronounces.filter(pronounceItem => {
        const pronounceIdentifier = getPronounceIdentifier(pronounceItem);
        if (pronounceIdentifier == null || pronounceCollectionIdentifiers.includes(pronounceIdentifier)) {
          return false;
        }
        pronounceCollectionIdentifiers.push(pronounceIdentifier);
        return true;
      });
      return [...pronouncesToAdd, ...pronounceCollection];
    }
    return pronounceCollection;
  }
}
