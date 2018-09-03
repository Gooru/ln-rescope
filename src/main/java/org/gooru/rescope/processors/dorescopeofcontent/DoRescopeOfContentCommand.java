package org.gooru.rescope.processors.dorescopeofcontent;

import java.util.List;
import java.util.UUID;

import org.gooru.rescope.infra.data.RescopeContext;
import org.gooru.rescope.infra.data.RescopeSourceType;
import org.gooru.rescope.infra.utils.UuidUtils;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 18/5/18.
 */
class DoRescopeOfContentCommand {

    static DoRescopeOfContentCommand builder(JsonObject requestBody) {
        DoRescopeOfContentCommand result = new DoRescopeOfContentCommand();
        result.classId = UuidUtils.convertStringToUuid(requestBody.getString(CommandAttributes.CLASS_ID));
        result.source = RescopeSourceType.builder(requestBody.getString(CommandAttributes.SOURCE));
        result.memberIds = UuidUtils.convertToUUIDList(requestBody.getJsonArray(CommandAttributes.MEMBER_IDS));
        result.courseId = UuidUtils.convertStringToUuid(requestBody.getString(CommandAttributes.COURSE_ID));
        result.validate();
        return result;
    }

    private RescopeSourceType source;
    private UUID classId;
    private List<UUID> memberIds;
    private UUID courseId;

    private DoRescopeOfContentCommand() {

    }

    public UUID getCourseId() {
        return courseId;
    }

    public RescopeSourceType getSource() {
        return source;
    }

    public UUID getClassId() {
        return classId;
    }

    public List<UUID> getMemberIds() {
        return memberIds;
    }

    public RescopeContext asRescopeContext() {
        switch (source) {
        case ClassJoinByMembers:
            return RescopeContext.buildForClassJoin(classId, memberIds);
        case RescopeSettingChanged:
            return RescopeContext.buildForRescopeSetting(classId);
        case CourseAssignmentToClass:
            return RescopeContext.buildForCourseAssignedToClass(classId, courseId);
        case OOB:
            return RescopeContext.buildForOOB(classId, courseId, memberIds);
        default:
            throw new IllegalStateException("Invalid rescope source type");
        }
    }

    private void validate() {
        if (classId == null && courseId == null) {
            throw new IllegalArgumentException("Both class and course should not be absent");
        }
        if (source == null) {
            throw new IllegalArgumentException("Invalid source");
        }
        if (((memberIds == null || memberIds.isEmpty())
            && (source == RescopeSourceType.OOB || source == RescopeSourceType.ClassJoinByMembers))
            || (memberIds != null && !memberIds.isEmpty() && source != RescopeSourceType.OOB
            && source != RescopeSourceType.ClassJoinByMembers)) {
            throw new IllegalArgumentException("Members should be provided only for OOB/class join type rescope");
        }
    }

    final class CommandAttributes {

        public static final String COURSE_ID = "courseId";
        static final String SOURCE = "source";
        static final String CLASS_ID = "classId";
        static final String MEMBER_IDS = "memberIds";

        private CommandAttributes() {
            throw new AssertionError();
        }
    }
}
