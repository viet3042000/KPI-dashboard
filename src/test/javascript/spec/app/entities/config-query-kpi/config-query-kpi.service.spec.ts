import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ConfigQueryKpiService } from 'app/entities/config-query-kpi/config-query-kpi.service';
import { IConfigQueryKpi, ConfigQueryKpi } from 'app/shared/model/config-query-kpi.model';

describe('Service Tests', () => {
  describe('ConfigQueryKpi Service', () => {
    let injector: TestBed;
    let service: ConfigQueryKpiService;
    let httpMock: HttpTestingController;
    let elemDefault: IConfigQueryKpi;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(ConfigQueryKpiService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new ConfigQueryKpi(0, 0, 0, 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            updateTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a ConfigQueryKpi', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            updateTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            updateTime: currentDate
          },
          returnedFromService
        );
        service
          .create(new ConfigQueryKpi(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a ConfigQueryKpi', () => {
        const returnedFromService = Object.assign(
          {
            timeType: 1,
            inputLevel: 1,
            queryData: 'BBBBBB',
            queryCheckData: 'BBBBBB',
            status: 1,
            description: 'BBBBBB',
            updateTime: currentDate.format(DATE_TIME_FORMAT),
            updateUser: 'BBBBBB',
            listParentInputLevel: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            updateTime: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of ConfigQueryKpi', () => {
        const returnedFromService = Object.assign(
          {
            timeType: 1,
            inputLevel: 1,
            queryData: 'BBBBBB',
            queryCheckData: 'BBBBBB',
            status: 1,
            description: 'BBBBBB',
            updateTime: currentDate.format(DATE_TIME_FORMAT),
            updateUser: 'BBBBBB',
            listParentInputLevel: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            updateTime: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ConfigQueryKpi', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
