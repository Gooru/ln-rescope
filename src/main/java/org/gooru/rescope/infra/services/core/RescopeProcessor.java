package org.gooru.rescope.infra.services.core;

import org.gooru.rescope.infra.jdbi.DBICreator;
import org.gooru.rescope.infra.services.itemfilter.SkippedItemsResponse;
import org.skife.jdbi.v2.DBI;

/**
 * Entry point for rescope processing
 * @author ashish.
 */

public interface RescopeProcessor {

    SkippedItemsResponse rescopedItems(RescopeProcessorContext context);

    static RescopeProcessor buildRescopeProcessor() {
        return new RescopeProcessorImpl(DBICreator.getDbiForDefaultDS(), DBICreator.getDbiForDsdbDS());
    }

    static RescopeProcessor buildRescopeProcessor(DBI dbi4core, DBI dbi4ds) {
        return new RescopeProcessorImpl(dbi4core, dbi4ds);
    }
}
