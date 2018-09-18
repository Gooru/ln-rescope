package org.gooru.rescope.infra.services.core.validators;

import org.gooru.rescope.infra.services.core.RescopeProcessorContext;
import org.gooru.rescope.infra.services.core.subjectinferer.SubjectInferer;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The validator works on basis of whether it is "In Class" or IL experience.
 *
 * In case of "In Class" experience, validator validates the following:
 * <ul>
 * <li>Class is not deleted</li>
 * <li>Specified course is not deleted and is attached to specified class</li>
 * <li>Specified user is member of specified class</li>
 * </ul>
 *
 * In case of IL, the validation is:
 * <ul>
 * <li>The specified course is not deleted in system</li>
 * </ul>
 *
 * In both cases, the common validations are:
 * <ul>
 * <li>The course is tagged to a subject bucket</li>
 * <li>The user has baselined learner profile for specified context</li>
 * </ul>
 *
 * @author ashish.
 */

class RescopeProcessorContextValidatorImpl implements RescopeProcessorContextValidator {

  private final DBI dbi4core;
  private DBI dbi4ds;
  private static final Logger LOGGER = LoggerFactory.getLogger(RescopeProcessorContext.class);
  private RescopeProcessorContextCoreValidatorDao dao4core = null;
  private RescopeProcessorContextDsValidatorDao dao4ds = null;
  private RescopeProcessorContext context;

  RescopeProcessorContextValidatorImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public void validate(RescopeProcessorContext context) {
    this.context = context;

    if (context.isInClassExperience()) {
      validateInClass();
    } else {
      validateForIL();
    }
  }

  private void validateForIL() {
    if (!getDao4Core().validateCourseExists(context.getCourseId())) {
      LOGGER.warn("Course: '{}' does not exist", context.getCourseId());
      throw new IllegalStateException("Course does not exist: " + context.getCourseId());
    }
    validateCommon();
  }

  private void validateInClass() {
    if (!getDao4Core().validateClassCourseUserCombo(context.getClassId(), context.getCourseId(),
        context.getUserId())) {
      LOGGER.warn("Course: '{}'; Class: '{}'; User: '{}' combination validation check failed",
          context.getCourseId(), context.getClassId(), context.getUserId());
      throw new IllegalStateException(
          "Course/Class/Member combination validation failed. Course: " + context.getCourseId()
              + ", Class: " + context.getClassId() + ", User: " + context.getUserId());
    }
    validateCommon();
  }

  private void validateCommon() {
    validatePresenceOfBaselinedLPForUser();
  }

  private void validatePresenceOfBaselinedLPForUser() {
    String subjectBucket = SubjectInferer.build(dbi4core)
        .inferSubjectForCourse(context.getCourseId());
    if (subjectBucket == null || subjectBucket.trim().isEmpty()) {
      LOGGER.warn("Subject bucket is not present or is empty for course: '{}'",
          context.getCourseId());
      throw new IllegalStateException("Subject bucket is not present or is empty for course: " +
          context.getCourseId());
    }
    if (context.isInClassExperience()) {
      validatePresenceOfBaselinedLPForUserInClass(subjectBucket);
    } else {
      validatePresenceOfBaselinedLPForUserInIL(subjectBucket);
    }

  }

  private void validatePresenceOfBaselinedLPForUserInIL(String subjectBucket) {
    if (!getDao4Ds().validateLPBaselinePresenceForIL(context.getUserId().toString(),
        context.getCourseId().toString(), subjectBucket)) {
      String contextString = context.toString();
      LOGGER.warn("LP baseline not present for context: '{}'", contextString);
      throw new IllegalStateException(
          "LP baseline not present for context : " + contextString);
    }
  }

  private void validatePresenceOfBaselinedLPForUserInClass(String subjectBucket) {
    if (!getDao4Ds().validateLPBaselinePresenceInClass(context.getUserId().toString(),
        context.getCourseId().toString(), context.getClassId().toString(), subjectBucket)) {
      String contextString = context.toString();
      LOGGER.warn("LP baseline not present for context: '{}'", contextString);
      throw new IllegalStateException(
          "LP baseline not present for context : " + contextString);
    }
  }

  private RescopeProcessorContextCoreValidatorDao getDao4Core() {
    if (dao4core == null) {
      dao4core = dbi4core.onDemand(RescopeProcessorContextCoreValidatorDao.class);
    }
    return dao4core;
  }

  private RescopeProcessorContextDsValidatorDao getDao4Ds() {
    if (dao4ds == null) {
      dao4ds = dbi4ds.onDemand(RescopeProcessorContextDsValidatorDao.class);
    }
    return dao4ds;
  }

}
