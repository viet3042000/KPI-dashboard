import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AppTestModule } from '../../../test.module';
import { MonitorQueryKpiUpdateComponent } from 'app/entities/monitor-query-kpi/monitor-query-kpi-update.component';
import { MonitorQueryKpiService } from 'app/entities/monitor-query-kpi/monitor-query-kpi.service';
import { MonitorQueryKpi } from 'app/shared/model/monitor-query-kpi.model';

describe('Component Tests', () => {
  describe('MonitorQueryKpi Management Update Component', () => {
    let comp: MonitorQueryKpiUpdateComponent;
    let fixture: ComponentFixture<MonitorQueryKpiUpdateComponent>;
    let service: MonitorQueryKpiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppTestModule],
        declarations: [MonitorQueryKpiUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MonitorQueryKpiUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MonitorQueryKpiUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MonitorQueryKpiService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MonitorQueryKpi(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new MonitorQueryKpi();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
