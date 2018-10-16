package org.gooru.rescope.infra.data;

import java.util.Objects;
import java.util.UUID;

/**
 * @author ashish on 18/5/18.
 */
public class RescopeContext {

  public static RescopeContext build(UUID classId, UUID courseId, UUID userId) {
    Objects.requireNonNull(courseId);
    Objects.requireNonNull(userId);
    return new RescopeContext(classId, courseId, userId);
  }


  private final UUID classId;
  private final UUID userId;
  private final UUID courseId;

  private RescopeContext(UUID classId, UUID courseId, UUID userId) {
    this.classId = classId;
    this.userId = userId;
    this.courseId = courseId;
  }


  public UUID getClassId() {
    return classId;
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getCourseId() {
    return courseId;
  }

  @Override
  public String toString() {
    return "RescopeContext{" +
        "classId=" + classId +
        ", userId=" + userId +
        ", courseId=" + courseId +
        '}';
  }
}
