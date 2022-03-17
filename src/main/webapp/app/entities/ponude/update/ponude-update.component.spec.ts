import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PonudeService } from '../service/ponude.service';
import { IPonude, Ponude } from '../ponude.model';
import { IPonudjaci } from 'app/entities/ponudjaci/ponudjaci.model';
import { PonudjaciService } from 'app/entities/ponudjaci/service/ponudjaci.service';

import { PonudeUpdateComponent } from './ponude-update.component';

describe('Ponude Management Update Component', () => {
  let comp: PonudeUpdateComponent;
  let fixture: ComponentFixture<PonudeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ponudeService: PonudeService;
  let ponudjaciService: PonudjaciService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PonudeUpdateComponent],
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
      .overrideTemplate(PonudeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PonudeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ponudeService = TestBed.inject(PonudeService);
    ponudjaciService = TestBed.inject(PonudjaciService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ponudjaci query and add missing value', () => {
      const ponude: IPonude = { id: 456 };
      const ponudjaci: IPonudjaci = { id: 40292 };
      ponude.ponudjaci = ponudjaci;

      const ponudjaciCollection: IPonudjaci[] = [{ id: 74556 }];
      jest.spyOn(ponudjaciService, 'query').mockReturnValue(of(new HttpResponse({ body: ponudjaciCollection })));
      const expectedCollection: IPonudjaci[] = [ponudjaci, ...ponudjaciCollection];
      jest.spyOn(ponudjaciService, 'addPonudjaciToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ponude });
      comp.ngOnInit();

      expect(ponudjaciService.query).toHaveBeenCalled();
      expect(ponudjaciService.addPonudjaciToCollectionIfMissing).toHaveBeenCalledWith(ponudjaciCollection, ponudjaci);
      expect(comp.ponudjacisCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ponude: IPonude = { id: 456 };
      const ponudjaci: IPonudjaci = { id: 4355 };
      ponude.ponudjaci = ponudjaci;

      activatedRoute.data = of({ ponude });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ponude));
      expect(comp.ponudjacisCollection).toContain(ponudjaci);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ponude>>();
      const ponude = { id: 123 };
      jest.spyOn(ponudeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ponude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ponude }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ponudeService.update).toHaveBeenCalledWith(ponude);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ponude>>();
      const ponude = new Ponude();
      jest.spyOn(ponudeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ponude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ponude }));
      saveSubject.complete();

      // THEN
      expect(ponudeService.create).toHaveBeenCalledWith(ponude);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ponude>>();
      const ponude = { id: 123 };
      jest.spyOn(ponudeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ponude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ponudeService.update).toHaveBeenCalledWith(ponude);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPonudjaciById', () => {
      it('Should return tracked Ponudjaci primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPonudjaciById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
