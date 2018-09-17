package org.gooru.rescope.infra.services.queueoperators;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

interface RescopeQueueOperatorDao {

  @SqlUpdate("update rescope_queue set status = 0 where status != 0")
  void initializeQueueStatus();

  @Mapper(RescopeQueueModel.RescopeQueueModelMapper.class)
  @SqlQuery(
      "select id, user_id, course_id, class_id, priority, status from rescope_queue where status = 0 order by"
          + " priority desc limit 1")
  RescopeQueueModel getNextDispatchableModel();

  @SqlUpdate("update rescope_queue set status = 1 where id = :modelId")
  void setQueuedRecordStatusAsDispatched(@Bind("modelId") Long id);

}
