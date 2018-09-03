package org.gooru.rescope.infra.services.itemfinder;

import java.util.List;

/**
 * @author ashish on 21/5/18.
 */
public class SkippedItemsResponse {

    private List<String> units;
    private List<String> lessons;
    private List<String> assessments;
    private List<String> collections;
    private List<String> assessmentsExternal;
    private List<String> collectionsExternal;

    public List<String> getUnits() {
        return units;
    }

    public void setUnits(List<String> units) {
        this.units = units;
    }

    public List<String> getLessons() {
        return lessons;
    }

    public void setLessons(List<String> lessons) {
        this.lessons = lessons;
    }

    public List<String> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<String> assessments) {
        this.assessments = assessments;
    }

    public List<String> getCollections() {
        return collections;
    }

    public void setCollections(List<String> collections) {
        this.collections = collections;
    }

    public List<String> getAssessmentsExternal() {
        return assessmentsExternal;
    }

    public void setAssessmentsExternal(List<String> assessmentsExternal) {
        this.assessmentsExternal = assessmentsExternal;
    }

    public List<String> getCollectionsExternal() {
        return collectionsExternal;
    }

    public void setCollectionsExternal(List<String> collectionsExternal) {
        this.collectionsExternal = collectionsExternal;
    }
}
