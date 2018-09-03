package org.gooru.rescope.infra.services.itemfinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ashish on 21/5/18.
 */
class SkippedItemsCalculator {

    private final List<ItemModel> allNonDeletedItemsInCourse;
    private final List<ItemModel> allNonSkippedItems;
    private final List<ItemModel> allSkippedItems;
    private List<String> skippedUnits = new ArrayList<>();
    private List<String> skippedLessons = new ArrayList<>();
    private final List<String> skippedCollections = new ArrayList<>();
    private final List<String> skippedAssessments = new ArrayList<>();
    private final List<String> skippedAssessmentsExternal = new ArrayList<>();
    private final List<String> skippedCollectionsExternal = new ArrayList<>();
    private boolean calculated = false;

    SkippedItemsCalculator(List<ItemModel> allNonDeletedItemsInCourse, List<ItemModel> allNonSkippedItems,
        List<ItemModel> allSkippedItems) {
        this.allNonDeletedItemsInCourse = allNonDeletedItemsInCourse;
        this.allNonSkippedItems = allNonSkippedItems;
        this.allSkippedItems = allSkippedItems;
    }

    public List<String> getSkippedUnits() {
        return skippedUnits;
    }

    public List<String> getSkippedLessons() {
        return skippedLessons;
    }

    public List<String> getSkippedCollections() {
        return skippedCollections;
    }

    public List<String> getSkippedAssessments() {
        return skippedAssessments;
    }

    public List<String> getSkippedAssessmentsExternal() {
        return skippedAssessmentsExternal;
    }

    public List<String> getSkippedCollectionsExternal() {
        return skippedCollectionsExternal;
    }

    private void calculateULLevelSkippedItems() {
        Set<String> unitsNotToBeSkipped = new HashSet<>();
        Set<String> allUnits = new HashSet<>();
        Set<String> lessonsNotToBeSkipped = new HashSet<>();
        Set<String> allLessons = new HashSet<>();

        for (ItemModel model : allNonSkippedItems) {
            unitsNotToBeSkipped.add(model.getUnitId());
            lessonsNotToBeSkipped.add(model.getLessonId());
        }

        for (ItemModel model : allNonDeletedItemsInCourse) {
            allUnits.add(model.getUnitId());
            allLessons.add(model.getLessonId());
        }

        // NOTE: After below operation allUnits and allLessons does not point to *all*
        allUnits.removeAll(unitsNotToBeSkipped);
        allLessons.removeAll(lessonsNotToBeSkipped);

        skippedUnits = new ArrayList<>(allUnits);
        skippedLessons = new ArrayList<>(allLessons);
    }

    private void calculateCollectionLevelSkippedItems() {
        for (ItemModel item : allSkippedItems) {
            if (item.isItemAssessment()) {
                skippedAssessments.add(item.getCollectionId());
            } else if (item.isItemCollection()) {
                skippedCollections.add(item.getCollectionId());
            } else if (item.isItemAssessmentExternal()) {
                skippedAssessmentsExternal.add(item.getCollectionId());
            } else if (item.isItemCollectionExternal()) {
                skippedCollectionsExternal.add(item.getCollectionId());
            }
        }
    }

    SkippedItemsCalculator calculate() {
        calculateCollectionLevelSkippedItems();
        calculateULLevelSkippedItems();
        calculated = true;
        return this;
    }

    SkippedItemsResponse getSkippedItemsResponse() {
        if (!calculated) {
            throw new IllegalStateException(
                "Trying to get SkippedItemsResponse from calculator with doing calculation");
        }
        SkippedItemsResponseBuilder builder = new SkippedItemsResponseBuilder(skippedUnits, skippedLessons,
            skippedAssessments, skippedCollections, skippedAssessmentsExternal, skippedCollectionsExternal);
        return builder.build();
    }

}
