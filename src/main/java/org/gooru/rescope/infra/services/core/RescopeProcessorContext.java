package org.gooru.rescope.infra.services.core;

import java.util.Objects;
import java.util.UUID;
import org.gooru.rescope.infra.data.RescopeQueueModel;

public class RescopeProcessorContext {

  private final UUID userId;
  private final UUID courseId;
  private final UUID classId;

  private RescopeProcessorContext(UUID userId, UUID courseId, UUID classId) {
    this.userId = userId;
    this.courseId = courseId;
    this.classId = classId;
  }


  public static RescopeProcessorContext buildFromRescopeQueueModel(RescopeQueueModel model) {
    return new RescopeProcessorContext(model.getUserId(), model.getCourseId(), model.getClassId());
  }

  public static RescopeProcessorContext build(UUID userId, UUID courseId, UUID classId) {
    Objects.requireNonNull(userId, "User id should not be null");
    Objects.requireNonNull(courseId, "Course id should not be null");
    return new RescopeProcessorContext(userId, courseId, classId);
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getCourseId() {
    return courseId;
  }

  public UUID getClassId() {
    return classId;
  }

  public boolean isInClassExperience() {
    return classId != null;
  }

  public boolean isILExperience() {
    return classId == null;
  }

  @Override
  public String toString() {
    return "RescopeProcessorContext{" +
        "userId=" + userId +
        ", courseId=" + courseId +
        ", classId=" + classId +
        '}';
  }
}
