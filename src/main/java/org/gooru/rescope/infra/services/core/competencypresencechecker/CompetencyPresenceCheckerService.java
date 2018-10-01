package org.gooru.rescope.infra.services.core.competencypresencechecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gooru.rescope.infra.services.core.algebra.competency.Competency;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyMap;
import org.gooru.rescope.infra.services.core.algebra.competency.DomainCode;

/**
 * @author ashish.
 */

class CompetencyPresenceCheckerService implements CompetencyPresenceChecker {

  private final CompetencyMap competencyMap;
  private final Map<String, Object> competencyLookupMap = new HashMap<>();

  CompetencyPresenceCheckerService(CompetencyMap competencyMap) {
    this.competencyMap = competencyMap;
    initialize();
  }

  private void initialize() {
    Object dummy = new Object();
    List<DomainCode> domains = competencyMap.getDomains();
    if (domains != null && !domains.isEmpty()) {
      for (DomainCode domainCode : domains) {
        List<Competency> competenciesForDomain = competencyMap.getCompetenciesForDomain(domainCode);
        if (competenciesForDomain != null && !competenciesForDomain.isEmpty()) {
          for (Competency competency : competenciesForDomain) {
            competencyLookupMap.put(competency.getCompetencyCode().getCode(), dummy);
          }
        }
      }
    }
  }

  @Override
  public boolean isCompetencyPresent(String competency) {
    return competencyLookupMap.containsKey(competency);
  }
}
