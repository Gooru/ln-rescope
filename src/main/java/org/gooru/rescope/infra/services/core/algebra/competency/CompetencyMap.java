package org.gooru.rescope.infra.services.core.algebra.competency;

import java.util.List;

/**
 * This is representation of competency in 2D space arranged by domains and second dimension being
 * progression.
 * <p>
 * There could be multiple competencies in a domain. Note that this could be sparse, and may not
 * contain all the domains
 *
 * @author ashish.
 */
public interface CompetencyMap {

  /**
   * Fetch all the domains in this competency map
   *
   * @return List of domains, in no specified order
   */
  List<DomainCode> getDomains();

  /**
   * Fetch all the competencies within specified domain in this competency map.
   *
   * @param domainCode the domain for which competencies need to be fetched
   * @return List of competencies, in order of progression which is lowest first
   */
  List<Competency> getCompetenciesForDomain(DomainCode domainCode);

  /**
   * Calculate a skyline for a given competency map
   *
   * @return competency line which denotes the skyline
   */
  CompetencyLine getCeilingLine();

  /**
   * Calculate an earthline for a given competency map
   *
   * @return competency line which denotes the earthline
   */
  CompetencyLine getFloorLine();

  /**
   * Trim the provided competency map above the provided competency line
   *
   * Note that if a domain is absent in competency line, that domain is represented as is from
   * source competency map into result competency map. The competency line is non inclusive in
   * trimming
   *
   * @return New competency map which is result of trim
   */
  CompetencyMap trimAboveCompetencyLine(CompetencyLine competencyLine);

  /**
   * Trim the provided competency map below the provided competency line
   *
   * Note that if a domain is absent in competency line, that domain is is represented as is from
   * source competency map into result competency map. The competency line is non inclusive in
   * trimming
   *
   * @return New competency map which is result of trim
   */
  CompetencyMap trimBelowCompetencyLine(CompetencyLine competencyLine);


  static CompetencyMap build(List<Competency> competencies) {
    return new CompetencyMapImpl(competencies);
  }
}
