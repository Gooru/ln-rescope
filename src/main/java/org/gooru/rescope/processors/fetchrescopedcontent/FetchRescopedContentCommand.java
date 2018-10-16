package org.gooru.rescope.processors.fetchrescopedcontent;

import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.UUID;
import org.gooru.rescope.infra.constants.HttpConstants;
import org.gooru.rescope.infra.data.EventBusMessage;
import org.gooru.rescope.infra.data.RescopeContext;
import org.gooru.rescope.infra.exceptions.HttpResponseWrapperException;
import org.gooru.rescope.infra.utils.UuidUtils;

/**
 * @author ashish on 17/5/18.
 */
class FetchRescopedContentCommand {

  private static FetchRescopedContentCommand buildFromJsonObject(UUID userId, JsonObject request) {
    FetchRescopedContentCommand command = new FetchRescopedContentCommand();
    try {
      command.classId = validateSingleValuedListAndGetFirstItem(
          UuidUtils.convertToUUIDListIgnoreInvalidItems(
              request.getJsonArray(CommandAttributes.CLASS_ID)));
      command.courseId = validateSingleValuedListAndGetFirstItem(
          UuidUtils.convertToUUIDList(request.getJsonArray(CommandAttributes.COURSE_ID)));
      UUID userInRequestBody = validateSingleValuedListAndGetFirstItem(
          UuidUtils.convertToUUIDList(request.getJsonArray(CommandAttributes.USER_ID)));
      if (userInRequestBody != null) {
        command.userId = userInRequestBody;
        command.teacherId = userId;
      } else {
        command.userId = userId;
        command.teacherId = null;
      }
      return command;
    } catch (IllegalArgumentException e) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  private static <T> T validateSingleValuedListAndGetFirstItem(List<T> inputList) {
    if (inputList == null || inputList.isEmpty()) {
      return null;
    }

    if (inputList.size() > 1) {
      throw new IllegalArgumentException("Single values expected in requested, multiple supplied");
    }

    return inputList.get(0);
  }

  static FetchRescopedContentCommand builder(EventBusMessage input) {
    FetchRescopedContentCommand command = buildFromJsonObject(input.getUserId(),
        input.getRequestBody());
    command.validate();
    return command;
  }

  private UUID classId;
  private UUID courseId;
  private UUID userId;
  private UUID teacherId;

  private void validate() {
    if (userId == null) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid user id");
    }
    if (courseId == null) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid course id");
    }
  }

  UUID getClassId() {
    return classId;
  }

  UUID getCourseId() {
    return courseId;
  }

  UUID getUserId() {
    return userId;
  }

  boolean isTeacherContext() {
    return teacherId != null;
  }

  FetchRescopedContentCommandBean asBean() {
    FetchRescopedContentCommandBean bean = new FetchRescopedContentCommandBean();
    bean.setClassId(classId);
    bean.setCourseId(courseId);
    bean.setUserId(userId);
    bean.setTeacherId(teacherId);
    return bean;
  }

  RescopeContext asRescopeContext() {
    return RescopeContext.build(classId, courseId, userId);
  }

  public static final class FetchRescopedContentCommandBean {

    private UUID classId;
    private UUID courseId;
    private UUID userId;
    private UUID teacherId;

    public UUID getUserId() {
      return userId;
    }

    public void setUserId(UUID userId) {
      this.userId = userId;
    }

    public UUID getClassId() {
      return classId;
    }

    public void setClassId(UUID classId) {
      this.classId = classId;
    }

    public UUID getCourseId() {
      return courseId;
    }

    public void setCourseId(UUID courseId) {
      this.courseId = courseId;
    }

    public UUID getTeacherId() {
      return teacherId;
    }

    public void setTeacherId(UUID teacherId) {
      this.teacherId = teacherId;
    }
  }

  public static final class CommandAttributes {

    static final String CLASS_ID = "classId";
    static final String COURSE_ID = "courseId";
    static final String USER_ID = "userId";

    private CommandAttributes() {
      throw new AssertionError();
    }
  }

}
