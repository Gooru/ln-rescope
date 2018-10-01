package org.gooru.rescope.infra.services.core.algebra.competency.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.gooru.rescope.infra.services.core.algebra.competency.Competency;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */
public class CompetencyMapper implements ResultSetMapper<Competency> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompetencyMapper.class);

  @Override
  public Competency map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    String subjectCode = r.getString(MapperFields.TX_SUBJECT_CODE);
    String domainCode = r.getString(MapperFields.TX_DOMAIN_CODE);
    String compCode = r.getString(MapperFields.TX_COMP_CODE);
    Integer compSeq = r.getInt(MapperFields.TX_COMP_SEQ);

    return Competency.build(subjectCode, domainCode, compCode, compSeq);
  }

  private static class MapperFields {

    private static final String TX_SUBJECT_CODE = "tx_subject_code";
    private static final String TX_DOMAIN_CODE = "tx_domain_code";
    private static final String TX_COMP_CODE = "tx_comp_code";
    private static final String TX_COMP_SEQ = "tx_comp_seq";
  }

}
