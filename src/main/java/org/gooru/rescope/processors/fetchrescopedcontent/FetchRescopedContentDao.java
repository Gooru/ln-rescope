package org.gooru.rescope.processors.fetchrescopedcontent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish on 17/5/18.
 */
interface FetchRescopedContentDao {

  @SqlQuery(
      "select skipped_content from user_rescoped_content where user_id = :userId and course_id = :courseId "
          + " and class_id = :classId")
  String fetchRescopedContentForUserInClass(
      @BindBean FetchRescopedContentCommand.FetchRescopedContentCommandBean bean);

  @SqlQuery(
      "select skipped_content from user_rescoped_content where user_id = :userId and course_id = :courseId "
          + " and class_id is null")
  String fetchRescopedContentForUserInIL(
      @BindBean FetchRescopedContentCommand.FetchRescopedContentCommandBean bean);

  @SqlQuery(
      "select exists (select 1 from class where id = :classId and (creator_id = :teacherId or collaborator ?? "
          + ":teacherId::text) and is_deleted = false )")
  boolean isUserTeacherOrCollaboratorForClass(
      @BindBean FetchRescopedContentCommand.FetchRescopedContentCommandBean bean);

}
