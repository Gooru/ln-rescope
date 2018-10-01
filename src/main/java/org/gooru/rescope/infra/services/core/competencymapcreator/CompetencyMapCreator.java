package org.gooru.rescope.infra.services.core.competencymapcreator;

import org.gooru.rescope.infra.jdbi.DBICreator;
import org.gooru.rescope.infra.services.core.RescopeProcessorContext;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyMap;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface CompetencyMapCreator {

  CompetencyMap create(RescopeProcessorContext context);

  static CompetencyMapCreator buildSubjectCompetencyMapCreator(DBI dbi) {
    return new SubjectCompetencyMapCreator(dbi);
  }

  static CompetencyMapCreator buildSubjectCompetencyMapCreator() {
    return new SubjectCompetencyMapCreator(DBICreator.getDbiForDsdbDS());
  }

}
