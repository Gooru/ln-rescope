package org.gooru.rescope.infra.data.competency;

/**
 * DB representation for the domain
 *
 * @author ashish.
 */
public class DomainModel {

    private final SubjectCode subjectCode;
    private final DomainCode domainCode;
    private final String domainName;
    private final Integer sequence;

    public DomainModel(SubjectCode subjectCode, DomainCode domainCode, String domainName, Integer sequence) {
        this.subjectCode = subjectCode;
        this.domainCode = domainCode;
        this.domainName = domainName;
        this.sequence = sequence;
    }

    public SubjectCode getSubjectCode() {
        return subjectCode;
    }

    public DomainCode getDomainCode() {
        return domainCode;
    }

    public String getDomainName() {
        return domainName;
    }

    public Integer getSequence() {
        return sequence;
    }
}
