package org.gooru.rescope.infra.services.core.competencylinefinder;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

interface CompetencyLineFinderCoreDao {

  @SqlQuery("select grade_upper_bound from class where id = :classId and is_deleted = false")
  Long fetchGradeUpperBoundForClass(@Bind("classId") UUID classId);


}
