package org.gooru.rescope.infra.services.core.coursecompetencyfetcher;

import java.util.List;
import java.util.UUID;
import org.gooru.rescope.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */
public interface CompetencyFetcher {

  List<String> fetchCompetenciesForCourse(UUID courseId);

  static CompetencyFetcher build() {
    return new CompetencyFetcherImpl(DBICreator.getDbiForDefaultDS());
  }

  static CompetencyFetcher build(DBI dbi) {
    return new CompetencyFetcherImpl(dbi);
  }
}
