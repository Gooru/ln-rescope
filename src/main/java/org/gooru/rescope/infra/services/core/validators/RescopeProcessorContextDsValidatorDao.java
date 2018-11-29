package org.gooru.rescope.infra.services.core.validators;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */
interface RescopeProcessorContextDsValidatorDao {

  @SqlQuery("select exists (select 1 from learner_profile_baselined where user_id = :userId "
      + " and course_id = :courseId and tx_subject_code = :subjectBucket and class_id = :classId)")
  boolean validateLPBaselinePresenceInClass(@Bind("userId") String userId,
      @Bind("courseId") String courseId, @Bind("classId") String classId,
      @Bind("subjectBucket") String subjectBucket);

  @SqlQuery("select exists (select 1 from learner_profile_baselined where user_id = :userId "
      + " and course_id = :courseId and tx_subject_code = :subjectBucket and class_id is null)")
  boolean validateLPBaselinePresenceForIL(@Bind("userId") String userId,
      @Bind("courseId") String courseId, @Bind("subjectBucket") String subjectBucket);
}
