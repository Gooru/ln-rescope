package org.gooru.rescope.infra.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.gooru.rescope.infra.utils.UuidUtils;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 19/5/18.
 */
public class RescopeQueueModel {

    public static final int RQ_STATUS_QUEUED = 0;
    public static final int RQ_STATUS_DISPATCHED = 1;
    public static final int RQ_STATUS_INPROCESS = 2;

    public static RescopeQueueModel fromJson(String input) {
        JsonObject json = new JsonObject(input);
        RescopeQueueModel model = new RescopeQueueModel();
        model.priority = json.getInteger("priority");
        model.status = json.getInteger("status");
        model.classId = UuidUtils.convertStringToUuid(json.getString("classId"));
        model.courseId = UuidUtils.convertStringToUuid(json.getString("courseId"));
        model.userId = UuidUtils.convertStringToUuid(json.getString("userId"));
        model.id = json.getLong("id");
        return model;
    }

    public static RescopeQueueModel fromRescopeContextNoMembers(RescopeContext context) {
        RescopeQueueModel result = new RescopeQueueModel();
        result.courseId = context.getCourseId();
        result.classId = context.getClassId();
        result.status = RQ_STATUS_QUEUED;
        result.priority = context.getSource().getOrder();
        return result;
    }

    public static RescopeQueueModel createNonPersistedEmptyModel() {
        return new RescopeQueueModel();
    }

    private Long id;
    private UUID userId;
    private UUID courseId;
    private UUID classId;
    private int priority;
    private int status;

    public String toJson() {
        return new JsonObject().put("id", id).put("userId", UuidUtils.uuidToString(userId))
            .put("courseId", UuidUtils.uuidToString(courseId)).put("classId", UuidUtils.uuidToString(classId))
            .put("priority", priority).put("status", status).toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isModelPersisted() {
        return id != null;
    }

    public static class RescopeQueueModelMapper implements ResultSetMapper<RescopeQueueModel> {

        @Override
        public RescopeQueueModel map(final int index, final ResultSet resultSet,
            final StatementContext statementContext) throws SQLException {
            RescopeQueueModel model = new RescopeQueueModel();
            model.setId(resultSet.getLong("id"));
            model.setPriority(resultSet.getInt("priority"));
            model.setStatus(resultSet.getInt("status"));
            model.setUserId(UuidUtils.convertStringToUuid(resultSet.getString("user_id")));
            model.setCourseId(UuidUtils.convertStringToUuid(resultSet.getString("course_id")));
            model.setClassId(UuidUtils.convertStringToUuid(resultSet.getString("class_id")));
            return model;
        }

    }

}
