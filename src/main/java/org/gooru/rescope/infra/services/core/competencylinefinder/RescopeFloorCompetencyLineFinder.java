package org.gooru.rescope.infra.services.core.competencylinefinder;

import java.util.List;
import org.gooru.rescope.infra.services.core.RescopeProcessorContext;
import org.gooru.rescope.infra.services.core.algebra.competency.Competency;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyAlgebraDefaultBuilder;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyLine;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyMap;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class RescopeFloorCompetencyLineFinder implements CompetencyLineFinder {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private CompetencyLineFinderDsDao dsDao;
  private RescopeProcessorContext context;

  RescopeFloorCompetencyLineFinder(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public CompetencyLine findCompetencyLineForRescope(RescopeProcessorContext context) {
    this.context = context;
    // Processing reaching here implies that baseline LP was done. It could be empty though
    List<Competency> userBaselinedCompetencies;
    if (context.isILExperience()) {
      userBaselinedCompetencies = fetchCompetencyLineFinderDsDao()
          .fetchCompetenciesForUserBaselineLPInIL(context.getSubject(),
              context.getCourseId().toString(), context.getUserId().toString());
    } else {
      userBaselinedCompetencies = fetchCompetencyLineFinderDsDao()
          .fetchCompetenciesForUserBaselineLPInClass(context.getSubject(),
              context.getClassId().toString(), context.getCourseId().toString(),
              context.getUserId().toString());
    }
    if (userBaselinedCompetencies != null && !userBaselinedCompetencies.isEmpty()) {
      return CompetencyMap.build(userBaselinedCompetencies).getCeilingLine();
    }
    return CompetencyAlgebraDefaultBuilder.getEmptyCompetencyLine();
  }

  private CompetencyLineFinderDsDao fetchCompetencyLineFinderDsDao() {
    if (dsDao == null) {
      dsDao = dbi4ds.onDemand(CompetencyLineFinderDsDao.class);
    }
    return dsDao;
  }
}
