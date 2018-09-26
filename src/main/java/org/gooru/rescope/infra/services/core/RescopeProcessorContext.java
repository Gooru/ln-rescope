package org.gooru.rescope.infra.services.core;

import java.util.Objects;
import java.util.UUID;
import org.gooru.rescope.infra.data.RescopeQueueModel;

public class RescopeProcessorContext {

  private final UUID userId;
  private final UUID courseId;
  private final UUID classId;
  private String subject;

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

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    if (this.subject == null) {
      this.subject = subject;
    } else {
      throw new IllegalStateException(
          "Tried to initialize the subject while it is already initialized");
    }
  }

  @Override
  public String toString() {
    return "RescopeProcessorContext{" +
        "userId=" + userId +
        ", courseId=" + courseId +
        ", classId=" + classId +
        ", subject='" + subject + '\'' +
        '}';
  }
}
