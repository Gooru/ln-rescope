package org.gooru.rescope.infra.services.core;

import org.gooru.rescope.infra.services.itemfilter.SkippedItemsResponse;
import org.skife.jdbi.v2.DBI;

class RescopeProcessorImpl implements RescopeProcessor {

  private final DBI dbi4core;
  private final DBI dbi4ds;

  RescopeProcessorImpl(DBI dbi4core, DBI dbi4ds) {

    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public SkippedItemsResponse rescopedItems(RescopeProcessorContext context) {
    // TODO: Implement this
    return null;
  }
}
