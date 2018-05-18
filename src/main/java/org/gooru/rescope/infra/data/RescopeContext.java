package org.gooru.rescope.infra.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author ashish on 18/5/18.
 */
public class RescopeContext {
    private RescopeSourceType source;
    private UUID classId;
    private List<UUID> memberIds;
    private UUID courseId;

    private RescopeContext(RescopeSourceType source, UUID classId, List<UUID> memberIds, UUID courseId) {
        this.source = source;
        this.classId = classId;
        this.memberIds = memberIds;
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        String members = memberIds.stream().map(UUID::toString).collect(Collectors.joining(","));
        return "RescopeContext{" + "source=" + source.getName() + ", classId=" + classId + ", memberIds=" + members
            + ", courseId=" + courseId + '}';
    }

    public static RescopeContext buildForClassJoin(UUID classId, List<UUID> members) {
        return new RescopeContext(RescopeSourceType.ClassJoinByMembers, classId, members, null);
    }

    public static RescopeContext buildForOOB(UUID classId, UUID courseId, UUID memberId) {
        List<UUID> members = new ArrayList<>();
        members.add(memberId);
        return new RescopeContext(RescopeSourceType.OOB, classId, members, courseId);
    }

    public static RescopeContext buildForOOB(UUID classId, UUID courseId, List<UUID> members) {
        return new RescopeContext(RescopeSourceType.OOB, classId, members, courseId);
    }

    public static RescopeContext buildForRescopeSetting(UUID classId) {
        return new RescopeContext(RescopeSourceType.RescopeSettingChanged, classId, Collections.emptyList(), null);
    }

    public static RescopeContext buildForCourseAssignedToClass(UUID classId, UUID courseId) {
        return new RescopeContext(RescopeSourceType.CourseAssignmentToClass, classId, Collections.emptyList(),
            courseId);
    }

    public RescopeContext createNewContext(List<UUID> members) {
        return new RescopeContext(source, classId, members, courseId);
    }

    public RescopeContext createNewContext(List<UUID> members, UUID courseId) {
        return new RescopeContext(source, classId, members, courseId);
    }
}
