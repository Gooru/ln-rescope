package org.gooru.rescope.infra.services.core.validators;

import org.gooru.rescope.infra.jdbi.DBICreator;
import org.gooru.rescope.infra.services.core.RescopeProcessorContext;
import org.skife.jdbi.v2.DBI;

/**
 * The validator to validate the context provided before rescope is calculated.
 *
 * If the validation fails, idea is to abort the processing of the rescope and halt the machinery
 * for this request.
 *
 * @author ashish.
 */

public interface RescopeProcessorContextValidator {

  void validate(RescopeProcessorContext context);

  static RescopeProcessorContextValidator build() {
    return new RescopeProcessorContextValidatorImpl(DBICreator.getDbiForDefaultDS(),
        DBICreator.getDbiForDsdbDS());
  }

  static RescopeProcessorContextValidator build(DBI dbi4core, DBI dbi4ds) {
    return new RescopeProcessorContextValidatorImpl(dbi4core, dbi4ds);
  }
}
