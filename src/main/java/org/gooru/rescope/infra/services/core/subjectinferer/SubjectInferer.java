package org.gooru.rescope.infra.services.core.subjectinferer;

import java.util.UUID;
import org.gooru.rescope.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */
public interface SubjectInferer {

  String inferSubjectForCourse(UUID courseId);

  static SubjectInferer build() {
    return new SubjectInfererImpl(DBICreator.getDbiForDefaultDS());
  }

  static SubjectInferer build(DBI dbi) {
    return new SubjectInfererImpl(dbi);
  }
}
