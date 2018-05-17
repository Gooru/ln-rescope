package org.gooru.rescope.processors.fetchrescopedcontent;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish on 17/5/18.
 */
interface FetchRescopedContentDao {

    @SqlQuery("select skipped_content from user_rescoped_content where user_id = :userId and course_id = :courseId "
                  + " and class_id = :classId")
    String fetchRescopedContentForUserInClass(
        @BindBean FetchRescopedContentCommand.FetchRescopedContentCommandBean bean);

    @SqlQuery("select skipped_content from user_rescoped_content where user_id = :userId and course_id = :courseId "
                  + " and class_id is null")
    String fetchRescopedContentForUserInIL(@BindBean FetchRescopedContentCommand.FetchRescopedContentCommandBean bean);

}
