package org.gooru.rescope.infra.services.core.algebra.competency;

/**
 * DB representation of Competency
 *
 * @author ashish.
 */
public class CompetencyModel {

  private final SubjectCode subjectCode;
  private final DomainCode domainCode;
  private final CompetencyCode competencyCode;
  private final String competencyName;
  private final String competencyDescription;
  private final String competencyStudentDescription;
  private final Integer sequence;

  public CompetencyModel(SubjectCode subjectCode, DomainCode domainCode,
      CompetencyCode competencyCode,
      String competencyName, String competencyDescription, String competencyStudentDescription,
      Integer sequence) {
    this.subjectCode = subjectCode;
    this.domainCode = domainCode;
    this.competencyCode = competencyCode;
    this.competencyName = competencyName;
    this.competencyDescription = competencyDescription;
    this.competencyStudentDescription = competencyStudentDescription;
    this.sequence = sequence;
  }

  public SubjectCode getSubjectCode() {
    return subjectCode;
  }

  public DomainCode getDomainCode() {
    return domainCode;
  }

  public CompetencyCode getCompetencyCode() {
    return competencyCode;
  }

  public String getCompetencyName() {
    return competencyName;
  }

  public String getCompetencyDescription() {
    return competencyDescription;
  }

  public String getCompetencyStudentDescription() {
    return competencyStudentDescription;
  }

  public Integer getSequence() {
    return sequence;
  }
}
