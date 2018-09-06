package org.gooru.rescope.processors.dorescopeofcontent;

import org.gooru.rescope.infra.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.util.UUID;

interface DoRescopeOfContentDao {

    @SqlUpdate("delete from user_rescoped_content where user_id = any(:userIds) and course_id = :courseId and " +
                   "class_id = :classId")
    void resetRescopeInfoInClassForSpecifiedUsers(@Bind("userIds") PGArray<UUID> userIds,
        @Bind("courseId") UUID courseId, @Bind("classId") UUID classId);

    @SqlUpdate("delete from user_rescoped_content where course_id = :courseId and class_id = :classId")
    void resetRescopeInfoInClassForAllUsers(@Bind("courseId") UUID courseId, @Bind("classId") UUID classId);

    @SqlUpdate("delete from user_rescoped_content where user_id = any(:userIds) and course_id = :courseId and " +
                   "class_id is null")
    void resetRescopeInfoForILForSpecifiedUsers(@Bind("userIds") PGArray<UUID> userIds, @Bind("courseId") UUID courseId);
}
