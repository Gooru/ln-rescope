package org.gooru.rescope.infra.services.itemfilter;

import java.util.List;
import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 21/5/18.
 */
interface SkippedItemsFinderDao {

  @Mapper(ItemModel.ItemModelMapper.class)
  @SqlQuery(
      "select course_id, unit_id, lesson_id, id, format, gut_codes from collection where course_id = :courseId "
          + " and is_deleted = false order by course_id, unit_id, lesson_id")
  List<ItemModel> fetchAllItemsFromCourseWithGutCodes(@Bind("courseId") UUID courseId);

}
