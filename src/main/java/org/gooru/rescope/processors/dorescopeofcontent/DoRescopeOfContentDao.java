package org.gooru.rescope.processors.dorescopeofcontent;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

interface DoRescopeOfContentDao {

  @SqlUpdate(
      "delete from user_rescoped_content where user_id = :userId and course_id = :courseId and class_id = :classId")
  void resetRescopeInfoInClassForSpecifiedUser(@Bind("userId") UUID userId,
      @Bind("courseId") UUID courseId, @Bind("classId") UUID classId);

  @SqlUpdate(
      "delete from user_rescoped_content where user_id = :userId and course_id = :courseId and class_id is null")
  void resetRescopeInfoForILForSpecifiedUser(@Bind("userId") UUID userId,
      @Bind("courseId") UUID courseId);
}
