package org.gooru.rescope.infra.services.core.algebra.competency.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyCode;
import org.gooru.rescope.infra.services.core.algebra.competency.CompetencyModel;
import org.gooru.rescope.infra.services.core.algebra.competency.DomainCode;
import org.gooru.rescope.infra.services.core.algebra.competency.SubjectCode;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */
public class CompetencyModelMapper implements ResultSetMapper<CompetencyModel> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompetencyModelMapper.class);

  @Override
  public CompetencyModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    String subjectCode = r.getString(MapperFields.TX_SUBJECT_CODE);
    String domainCode = r.getString(MapperFields.TX_DOMAIN_CODE);
    String compCode = r.getString(MapperFields.TX_COMP_CODE);
    String compName = r.getString(MapperFields.TX_COMP_NAME);
    String compDesc = r.getString(MapperFields.TX_COMP_DESC);
    String compStudentDesc = r.getString(MapperFields.TX_COMP_STUDENT_DESC);
    Integer compSeq = r.getInt(MapperFields.TX_COMP_SEQ);

    return new CompetencyModel(new SubjectCode(subjectCode), new DomainCode(domainCode),
        new CompetencyCode(compCode), compName, compDesc, compStudentDesc, compSeq);
  }

  private static class MapperFields {

    private static final String TX_SUBJECT_CODE = "tx_subject_code";
    private static final String TX_DOMAIN_CODE = "tx_domain_code";
    private static final String TX_COMP_CODE = "tx_comp_code";
    private static final String TX_COMP_NAME = "tx_comp_name";
    private static final String TX_COMP_DESC = "tx_comp_desc";
    private static final String TX_COMP_STUDENT_DESC = "tx_comp_student_desc";
    private static final String TX_COMP_SEQ = "tx_comp_seq";
  }

}
