package org.gooru.rescope.infra.services.core.competencymapcreator;

import java.util.List;
import org.gooru.rescope.infra.services.core.RescopeProcessorContext;
import org.gooru.rescope.infra.services.core.algebra.competency.Competency;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyMap;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class SubjectCompetencyMapCreator implements CompetencyMapCreator {

  private final DBI dbi;
  private CompetencyMapCreatorDao dao;
  private static final Logger LOGGER = LoggerFactory.getLogger(SubjectCompetencyMapCreator.class);

  SubjectCompetencyMapCreator(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public CompetencyMap create(RescopeProcessorContext context) {
    List<Competency> competenciesInSubject = fetchDao()
        .fetchAllCompetenciesForSubject(context.getSubject());
    if (competenciesInSubject == null || competenciesInSubject.isEmpty()) {
      LOGGER.warn("Competencies not found for subject code: '{}'", context.getSubject());
      throw new IllegalStateException(
          "Competencies not found for subject code: " + context.getSubject());
    }
    return CompetencyMap.build(competenciesInSubject);
  }

  private CompetencyMapCreatorDao fetchDao() {
    if (dao == null) {
      dao = dbi.onDemand(CompetencyMapCreatorDao.class);
    }
    return dao;
  }
}
