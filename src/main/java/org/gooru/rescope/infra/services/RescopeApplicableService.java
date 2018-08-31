package org.gooru.rescope.infra.services;

import java.util.UUID;

import org.gooru.rescope.infra.components.AppConfiguration;
import org.gooru.rescope.infra.jdbi.DBICreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 17/5/18.
 */
public final class RescopeApplicableService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RescopeApplicableService.class);

    public static boolean isRescopeApplicableToClass(UUID classId) {
        RescopeApplicableDao dao = getRescopeApplicableDao();
        String courseId = dao.fetchCourseForClass(classId);
        if (courseId == null) {
            LOGGER.info("Course is not assigned to class '{}' hence rescope not applicable", classId.toString());
            return false;
        }
        return AppConfiguration.getInstance().getRescopeApplicableCourseVersion()
                .equals(dao.fetchCourseVersion(courseId));
    }

    public static boolean isRescopeApplicableToCourseInIL(UUID courseId) {
        RescopeApplicableDao dao = getRescopeApplicableDao();
        return AppConfiguration.getInstance().getRescopeApplicableCourseVersion()
                .equals(dao.fetchCourseVersion(courseId));
    }

    private static RescopeApplicableDao getRescopeApplicableDao() {
        return DBICreator.getDbiForDefaultDS().onDemand(RescopeApplicableDao.class);
    }
}
