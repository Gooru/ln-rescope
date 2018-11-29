package org.gooru.rescope.infra.services.itemfilter;

import java.util.UUID;
import org.gooru.rescope.infra.jdbi.DBICreator;
import org.gooru.rescope.infra.services.core.competencypresencechecker.CompetencyPresenceChecker;
import org.skife.jdbi.v2.DBI;

/**
 * This is the entry point for the flow to obtain the skipped items in given context. The context
 * could be course, unit or lesson.
 * <p>
 * Right now we only support context of course. When other contexts are needed, we need to provide
 * extensions in form of alternate implementations of this interface.
 *
 * @author ashish on 21/5/18.
 */
public interface SkippedItemsFinder {

  SkippedItemsResponse findItemsThatWillBeSkipped(UUID userId, UUID courseId);

  static SkippedItemsFinder buildSkippedItemsFinderForCourse(
      CompetencyPresenceChecker competencyPresenceChecker) {
    return new SkippedItemsFinderInCourseImpl(DBICreator.getDbiForDefaultDS(),
        competencyPresenceChecker);
  }

  static SkippedItemsFinder buildSkippedItemsFinderForCourse(DBI dbi,
      CompetencyPresenceChecker competencyPresenceChecker) {

    return new SkippedItemsFinderInCourseImpl(dbi, competencyPresenceChecker);
  }


}
