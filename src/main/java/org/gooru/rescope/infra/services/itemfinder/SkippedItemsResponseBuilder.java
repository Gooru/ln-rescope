package org.gooru.rescope.infra.services.itemfinder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ashish on 21/5/18.
 */
class SkippedItemsResponseBuilder {
    private List<String> units;
    private List<String> lessons;
    private List<String> assessments;
    private List<String> collections;
    private List<String> assessmentsExternal;
    private List<String> collectionsExternal;

    SkippedItemsResponseBuilder(List<String> units, List<String> lessons, List<String> assessments,
            List<String> collections, List<String> assessmentsExternal, List<String> collectionsExternal) {
        this.units = initializeWithEmptyListIfNull(units);
        this.lessons = initializeWithEmptyListIfNull(lessons);
        this.assessments = initializeWithEmptyListIfNull(assessments);
        this.collections = initializeWithEmptyListIfNull(collections);
        this.assessmentsExternal = initializeWithEmptyListIfNull(assessmentsExternal);
        this.collectionsExternal = initializeWithEmptyListIfNull(collectionsExternal);
    }

    public SkippedItemsResponse build() {
        SkippedItemsResponse result = new SkippedItemsResponse();
        result.setUnits(units);
        result.setLessons(lessons);
        result.setCollections(collections);
        result.setAssessments(assessments);
        result.setAssessmentsExternal(assessmentsExternal);
        result.setCollectionsExternal(collectionsExternal);
        return result;
    }

    public SkippedItemsResponseBuilder() {
        this.units = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.assessments = new ArrayList<>();
        this.collections = new ArrayList<>();
        this.assessmentsExternal = new ArrayList<>();
        this.collectionsExternal = new ArrayList<>();
    }

    public void addUnit(String unitId) {
        this.units.add(unitId);
    }

    public void addLesson(String lessonId) {
        this.lessons.add(lessonId);
    }

    public void addCollection(String collectionId) {
        this.collections.add(collectionId);
    }

    public void addAssessment(String assessmentId) {
        this.assessments.add(assessmentId);
    }

    public void addAssessmentExternal(String assessmentExternalId) {
        this.assessmentsExternal.add(assessmentExternalId);
    }

    public void addCollectionExternal(String collectionExternalId) {
        this.collectionsExternal.add(collectionExternalId);
    }

    private static List<String> initializeWithEmptyListIfNull(List<String> input) {
        if (input == null) {
            return new ArrayList<>();
        }
        return input;
    }

}
