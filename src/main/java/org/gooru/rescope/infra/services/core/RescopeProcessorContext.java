package org.gooru.rescope.infra.services.core;

import org.gooru.rescope.infra.data.RescopeQueueModel;

import java.util.UUID;

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

    public UUID getUserId() {
        return userId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public UUID getClassId() {
        return classId;
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
