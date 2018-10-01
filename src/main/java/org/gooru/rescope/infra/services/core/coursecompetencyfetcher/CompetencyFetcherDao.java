package org.gooru.rescope.infra.services.core.coursecompetencyfetcher;

import java.util.List;
import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */
interface CompetencyFetcherDao {

  @SqlQuery("select jsonb_object_keys(aggregated_gut_codes) from course where id = :courseId and is_deleted = false")
  List<String> findCompetenciesForCourse(@Bind("courseId") UUID course);

}
