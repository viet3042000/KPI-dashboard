import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AppTestModule } from '../../../test.module';
import { ConfigQueryKpiDeleteDialogComponent } from 'app/entities/config-query-kpi/config-query-kpi-delete-dialog.component';
import { ConfigQueryKpiService } from 'app/entities/config-query-kpi/config-query-kpi.service';

describe('Component Tests', () => {
  describe('ConfigQueryKpi Management Delete Component', () => {
    let comp: ConfigQueryKpiDeleteDialogComponent;
    let fixture: ComponentFixture<ConfigQueryKpiDeleteDialogComponent>;
    let service: ConfigQueryKpiService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AppTestModule],
        declarations: [ConfigQueryKpiDeleteDialogComponent]
      })
        .overrideTemplate(ConfigQueryKpiDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConfigQueryKpiDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfigQueryKpiService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
