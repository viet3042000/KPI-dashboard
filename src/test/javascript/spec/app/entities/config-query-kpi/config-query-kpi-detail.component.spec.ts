import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppTestModule } from '../../../test.module';
import { ConfigQueryKpiDetailComponent } from 'app/entities/config-query-kpi/config-query-kpi-detail.component';
import { ConfigQueryKpi } from 'app/shared/model/config-query-kpi.model';

describe('Component Tests', () => {
  describe('ConfigQueryKpi Management Detail Component', () => {
    let comp: ConfigQueryKpiDetailComponent;
    let fixture: ComponentFixture<ConfigQueryKpiDetailComponent>;
    const route = ({ data: of({ configQueryKpi: new ConfigQueryKpi(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppTestModule],
        declarations: [ConfigQueryKpiDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ConfigQueryKpiDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConfigQueryKpiDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.configQueryKpi).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
