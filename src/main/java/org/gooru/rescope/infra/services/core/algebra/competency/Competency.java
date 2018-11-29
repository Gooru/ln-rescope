package org.gooru.rescope.infra.services.core.algebra.competency;

import org.gooru.rescope.infra.constants.Constants;

/**
 * This is the model which encapsulates the Domain model of Competency (which is different from DB
 * model). Currently taxonomy courses are not considered in model. Also note that this does not
 * encapsulate the enriched model to provide more information like competency title, domain name
 * etc.
 *
 * @author ashish.
 */
public interface Competency {

  /**
   * The subject code to which the competency belongs
   */
  SubjectCode getSubject();

  /**
   * The domain to which the competency belongs
   */
  DomainCode getDomain();

  /**
   * The competency code for this competency
   */
  CompetencyCode getCompetencyCode();

  /**
   * The progression level of this competency
   */
  ProgressionLevel getProgressionLevel();

  /**
   * Do this competency and specified one are from same subject
   */
  boolean belongToSameSubject(Competency competency);

  /**
   * Do this competency and specified one belong to same path. This would mean that they need to
   * belong to same subject and same domain
   */
  boolean belongToSamePath(Competency competency);

  /**
   * Is this competency a pre requisite of specified competency. It implies that as a pre condition,
   * both competencies should belong to same path
   */
  boolean isPreRequisiteOf(Competency competency);

  /**
   * Get {@link CompetencyPath} between this and specified competency
   */
  CompetencyPath calculatePathTo(Competency competency);

  static Competency build(String subject, String domain, String competencyCode, int progression) {
    return new CompetencyImpl(new SubjectCode(subject), new DomainCode(domain),
        new CompetencyCode(competencyCode),
        new ProgressionLevel(progression));
  }

  static Competency buildInitialCompetency(String subject, String domain) {
    return new CompetencyImpl(new SubjectCode(subject), new DomainCode(domain),
        new CompetencyCode(Constants.Misc.COMPETENCY_PLACEHOLDER), new ProgressionLevel(0));
  }

  static Competency buildInitialCompetency(SubjectCode subject, DomainCode domain) {
    return new CompetencyImpl(subject, domain,
        new CompetencyCode(Constants.Misc.COMPETENCY_PLACEHOLDER),
        new ProgressionLevel(0));
  }

}
