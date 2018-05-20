package org.gooru.rescope.infra.services;

import java.util.List;
import java.util.UUID;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.jdbi.PGArray;
import org.gooru.rescope.infra.jdbi.UUIDMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 18/5/18.
 */
interface RescopeRequestQueueDao {

    @SqlQuery("select exists (select 1 from class where id = :classId and is_deleted = false and is_archived = false)")
    boolean isClassNotDeletedAndNotArchived(@Bind("classId") UUID classId);

    @Mapper(UUIDMapper.class)
    @SqlQuery("select user_id from class_member where class_id = :classId and user_id is not null")
    List<UUID> fetchMembersOfClass(@Bind("classId") UUID classId);

    @Mapper(UUIDMapper.class)
    @SqlQuery("select user_id from class_member where class_id = :classId and user_id = any(:usersList)")
    List<UUID> fetchSpecifiedMembersOfClass(@Bind("classId") UUID classId, @Bind("usersList") PGArray<UUID> members);

    @Mapper(UUIDMapper.class)
    @SqlQuery("select course_id from class where id = :classId")
    UUID fetchCourseForClass(@Bind("classId") UUID classId);

    @SqlQuery("select exists(select 1 from course where id = :courseId and is_deleted = false)")
    boolean isCourseNotDeleted(@Bind("courseId") UUID courseId);

    @SqlBatch("insert into rescope_queue(user_id, course_id, class_id, priority, status) values (:members, :courseId,"
                  + " :classId, :priority, :status) ON CONFLICT DO NOTHING")
    void queueRequests(@Bind("members") List<UUID> userId, @BindBean RescopeQueueModel rescopeQueueModel);

    @SqlUpdate("update rescope_queue set status = 0 where status != 0")
    void initializeQueueStatus();

    @Mapper(RescopeQueueModel.RescopeQueueModelMapper.class)
    @SqlQuery("select id, user_id, course_id, class_id, priority, status from rescope_queue where status = 0 order by"
                  + " priority desc limit 1")
    RescopeQueueModel getNextDispatchableModel();

    @SqlUpdate("update rescope_queue set status = 1 where id = :modelId")
    void setQueuedRecordStatusAsDispatched(@Bind("modelId") Long id);

    @SqlUpdate("delete from rescope_queue where id = :modelId")
    void dequeueRecord(@Bind("modelId") Long id);

    @SqlQuery("select exists (select 1 from rescope_queue where id = :id and status = 1)")
    boolean isQueuedRecordStillDispatched(@Bind("id") Long modelId);

    @SqlQuery("select exists (select 1 from user_rescoped_content where user_id = :userId and course_id = :courseId  "
                  + "and class_id = :classId)")
    boolean rescopeDoneForUserInClass(@BindBean RescopeQueueModel model);

    @SqlQuery("select exists (select 1 from user_rescoped_content where user_id = :userId and course_id = :courseId  "
                  + "and class_id is null)")
    boolean rescopeDoneForUserInIL(@BindBean RescopeQueueModel model);

}
