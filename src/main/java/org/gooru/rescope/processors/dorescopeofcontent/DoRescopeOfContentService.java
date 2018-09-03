package org.gooru.rescope.processors.dorescopeofcontent;

import org.gooru.rescope.infra.services.RescopeApplicableService;
import org.gooru.rescope.infra.services.RescopeRequestQueueService;

/**
 * @author ashish on 18/5/18.
 */
class DoRescopeOfContentService {

    private DoRescopeOfContentCommand command;

    DoRescopeOfContentService() {
    }

    private void doRescopeForIL() {
        if (RescopeApplicableService.isRescopeApplicableToCourseInIL(command.getCourseId())) {
            RescopeRequestQueueService service = RescopeRequestQueueService.build();
            service.enqueue(command.asRescopeContext());
        }
    }

    private void doRescopeInClass() {
        if (RescopeApplicableService.isRescopeApplicableToClass(command.getClassId())) {
            RescopeRequestQueueService service = RescopeRequestQueueService.build();
            service.enqueue(command.asRescopeContext());
        }
    }

    void doRescope(DoRescopeOfContentCommand command) {
        this.command = command;
        if (command.getClassId() != null) {
            doRescopeInClass();
        } else {
            doRescopeForIL();
        }
    }
}
