package org.gooru.rescope.infra.services.core.competencylinefinder;

import org.gooru.rescope.infra.services.core.RescopeProcessorContext;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

public interface CompetencyLineFinder {

  CompetencyLine findCompetencyLineForRescope(RescopeProcessorContext context);

  static CompetencyLineFinder buildCeilingLineFinder(DBI dbi4core, DBI dbi4ds) {
    // TODO: implement this
    return null;
  }

  static CompetencyLineFinder buildFloorLineFinder(DBI dbi4core, DBI dbi4ds) {
    // TODO: implement this
    return null;
  }

  static CompetencyLineFinder buildCeilingLineFinder() {
    // TODO: implement this
    return null;
  }

  static CompetencyLineFinder buildFloorLineFinder() {
    // TODO: implement this
    return null;
  }

}
