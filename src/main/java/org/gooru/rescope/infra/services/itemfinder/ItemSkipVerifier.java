package org.gooru.rescope.infra.services.itemfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.gooru.rescope.infra.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 21/5/18.
 */
class ItemSkipVerifier {

    private final DBI dbi;
    private final UUID userId;

    ItemSkipVerifier(DBI dbi, UUID userId) {
        this.dbi = dbi;
        this.userId = userId;
    }

    boolean canSkip(ItemModel model) {
        if (model.getGutCodes() == null || model.getGutCodes().isEmpty()) {
            return false;
        }

        List<String> competencyList = new ArrayList<>(model.getGutCodes());
        SkippedItemsFinderDao dao = dbi.onDemand(SkippedItemsFinderDao.class);

        List<String> completedCompetenciesByUser = dao.findCompletedOrMasteredCompetenciesForUserInGivenList(userId,
            CollectionUtils.convertToSqlArrayOfString(competencyList));
        competencyList.removeAll(completedCompetenciesByUser);
        return competencyList.isEmpty();

    }

}
