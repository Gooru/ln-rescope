package org.gooru.rescope.infra.services;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 20/5/18.
 */
public interface RescopeQueueRecordProcessingService {

  void doRescope(RescopeQueueModel model);

  static RescopeQueueRecordProcessingService build() {
    return new RescopeQueueRecordProcessingServiceImpl(DBICreator.getDbiForDefaultDS());
  }

  static RescopeQueueRecordProcessingService build(DBI dbi) {
    return new RescopeQueueRecordProcessingServiceImpl(dbi);
  }

}
