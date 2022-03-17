import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPonudjaci, Ponudjaci } from '../ponudjaci.model';

import { PonudjaciService } from './ponudjaci.service';

describe('Ponudjaci Service', () => {
  let service: PonudjaciService;
  let httpMock: HttpTestingController;
  let elemDefault: IPonudjaci;
  let expectedResult: IPonudjaci | IPonudjaci[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PonudjaciService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nazivPonudjaca: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Ponudjaci', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Ponudjaci()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ponudjaci', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nazivPonudjaca: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ponudjaci', () => {
      const patchObject = Object.assign({}, new Ponudjaci());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ponudjaci', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nazivPonudjaca: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Ponudjaci', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPonudjaciToCollectionIfMissing', () => {
      it('should add a Ponudjaci to an empty array', () => {
        const ponudjaci: IPonudjaci = { id: 123 };
        expectedResult = service.addPonudjaciToCollectionIfMissing([], ponudjaci);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ponudjaci);
      });

      it('should not add a Ponudjaci to an array that contains it', () => {
        const ponudjaci: IPonudjaci = { id: 123 };
        const ponudjaciCollection: IPonudjaci[] = [
          {
            ...ponudjaci,
          },
          { id: 456 },
        ];
        expectedResult = service.addPonudjaciToCollectionIfMissing(ponudjaciCollection, ponudjaci);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ponudjaci to an array that doesn't contain it", () => {
        const ponudjaci: IPonudjaci = { id: 123 };
        const ponudjaciCollection: IPonudjaci[] = [{ id: 456 }];
        expectedResult = service.addPonudjaciToCollectionIfMissing(ponudjaciCollection, ponudjaci);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ponudjaci);
      });

      it('should add only unique Ponudjaci to an array', () => {
        const ponudjaciArray: IPonudjaci[] = [{ id: 123 }, { id: 456 }, { id: 74936 }];
        const ponudjaciCollection: IPonudjaci[] = [{ id: 123 }];
        expectedResult = service.addPonudjaciToCollectionIfMissing(ponudjaciCollection, ...ponudjaciArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ponudjaci: IPonudjaci = { id: 123 };
        const ponudjaci2: IPonudjaci = { id: 456 };
        expectedResult = service.addPonudjaciToCollectionIfMissing([], ponudjaci, ponudjaci2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ponudjaci);
        expect(expectedResult).toContain(ponudjaci2);
      });

      it('should accept null and undefined values', () => {
        const ponudjaci: IPonudjaci = { id: 123 };
        expectedResult = service.addPonudjaciToCollectionIfMissing([], null, ponudjaci, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ponudjaci);
      });

      it('should return initial array if no Ponudjaci is added', () => {
        const ponudjaciCollection: IPonudjaci[] = [{ id: 123 }];
        expectedResult = service.addPonudjaciToCollectionIfMissing(ponudjaciCollection, undefined, null);
        expect(expectedResult).toEqual(ponudjaciCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
