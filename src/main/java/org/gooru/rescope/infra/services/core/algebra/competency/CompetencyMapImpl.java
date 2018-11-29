package org.gooru.rescope.infra.services.core.algebra.competency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ashish.
 */
class CompetencyMapImpl implements CompetencyMap {

  private final List<DomainCode> domains;
  private final Map<DomainCode, List<Competency>> domainCodeCompetencyListMap;
  private CompetencyLine ceilingLine, floorLine;

  CompetencyMapImpl(List<Competency> competencies) {
    domainCodeCompetencyListMap = new HashMap<>(competencies.size());

    for (Competency competency : competencies) {
      List<Competency> competenciesForDomain = domainCodeCompetencyListMap
          .get(competency.getDomain());
      if (competenciesForDomain == null) {
        competenciesForDomain = new ArrayList<>();
        competenciesForDomain.add(competency);
        domainCodeCompetencyListMap.put(competency.getDomain(), competenciesForDomain);
      } else {
        competenciesForDomain.add(competency);
      }
    }
    domains = Collections.unmodifiableList(new ArrayList<>(domainCodeCompetencyListMap.keySet()));
    processDomainCompetencyMap();
  }

  private CompetencyMapImpl(List<DomainCode> domains,
      Map<DomainCode, List<Competency>> domainCodeCompetencyListMap) {

    this.domains = domains;
    this.domainCodeCompetencyListMap = domainCodeCompetencyListMap;
  }

  @Override
  public List<DomainCode> getDomains() {
    return Collections.unmodifiableList(domains);
  }

  @Override
  public List<Competency> getCompetenciesForDomain(DomainCode domainCode) {
    return Collections.unmodifiableList(domainCodeCompetencyListMap.get(domainCode));
  }

  @Override
  public CompetencyLine getCeilingLine() {
    if (ceilingLine == null) {
      ceilingLine = CompetencyLine.build(this, true);
    }
    return ceilingLine;
  }

  @Override
  public CompetencyLine getFloorLine() {
    if (floorLine == null) {
      floorLine = CompetencyLine.build(this, false);
    }
    return floorLine;
  }

  @Override
  public CompetencyMap trimAboveCompetencyLine(CompetencyLine competencyLine) {
    return trimWithRespectToCompetencyLine(competencyLine, true, true);
  }

  @Override
  public CompetencyMap trimBelowCompetencyLine(CompetencyLine competencyLine) {
    return trimWithRespectToCompetencyLine(competencyLine, false, false);
  }

  @Override
  public String toString() {
    return "CompetencyMap{" + "domains=" + domains + ", domainCodeCompetencyListMap="
        + domainCodeCompetencyListMap
        + ", ceilingLine=" + ceilingLine + ", floorLine=" + floorLine + '}';
  }

  /*
   * While trimming,
   * - if the domain is missing in competency map but present in competency line,
   * then it is a no-op. As one which does not exists, can't be trimmed.
   * - If the domain is missing in competency line, and is present in competency map, it does not
   * need trimming. Use it as is.
   * - Only if domain is present in both, there would be a case for trimming.
   * Hence, the iteration is needed only on domain of competency maps. Logic won't care if there
   * are additional domains present in competency line.
   */
  private CompetencyMap trimWithRespectToCompetencyLine(CompetencyLine competencyLine,
      boolean trimAbove, boolean skipDomainsNotInCompetencyLine) {
    List<DomainCode> resultDomains = new ArrayList<>(domains.size());
    Map<DomainCode, List<Competency>> resultDomainCodeCompetencyListMap = new HashMap<>(
        domains.size());
    List<DomainCode> domainsInCompetencyLine = competencyLine.getDomains();
    for (DomainCode domainCode : this.domains) {
      List<Competency> competenciesForDomainInCompetencyMap = domainCodeCompetencyListMap
          .get(domainCode);
      Competency competencyFromCompetencyLine = competencyLine.getCompetencyForDomain(domainCode);
      if (domainsInCompetencyLine.contains(domainCode)) {
        List<Competency> trimmedCompetencies;
        ProgressionAwareCompetencyTrimmer trimmer = new ProgressionAwareCompetencyTrimmer(
            competenciesForDomainInCompetencyMap, competencyFromCompetencyLine);
        if (trimAbove) {
          trimmedCompetencies = trimmer.trimAbove();
        } else {
          trimmedCompetencies = trimmer.trimBelow();
        }
        resultDomains.add(domainCode);
        resultDomainCodeCompetencyListMap.put(domainCode, trimmedCompetencies);
      } else {
        if (!skipDomainsNotInCompetencyLine) {
          resultDomains.add(domainCode);
          resultDomainCodeCompetencyListMap.put(domainCode, competenciesForDomainInCompetencyMap);
        }
      }
    }

    return new CompetencyMapImpl(resultDomains, resultDomainCodeCompetencyListMap);
  }

  private void processDomainCompetencyMap() {
    for (DomainCode domainCode : domains) {
      List<Competency> competencies = domainCodeCompetencyListMap.get(domainCode);
      Set<Competency> competencySet = new HashSet<>(competencies);
      competencies = new ArrayList<>(competencySet);
      competencies.sort(new CompetencySorterByProgression());
      domainCodeCompetencyListMap.put(domainCode, competencies);
    }
  }
}
