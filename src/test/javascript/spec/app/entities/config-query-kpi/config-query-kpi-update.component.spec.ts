import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AppTestModule } from '../../../test.module';
import { ConfigQueryKpiUpdateComponent } from 'app/entities/config-query-kpi/config-query-kpi-update.component';
import { ConfigQueryKpiService } from 'app/entities/config-query-kpi/config-query-kpi.service';
import { ConfigQueryKpi } from 'app/shared/model/config-query-kpi.model';

describe('Component Tests', () => {
  describe('ConfigQueryKpi Management Update Component', () => {
    let comp: ConfigQueryKpiUpdateComponent;
    let fixture: ComponentFixture<ConfigQueryKpiUpdateComponent>;
    let service: ConfigQueryKpiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppTestModule],
        declarations: [ConfigQueryKpiUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ConfigQueryKpiUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfigQueryKpiUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfigQueryKpiService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ConfigQueryKpi(123);
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
        const entity = new ConfigQueryKpi();
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
