package org.gooru.rescope.infra.data.competency;

import java.util.List;

/**
 * This is representation of delta between two competency lines {@link CompetencyLine}. It is represented as a
 * competency path {@link CompetencyPath} per domain. If a domain is present in source but not in destination, there is
 * no route, hence discard that domain. If a domain is present in destination but not in source, assume initial
 * competency for that domain to calculate path. If the domain is present in both, directly calculate path. If directly
 * calculated path is not in progression order (destination is at lower progression than source), create an empty path.
 *
 * @author ashish.
 */
public interface CompetencyRoute {

    /**
     * Get the domains present in this route
     *
     * @return List of domains in no predefined order
     */
    List<DomainCode> getDomains();

    /**
     * Gets the competency path for the specified domain.
     *
     * @param domainCode the domain for which competency path needs to be fetched
     * @return Competency path
     */
    CompetencyPath getPathForDomain(DomainCode domainCode);

    static CompetencyRoute build(CompetencyLine sourceLine, CompetencyLine destinationLine) {
        return new CompetencyRouteImpl(sourceLine, destinationLine);
    }

}
