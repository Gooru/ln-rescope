package org.gooru.rescope.infra.services.itemfilter;

import java.util.List;
import org.gooru.rescope.infra.services.core.competencypresencechecker.CompetencyPresenceChecker;

/**
 * @author ashish on 21/5/18.
 */
class ItemSkipVerifier {

  private final CompetencyPresenceChecker competencyPresenceChecker;

  ItemSkipVerifier(CompetencyPresenceChecker competencyPresenceChecker) {

    this.competencyPresenceChecker = competencyPresenceChecker;
  }

  boolean canSkip(ItemModel model) {
    List<String> gutCodes = model.getGutCodes();

    if (gutCodes == null || gutCodes.isEmpty()) {
      return false;
    }

    for (String competency : gutCodes) {
      if (competencyPresenceChecker.isCompetencyPresent(competency)) {
        // Break here as user has at least one competency to study
        return false;
      }
    }
    return true;

  }

}
