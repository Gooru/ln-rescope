package org.gooru.rescope.infra.services.core.competencylinefinder;

import org.gooru.rescope.infra.jdbi.DBICreator;
import org.gooru.rescope.infra.services.core.RescopeProcessorContext;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

public interface CompetencyLineFinder {

  CompetencyLine findCompetencyLineForRescope(RescopeProcessorContext context);

  static CompetencyLineFinder buildCeilingLineFinder(DBI dbi4core, DBI dbi4ds) {
    return new RescopeCeilingCompetencyLineFinder(dbi4core, dbi4ds);
  }

  static CompetencyLineFinder buildFloorLineFinder(DBI dbi4core, DBI dbi4ds) {
    return new RescopeFloorCompetencyLineFinder(dbi4core, dbi4ds);
  }

  static CompetencyLineFinder buildCeilingLineFinder() {
    return new RescopeCeilingCompetencyLineFinder(DBICreator.getDbiForDefaultDS(),
        DBICreator.getDbiForDsdbDS());
  }

  static CompetencyLineFinder buildFloorLineFinder() {
    return new RescopeFloorCompetencyLineFinder(DBICreator.getDbiForDefaultDS(),
        DBICreator.getDbiForDsdbDS());
  }

}
