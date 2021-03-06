package org.gooru.rescope.infra.services.itemfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.gooru.rescope.infra.services.core.competencypresencechecker.CompetencyPresenceChecker;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 21/5/18.
 */
class SkippedItemsFinderInCourseImpl implements SkippedItemsFinder {

  private final DBI dbi;
  private final CompetencyPresenceChecker competencyPresenceChecker;
  private final List<ItemModel> allSkippedItems = new ArrayList<>();
  private final List<ItemModel> allNonSkippedItems = new ArrayList<>();
  private ItemSkipVerifier skipVerifier;
  private List<ItemModel> allNonDeletedItemsInCourse;

  SkippedItemsFinderInCourseImpl(DBI dbi,
      CompetencyPresenceChecker competencyPresenceChecker) {
    this.dbi = dbi;
    this.competencyPresenceChecker = competencyPresenceChecker;
  }

  @Override
  public SkippedItemsResponse findItemsThatWillBeSkipped(UUID userId, UUID courseId) {
    SkippedItemsFinderDao dao = dbi.onDemand(SkippedItemsFinderDao.class);
    skipVerifier = new ItemSkipVerifier(competencyPresenceChecker);

    allNonDeletedItemsInCourse = dao.fetchAllItemsFromCourseWithGutCodes(courseId);

    for (ItemModel item : allNonDeletedItemsInCourse) {
      if (canItemBeSkipped(item)) {
        allSkippedItems.add(item);
      } else {
        allNonSkippedItems.add(item);
      }
    }

    return createResponse();
  }

  private boolean canItemBeSkipped(ItemModel item) {
    return skipVerifier.canSkip(item);
  }

  private SkippedItemsResponse createResponse() {
    if (allSkippedItems.isEmpty()) {
      return new SkippedItemsResponseBuilder().build();
    }
    return new SkippedItemsCalculator(allNonDeletedItemsInCourse, allNonSkippedItems,
        allSkippedItems).calculate()
        .getSkippedItemsResponse();
  }

}
