package org.gooru.rescope.infra.services.core;

import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyLine;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyMap;
import org.gooru.rescope.infra.services.core.competencylinefinder.CompetencyLineFinder;
import org.gooru.rescope.infra.services.core.competencymapcreator.CompetencyMapCreator;
import org.gooru.rescope.infra.services.core.competencypresencechecker.CompetencyPresenceChecker;
import org.gooru.rescope.infra.services.core.subjectinferer.SubjectInferer;
import org.gooru.rescope.infra.services.core.validators.RescopeProcessorContextValidator;
import org.gooru.rescope.infra.services.itemfilter.SkippedItemsFinder;
import org.gooru.rescope.infra.services.itemfilter.SkippedItemsResponse;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class RescopeProcessorImpl implements RescopeProcessor {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private RescopeProcessorContext context;

  RescopeProcessorImpl(DBI dbi4core, DBI dbi4ds) {

    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public SkippedItemsResponse rescopedItems(RescopeProcessorContext context) {
    this.context = context;

    validate();
    initializeSubject();
    return findItemsThatWillBeSkipped();
  }

  private void validate() {
    RescopeProcessorContextValidator.build(dbi4core, dbi4ds).validate(context);
  }

  private SkippedItemsResponse findItemsThatWillBeSkipped() {
    CompetencyLine ceilingCompetencyLine = CompetencyLineFinder
        .buildCeilingLineFinder(dbi4core, dbi4ds).findCompetencyLineForRescope(context);
    CompetencyLine floorCompetencyLine = CompetencyLineFinder.buildFloorLineFinder(dbi4core, dbi4ds)
        .findCompetencyLineForRescope(context);
    CompetencyMap competencyMapForSubject = CompetencyMapCreator
        .buildSubjectCompetencyMapCreator(dbi4ds)
        .create(context);
    CompetencyMap trimmedCompetencyMapAboveCeiling = competencyMapForSubject
        .trimAboveCompetencyLine(ceilingCompetencyLine);
    CompetencyMap adjustedStudyRouteForUser = trimmedCompetencyMapAboveCeiling
        .trimBelowCompetencyLine(floorCompetencyLine);
    CompetencyPresenceChecker competencyPresenceChecker = CompetencyPresenceChecker
        .buildCompetencyPresenceCheckerFromCompetencyMap(adjustedStudyRouteForUser);
    return SkippedItemsFinder.buildSkippedItemsFinderForCourse(dbi4core, competencyPresenceChecker)
        .findItemsThatWillBeSkipped(context.getUserId(), context.getCourseId());

  }

  private void initializeSubject() {
    String subject = SubjectInferer.build(dbi4core).inferSubjectForCourse(context.getCourseId());
    context.setSubject(subject);
  }

}
