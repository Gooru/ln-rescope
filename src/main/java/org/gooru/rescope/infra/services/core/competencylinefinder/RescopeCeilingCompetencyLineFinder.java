package org.gooru.rescope.infra.services.core.competencylinefinder;

import java.util.List;
import org.gooru.rescope.infra.services.core.RescopeProcessorContext;
import org.gooru.rescope.infra.services.core.algebra.competency.Competency;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyAlgebraDefaultBuilder;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyLine;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyMap;
import org.gooru.rescope.infra.services.core.coursecompetencyfetcher.CompetencyFetcher;
import org.gooru.rescope.infra.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class RescopeCeilingCompetencyLineFinder implements CompetencyLineFinder {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private RescopeProcessorContext context;
  private CompetencyLineFinderCoreDao coreDao;
  private CompetencyLineFinderDsDao dsDao;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(RescopeCeilingCompetencyLineFinder.class);

  RescopeCeilingCompetencyLineFinder(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public CompetencyLine findCompetencyLineForRescope(RescopeProcessorContext context) {
    this.context = context;
    CompetencyLine resultLine = null;
    if (context.isInClassExperience()) {
      resultLine = fetchCompetencyCeilingLineFromClass();
    }
    if (resultLine == null || resultLine.isEmpty()) {
      // This is also fallback for IL case
      resultLine = fetchCompetencyCeilingLineFromCourse();
    }
    return resultLine;
  }

  private CompetencyLine fetchCompetencyCeilingLineFromCourse() {
    CompetencyFetcher competencyFetcher = CompetencyFetcher.build(dbi4core);
    List<String> destinationGutCodes = competencyFetcher
        .fetchCompetenciesForCourse(context.getCourseId());
    if (destinationGutCodes != null && !destinationGutCodes.isEmpty()) {
      List<Competency> competenciesForSpecifiedCourseAndSubject = fetchDsDao()
          .transformGutCodesToCompetency(
              context.getSubject(), CollectionUtils.convertToSqlArrayOfString(destinationGutCodes));
      if (competenciesForSpecifiedCourseAndSubject != null
          && !competenciesForSpecifiedCourseAndSubject.isEmpty()) {
        CompetencyMap competencyMap = CompetencyMap.build(competenciesForSpecifiedCourseAndSubject);
        CompetencyLine resultLine = competencyMap.getCeilingLine();
        if (!resultLine.isEmpty()) {
          return resultLine;
        }
      }
    }
    LOGGER.warn("No aggregated competencies found for course: '{}'", context.getCourseId());
    throw new IllegalStateException(
        "No aggregated competencies found for course: " + context.getCourseId());
  }

  private CompetencyLine fetchCompetencyCeilingLineFromClass() {
    Long highGradeForClass = fetchCoreDao().fetchGradeUpperBoundForClass(context.getClassId());
    if (highGradeForClass != null) {
      List<Competency> competenciesForGradeAndSubject = fetchDsDao()
          .fetchCompetenciesForGradeHighLine(context.getSubject(), highGradeForClass);
      if (!competenciesForGradeAndSubject.isEmpty()) {
        CompetencyLine resultLine = CompetencyMap.build(competenciesForGradeAndSubject)
            .getCeilingLine();
        if (!resultLine.isEmpty()) {
          return resultLine;
        }
      }
    }
    return CompetencyAlgebraDefaultBuilder.getEmptyCompetencyLine();
  }

  private CompetencyLineFinderCoreDao fetchCoreDao() {
    if (coreDao == null) {
      coreDao = dbi4core.onDemand(CompetencyLineFinderCoreDao.class);
    }
    return coreDao;
  }

  private CompetencyLineFinderDsDao fetchDsDao() {
    if (dsDao == null) {
      dsDao = dbi4ds.onDemand(CompetencyLineFinderDsDao.class);
    }
    return dsDao;
  }
}
