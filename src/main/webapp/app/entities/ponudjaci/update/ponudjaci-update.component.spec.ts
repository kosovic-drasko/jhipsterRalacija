import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PonudjaciService } from '../service/ponudjaci.service';
import { IPonudjaci, Ponudjaci } from '../ponudjaci.model';

import { PonudjaciUpdateComponent } from './ponudjaci-update.component';

describe('Ponudjaci Management Update Component', () => {
  let comp: PonudjaciUpdateComponent;
  let fixture: ComponentFixture<PonudjaciUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ponudjaciService: PonudjaciService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PonudjaciUpdateComponent],
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
      .overrideTemplate(PonudjaciUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PonudjaciUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ponudjaciService = TestBed.inject(PonudjaciService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ponudjaci: IPonudjaci = { id: 456 };

      activatedRoute.data = of({ ponudjaci });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ponudjaci));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ponudjaci>>();
      const ponudjaci = { id: 123 };
      jest.spyOn(ponudjaciService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ponudjaci });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ponudjaci }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ponudjaciService.update).toHaveBeenCalledWith(ponudjaci);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ponudjaci>>();
      const ponudjaci = new Ponudjaci();
      jest.spyOn(ponudjaciService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ponudjaci });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ponudjaci }));
      saveSubject.complete();

      // THEN
      expect(ponudjaciService.create).toHaveBeenCalledWith(ponudjaci);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ponudjaci>>();
      const ponudjaci = { id: 123 };
      jest.spyOn(ponudjaciService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ponudjaci });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ponudjaciService.update).toHaveBeenCalledWith(ponudjaci);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
