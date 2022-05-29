import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PronounceService } from '../service/pronounce.service';
import { IPronounce, Pronounce } from '../pronounce.model';

import { PronounceUpdateComponent } from './pronounce-update.component';

describe('Pronounce Management Update Component', () => {
  let comp: PronounceUpdateComponent;
  let fixture: ComponentFixture<PronounceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pronounceService: PronounceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PronounceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PronounceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PronounceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pronounceService = TestBed.inject(PronounceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pronounce: IPronounce = { id: 456 };

      activatedRoute.data = of({ pronounce });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pronounce));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pronounce>>();
      const pronounce = { id: 123 };
      jest.spyOn(pronounceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pronounce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pronounce }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pronounceService.update).toHaveBeenCalledWith(pronounce);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pronounce>>();
      const pronounce = new Pronounce();
      jest.spyOn(pronounceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pronounce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pronounce }));
      saveSubject.complete();

      // THEN
      expect(pronounceService.create).toHaveBeenCalledWith(pronounce);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pronounce>>();
      const pronounce = { id: 123 };
      jest.spyOn(pronounceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pronounce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pronounceService.update).toHaveBeenCalledWith(pronounce);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
