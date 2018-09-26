package org.gooru.rescope.infra.services.core.competencypresencechecker;

import org.gooru.rescope.infra.services.core.algebra.competency.Competency;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyMap;

public interface CompetencyPresenceChecker {

  boolean isCompetencyPresent(Competency competency);

  static CompetencyPresenceChecker buildCompetencyPresenceCheckerFromCompetencyMap(
      CompetencyMap competencyMap) {
    // TODO: Implement this
    return null;
  }

}
