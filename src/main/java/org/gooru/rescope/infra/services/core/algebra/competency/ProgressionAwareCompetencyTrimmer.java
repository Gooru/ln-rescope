package org.gooru.rescope.infra.services.core.algebra.competency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ashish.
 */
class ProgressionAwareCompetencyTrimmer {

  private final List<Competency> sourceCompetencyList;
  private final Competency competencyTrimPivot;

  ProgressionAwareCompetencyTrimmer(List<Competency> sourceCompetencyList,
      Competency competencyTrimPivot) {

    this.sourceCompetencyList = sourceCompetencyList;
    this.competencyTrimPivot = competencyTrimPivot;
  }

  /*
   * Pivot is being kept, and thus trim is not trimming the pivot itself
   */
  List<Competency> trimAbove() {
    int progressionLevelPivot = competencyTrimPivot.getProgressionLevel().getProgressionLevel();
    List<Competency> result = new ArrayList<>(sourceCompetencyList.size());

    for (Competency competency : sourceCompetencyList) {
      if (competency.getProgressionLevel().getProgressionLevel() <= progressionLevelPivot) {
        result.add(competency);
      }
    }

    return result.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(result);
  }

  /*
   * Pivot is being kept, and thus trim is not trimming the pivot itself
   */
  List<Competency> trimBelow() {
    int progressionLevelPivot = competencyTrimPivot.getProgressionLevel().getProgressionLevel();
    List<Competency> result = new ArrayList<>(sourceCompetencyList.size());

    for (Competency competency : sourceCompetencyList) {
      if (competency.getProgressionLevel().getProgressionLevel() >= progressionLevelPivot) {
        result.add(competency);
      }
    }

    return result.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(result);
  }

  /*
   * Pivot is being kept, and thus trim is trimming the pivot as well
   */
  List<Competency> trimAboveIncludingPivot() {
    int progressionLevelPivot = competencyTrimPivot.getProgressionLevel().getProgressionLevel();
    List<Competency> result = new ArrayList<>(sourceCompetencyList.size());

    for (Competency competency : sourceCompetencyList) {
      if (competency.getProgressionLevel().getProgressionLevel() < progressionLevelPivot) {
        result.add(competency);
      }
    }

    return result.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(result);
  }

  /*
   * Pivot is being kept, and thus trim is trimming the pivot as well
   */
  List<Competency> trimBelowIncludingPivot() {
    int progressionLevelPivot = competencyTrimPivot.getProgressionLevel().getProgressionLevel();
    List<Competency> result = new ArrayList<>(sourceCompetencyList.size());

    for (Competency competency : sourceCompetencyList) {
      if (competency.getProgressionLevel().getProgressionLevel() > progressionLevelPivot) {
        result.add(competency);
      }
    }

    return result.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(result);
  }

}
