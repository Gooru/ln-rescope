package org.gooru.rescope.infra.data;

import java.util.UUID;

/**
 * @author ashish on 19/5/18.
 */
public class RescopeQueueModel {
    Integer id;
    UUID userId;
    UUID courseId;
    UUID classId;
    int priority;
    int status;

    public static final int RQ_STATUS_QUEUED = 0;
    public static final int RQ_STATUS_DISPATCHED = 1;
    public static final int RQ_STATUS_INPROCESS = 2;

    public static RescopeQueueModel fromRescopeContextNoMembers(RescopeContext context) {
        RescopeQueueModel result = new RescopeQueueModel();
        result.courseId = context.getCourseId();
        result.classId = context.getClassId();
        result.status = RQ_STATUS_QUEUED;
        result.priority = context.getSource().getOrder();
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public UUID getClassId() {
        return classId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
