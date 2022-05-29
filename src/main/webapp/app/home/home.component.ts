import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import {PronounceService} from  'app/entities/pronounce/service/pronounce.service';
import {Pronounce} from  'app/entities/pronounce/pronounce.model';
import { HttpResponse } from '@angular/common/http';
import { DataUtils } from 'app/core/util/data-util.service';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  pronounce: Pronounce | null = null;
  public audioUrl  = "" ;

  private readonly destroy$ = new Subject<void>();

  constructor(public domSanitizer :DomSanitizer  , protected dataUtils: DataUtils, private accountService: AccountService, private pronounceService:  PronounceService, private router: Router) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
          this.account = account;
          this.pronounceService.findByLogin (account!.login)
          .subscribe((res: HttpResponse<Pronounce>) => {
            this.pronounce = res.body;
            this.audioUrl =  `data:audio/mpeg;base64,` + String(this.pronounce!.pronunciation) ;
        
            alert(this.audioUrl);
          });
            
      });



}

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  cleanData(data: string): string{
    alert("");
    return String(this.domSanitizer.bypassSecurityTrustUrl("data:audio/mpeg;base64," + data));
  }
}
