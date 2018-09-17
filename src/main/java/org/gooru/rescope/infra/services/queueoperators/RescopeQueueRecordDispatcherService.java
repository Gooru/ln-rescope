package org.gooru.rescope.infra.services.queueoperators;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * This service is responsible to read the record from the queue and return to caller. Caller needs
 * to decides as to what they want to do with the record. This means updating the status of record
 * to indicate that they are being processed. However, fetching the record using this service will
 * mark the record for being dispatched.
 *
 * @author ashish on 20/5/18.
 */
public interface RescopeQueueRecordDispatcherService {

  RescopeQueueModel getNextRecordToDispatch();

  static RescopeQueueRecordDispatcherService build() {
    return new RescopeQueueRecordDispatcherServiceImpl(DBICreator.getDbiForDefaultDS());
  }

  static RescopeQueueRecordDispatcherService build(DBI dbi) {
    return new RescopeQueueRecordDispatcherServiceImpl(dbi);
  }

}
