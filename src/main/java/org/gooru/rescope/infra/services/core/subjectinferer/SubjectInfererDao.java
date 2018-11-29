package org.gooru.rescope.infra.services.core.subjectinferer;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */
interface SubjectInfererDao {

  @SqlQuery("select subject_bucket from course where id = :courseId and is_deleted = false")
  String fetchSubjectBucketForCourse(@Bind("courseId") UUID courseId);

  @SqlQuery("select default_taxonomy_subject_id from taxonomy_subject where code = :subjectCode")
  String fetchGutSubjectCodeForFrameworkSubjectCode(@Bind("subjectCode") String subjectCode);
}
