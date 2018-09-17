package org.gooru.rescope.infra.services;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

interface RescopeQueueRecordProcessingServiceDao {

  @SqlQuery(
      "select exists (select 1 from user_rescoped_content where user_id = :userId and course_id = :courseId  "
          + "and class_id = :classId)")
  boolean rescopeDoneForUserInClass(@BindBean RescopeQueueModel model);

  @SqlQuery(
      "select exists (select 1 from user_rescoped_content where user_id = :userId and course_id = :courseId  "
          + "and class_id is null)")
  boolean rescopeDoneForUserInIL(@BindBean RescopeQueueModel model);

  @SqlUpdate(
      "insert into user_rescoped_content(user_id, class_id, course_id, skipped_content) values (:userId, "
          + ":classId, :courseId, :skippedContent::jsonb)")
  void persistRescopedContent(@BindBean RescopeQueueModel model,
      @Bind("skippedContent") String skippedContent);

  @SqlQuery("select exists (select 1 from rescope_queue where id = :id and status = 1)")
  boolean isQueuedRecordStillDispatched(@Bind("id") Long modelId);

  @SqlUpdate("delete from rescope_queue where id = :modelId")
  void dequeueRecord(@Bind("modelId") Long id);


}
