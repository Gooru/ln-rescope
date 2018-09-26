package org.gooru.rescope.infra.services.core.competencylinefinder;

import java.util.List;
import org.gooru.rescope.infra.jdbi.PGArray;
import org.gooru.rescope.infra.services.core.algebra.competency.Competency;
import org.gooru.rescope.infra.services.core.algebra.competency.mappers.CompetencyMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

interface CompetencyLineFinderDsDao {

  @Mapper(CompetencyMapper.class)
  @SqlQuery(
      "SELECT dcmt.tx_subject_code, dcmt.tx_domain_code, dcmt.tx_comp_code, dcmt.tx_comp_seq FROM   "
          + " domain_competency_matrix dcmt WHERE  dcmt.tx_subject_code = :subjectCode AND "
          + " dcmt.tx_comp_code = any(select highline_tx_comp_code from grade_competency_bound where "
          + " grade_id = :gradeId and tx_subject_code = :subjectCode)")
  List<Competency> fetchCompetenciesForGradeHighLine(@Bind("subjectCode") String subjectCode,
      @Bind("gradeId") Long gradeId);

  @Mapper(CompetencyMapper.class)
  @SqlQuery(
      "SELECT dcmt.tx_subject_code, dcmt.tx_domain_code, dcmt.tx_comp_code, dcmt.tx_comp_seq FROM   "
          + " domain_competency_matrix dcmt WHERE  dcmt.tx_subject_code = :subjectCode AND "
          + " dcmt.tx_comp_code = any(:gutCodes)")
  List<Competency> transformGutCodesToCompetency(@Bind("subjectCode") String subjectCode,
      @Bind("gutCodes") PGArray<String> gutCodes);

  @Mapper(CompetencyMapper.class)
  @SqlQuery(
      "SELECT dcmt.tx_subject_code, dcmt.tx_domain_code, dcmt.tx_comp_code, dcmt.tx_comp_seq FROM   "
          + " domain_competency_matrix dcmt WHERE  dcmt.tx_subject_code = :subjectCode AND "
          + " dcmt.tx_comp_code = any(select gut_code from baseline_learner_profile where "
          + " tx_subject_code = :subjectCode and class_id = :classId and course_id = :courseId "
          + " and user_id = :userId)")
  List<Competency> fetchCompetenciesForUserBaselineLPInClass(
      @Bind("subjectCode") String subjectCode,
      @Bind("classId") String classId,
      @Bind("courseId") String courseId,
      @Bind("userId") String userId);


  @Mapper(CompetencyMapper.class)
  @SqlQuery(
      "SELECT dcmt.tx_subject_code, dcmt.tx_domain_code, dcmt.tx_comp_code, dcmt.tx_comp_seq FROM   "
          + " domain_competency_matrix dcmt WHERE  dcmt.tx_subject_code = :subjectCode AND "
          + " dcmt.tx_comp_code = any(select gut_code from baseline_learner_profile where "
          + " tx_subject_code = :subjectCode and class_id is null and course_id = :courseId "
          + " and user_id = :userId)")
  List<Competency> fetchCompetenciesForUserBaselineLPInIL(
      @Bind("subjectCode") String subjectCode,
      @Bind("courseId") String courseId,
      @Bind("userId") String userId);

}
