package org.gooru.rescope.infra.services.core.algebra.competency;

import java.util.List;

/**
 * This is representation of a line in {@link CompetencyMap}
 * <p>
 * This involves one point in space which is to say, it stores one competency per domain. It could
 * model the highest progression topic covered by content, or lowest progression covered by content
 * or proficiency of learner
 *
 * @author ashish.
 */
public interface CompetencyLine {

  /**
   * Fetch the domains list for the competency lines
   *
   * @return List of domains, not in any predefined order
   */
  List<DomainCode> getDomains();

  /**
   * Fetch the competency for specified domain. For Competency Line single domain should have one
   * competency.
   *
   * @param domainCode Code of the domain for which competency needs to be fetched
   * @return Competency
   */
  Competency getCompetencyForDomain(DomainCode domainCode);

  /**
   * Compute the Competency Route from this Competency line to specified competency line
   *
   * @param competencyLine The destination or target Competency line
   * @return Competency Route
   */
  CompetencyRoute getRouteToCompetencyLine(CompetencyLine competencyLine);

  static CompetencyLine build(CompetencyMap competencyMap, boolean ceiling) {
    return new CompetencyLineImpl(competencyMap, ceiling);
  }
}
