package org.gooru.rescope.processors.dorescopeofcontent;

import io.vertx.core.json.JsonObject;
import java.util.UUID;
import org.gooru.rescope.infra.constants.Constants.Message;
import org.gooru.rescope.infra.data.RescopeContext;
import org.gooru.rescope.infra.utils.UuidUtils;

/**
 * @author ashish on 18/5/18.
 */
class DoRescopeOfContentCommand {

  static DoRescopeOfContentCommand builder(JsonObject messageBody) {
    JsonObject requestBody = messageBody.getJsonObject(Message.MSG_HTTP_BODY);
    DoRescopeOfContentCommand result = new DoRescopeOfContentCommand();
    result.classId = UuidUtils
        .convertStringToUuid(requestBody.getString(CommandAttributes.CLASS_ID));
    result.userId = UuidUtils
        .convertStringToUuid(requestBody.getString(CommandAttributes.USER_ID));
    result.courseId = UuidUtils
        .convertStringToUuid(requestBody.getString(CommandAttributes.COURSE_ID));
    result.validate();
    return result;
  }

  private UUID classId;
  private UUID userId;
  private UUID courseId;

  private DoRescopeOfContentCommand() {

  }

  UUID getCourseId() {
    return courseId;
  }

  UUID getClassId() {
    return classId;
  }

  public UUID getUserId() {
    return userId;
  }

  RescopeContext asRescopeContext() {
    return RescopeContext.build(classId, courseId, userId);
  }

  private void validate() {
    if (classId == null && courseId == null) {
      throw new IllegalArgumentException("Both class and course should not be absent");
    }
    if (userId == null) {
      throw new IllegalArgumentException("User should not be absent");
    }
  }

  final class CommandAttributes {

    static final String COURSE_ID = "courseId";
    static final String CLASS_ID = "classId";
    static final String USER_ID = "userId";

    private CommandAttributes() {
      throw new AssertionError();
    }
  }
}
