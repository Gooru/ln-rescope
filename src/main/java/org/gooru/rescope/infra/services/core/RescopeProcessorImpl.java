package org.gooru.rescope.infra.services.core;

import org.gooru.rescope.infra.services.core.validators.RescopeProcessorContextValidator;
import org.gooru.rescope.infra.services.itemfilter.SkippedItemsResponse;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class RescopeProcessorImpl implements RescopeProcessor {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private RescopeProcessorContext context;

  RescopeProcessorImpl(DBI dbi4core, DBI dbi4ds) {

    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public SkippedItemsResponse rescopedItems(RescopeProcessorContext context) {
    this.context = context;

    validate();
    // TODO: Implement this
    return null;
  }

  private void validate() {
    RescopeProcessorContextValidator.build(dbi4core, dbi4ds).validate(context);
  }
}
