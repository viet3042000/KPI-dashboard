import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppTestModule } from '../../../test.module';
import { MonitorQueryKpiDetailComponent } from 'app/entities/monitor-query-kpi/monitor-query-kpi-detail.component';
import { MonitorQueryKpi } from 'app/shared/model/monitor-query-kpi.model';

describe('Component Tests', () => {
  describe('MonitorQueryKpi Management Detail Component', () => {
    let comp: MonitorQueryKpiDetailComponent;
    let fixture: ComponentFixture<MonitorQueryKpiDetailComponent>;
    const route = ({ data: of({ monitorQueryKpi: new MonitorQueryKpi(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppTestModule],
        declarations: [MonitorQueryKpiDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MonitorQueryKpiDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MonitorQueryKpiDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.monitorQueryKpi).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
