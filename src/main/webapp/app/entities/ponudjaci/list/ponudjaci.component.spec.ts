import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PonudjaciService } from '../service/ponudjaci.service';

import { PonudjaciComponent } from './ponudjaci.component';

describe('Ponudjaci Management Component', () => {
  let comp: PonudjaciComponent;
  let fixture: ComponentFixture<PonudjaciComponent>;
  let service: PonudjaciService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PonudjaciComponent],
    })
      .overrideTemplate(PonudjaciComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PonudjaciComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PonudjaciService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.ponudjacis?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
