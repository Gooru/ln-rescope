package org.gooru.rescope.infra.services.core.competencymapcreator;

import java.util.List;
import org.gooru.rescope.infra.services.core.algebra.competency.Competency;
import org.gooru.rescope.infra.services.core.algebra.competency.mappers.CompetencyMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

interface CompetencyMapCreatorDao {

  @Mapper(CompetencyMapper.class)
  @SqlQuery(
      "SELECT dcmt.tx_subject_code, dcmt.tx_domain_code, dcmt.tx_comp_code, dcmt.tx_comp_seq FROM   "
          + " domain_competency_matrix dcmt WHERE  dcmt.tx_subject_code = :subjectCode ")
  List<Competency> fetchAllCompetenciesForSubject(@Bind("subjectCode") String subjectCode);

}
