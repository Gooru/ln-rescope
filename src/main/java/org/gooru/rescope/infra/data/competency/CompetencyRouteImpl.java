package org.gooru.rescope.infra.data.competency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ashish.
 */
class CompetencyRouteImpl implements CompetencyRoute {

    private final CompetencyLine sourceLine;
    private final List<DomainCode> domains;
    private final Map<DomainCode, CompetencyPath> domainCodeCompetencyPathMap;
    private final CompetencyLine destinationLine;

    CompetencyRouteImpl(CompetencyLine sourceLine, CompetencyLine destinationLine) {
        this.sourceLine = sourceLine;
        this.destinationLine = destinationLine;
        // While calculating path, if the domain is present in source but not in destination, do not consider that
        // domain. If the domain is present in destination but not in source, then work off with initial competency
        // for that domain. If domain is present in both lines, directly calculate.
        domains = Collections.unmodifiableList(new ArrayList<>(destinationLine.getDomains()));
        domainCodeCompetencyPathMap = calculateCompetencyPathsForDestinationDomains();
    }

    @Override
    public List<DomainCode> getDomains() {
        return Collections.unmodifiableList(domains);
    }

    @Override
    public CompetencyPath getPathForDomain(DomainCode domainCode) {
        if (domainCode != null) {
            return domainCodeCompetencyPathMap.get(domainCode);
        }
        return null;
    }

    @Override
    public String toString() {
        return "CompetencyRoute{" + "sourceLine=" + sourceLine + ", domains=" + domains
            + ", domainCodeCompetencyPathMap=" + domainCodeCompetencyPathMap + ", destinationLine=" + destinationLine
            + '}';
    }

    private Map<DomainCode, CompetencyPath> calculateCompetencyPathsForDestinationDomains() {
        Map<DomainCode, CompetencyPath> result = new HashMap<>();
        for (DomainCode domainCode : domains) {
            Competency destinationCompetency = destinationLine.getCompetencyForDomain(domainCode);
            Competency sourceCompetency = sourceLine.getCompetencyForDomain(domainCode);
            if (sourceCompetency == null) {
                sourceCompetency = Competency
                    .buildInitialCompetency(destinationCompetency.getSubject(), destinationCompetency.getDomain());
            }
            CompetencyPath path = CompetencyPath.build(sourceCompetency, destinationCompetency);
            result.put(domainCode, path);
        }
        return result;
    }
}
