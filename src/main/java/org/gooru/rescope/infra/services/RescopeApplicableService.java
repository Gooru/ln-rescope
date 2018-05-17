package org.gooru.rescope.infra.services;

import java.util.UUID;

import org.gooru.rescope.infra.jdbi.DBICreator;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 17/5/18.
 */
public final class RescopeApplicableService {

    private static final String RESCOPE_SETTING_KEY = "rescope";

    public static boolean isRescopeApplicableToClass(UUID classId) {
        RescopeApplicableDao dao = getRescopeApplicableDao();
        String setting = dao.fetchClassSetting(classId);
        if (setting == null) {
            throw new IllegalStateException("Class setting should not be null");
        }
        JsonObject jsonSetting = new JsonObject(setting);
        return Boolean.TRUE.equals(jsonSetting.getBoolean(RESCOPE_SETTING_KEY));
    }

    public static boolean isRescopeApplicableToCourseInIL(UUID courseId) {
        RescopeApplicableDao dao = getRescopeApplicableDao();
        return (dao.fetchCourseVersion(courseId) != null);
    }

    private static RescopeApplicableDao getRescopeApplicableDao() {
        return DBICreator.getDbiForDefaultDS().onDemand(RescopeApplicableDao.class);
    }
}
