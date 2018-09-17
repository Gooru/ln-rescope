package org.gooru.rescope.infra.data.competency;

import java.util.Objects;

/**
 * @author ashish.
 */
class CompetencyImpl implements Competency {

  private final SubjectCode subjectCode;
  private final DomainCode domainCode;
  private final CompetencyCode competencyCode;
  private final ProgressionLevel progressionLevel;

  CompetencyImpl(SubjectCode subjectCode, DomainCode domainCode, CompetencyCode competencyCode,
      ProgressionLevel progressionLevel) {
    Objects.requireNonNull(subjectCode);
    Objects.requireNonNull(domainCode);
    Objects.requireNonNull(competencyCode);
    Objects.requireNonNull(progressionLevel);

    this.subjectCode = subjectCode;
    this.domainCode = domainCode;
    this.competencyCode = competencyCode;
    this.progressionLevel = progressionLevel;
  }

  @Override
  public SubjectCode getSubject() {
    return subjectCode;
  }

  @Override
  public DomainCode getDomain() {
    return domainCode;
  }

  @Override
  public CompetencyCode getCompetencyCode() {
    return competencyCode;
  }

  @Override
  public ProgressionLevel getProgressionLevel() {
    return progressionLevel;
  }

  @Override
  public boolean belongToSameSubject(Competency competency) {
    if (competency != null) {
      return subjectCode.equals(competency.getSubject());
    }
    return false;
  }

  @Override
  public boolean belongToSamePath(Competency competency) {
    if (competency != null) {
      return subjectCode.equals(competency.getSubject()) && domainCode
          .equals(competency.getDomain());
    }
    return false;
  }

  @Override
  public boolean isPreRequisiteOf(Competency competency) {
    if (belongToSamePath(competency)) {
      return progressionLevel.getProgressionLevel() < competency.getProgressionLevel()
          .getProgressionLevel();
    }
    return false;
  }

  @Override
  public CompetencyPath calculatePathTo(Competency competency) {
    if (belongToSamePath(competency)) {
      return CompetencyPath.build(this, competency);
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CompetencyImpl that = (CompetencyImpl) o;

    if (!subjectCode.equals(that.subjectCode)) {
      return false;
    }
    if (!domainCode.equals(that.domainCode)) {
      return false;
    }
    if (!competencyCode.equals(that.competencyCode)) {
      return false;
    }
    return progressionLevel.equals(that.progressionLevel);
  }

  @Override
  public int hashCode() {
    int result = subjectCode.hashCode();
    result = 31 * result + domainCode.hashCode();
    result = 31 * result + competencyCode.hashCode();
    result = 31 * result + progressionLevel.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Competency{" + "subjectCode=" + subjectCode + ", domainCode=" + domainCode
        + ", competencyCode="
        + competencyCode + ", progressionLevel=" + progressionLevel + '}';
  }
}
