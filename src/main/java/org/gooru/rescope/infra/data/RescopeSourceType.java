package org.gooru.rescope.infra.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 18/5/18.
 */
public enum RescopeSourceType {

  RescopeSettingChanged("rescope", 1),
  CourseAssignmentToClass("assign.course.to.class", 2),
  ClassJoinByMembers("join.class", 3),
  OOB("oob", 4);

  private final String name;
  private final int order;

  RescopeSourceType(String name, int order) {
    this.name = name;
    this.order = order;
  }

  public String getName() {
    return this.name;
  }

  public int getOrder() {
    return order;
  }

  private static final Map<String, RescopeSourceType> LOOKUP = new HashMap<>(values().length);

  static {
    for (RescopeSourceType rescopeSourceType : values()) {
      LOOKUP.put(rescopeSourceType.name, rescopeSourceType);
    }
  }

  public static RescopeSourceType builder(String type) {
    RescopeSourceType result = LOOKUP.get(type);
    if (result == null) {
      throw new IllegalArgumentException("Invalid rescope source type: " + type);
    }
    return result;
  }

}
