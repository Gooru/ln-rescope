package org.gooru.rescope.infra.services.core.competencypresencechecker;

import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyMap;

public interface CompetencyPresenceChecker {

  boolean isCompetencyPresent(String competency);

  static CompetencyPresenceChecker buildCompetencyPresenceCheckerFromCompetencyMap(
      CompetencyMap competencyMap) {
    return new CompetencyPresenceCheckerService(competencyMap);
  }

}
