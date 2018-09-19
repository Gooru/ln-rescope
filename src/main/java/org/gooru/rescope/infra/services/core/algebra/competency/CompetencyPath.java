package org.gooru.rescope.infra.services.core.algebra.competency;

import java.util.List;

/**
 * This encapsulates the path between two competencies within the same domain. Note that it does not
 * represent the path in form of competencies themselves, rather the progression that is needed to
 * be traversed from one competency to other.
 * <p>
 * Note that resulting path would be having progression arranged from smaller to bigger one.
 * However, the directionality should be obtained via {@link CompetencyPath#isPathInProgressionOrder()}
 * before using path information.
 *
 * @author ashish.
 */
public interface CompetencyPath {

  /**
   * The subject for which the path is relevant
   */
  SubjectCode getSubject();

  /**
   * The domain for which the path is relevant
   */
  DomainCode getDomain();

  /**
   * The start point for the path
   */
  Competency getStartPoint();

  /**
   * The end point for the path
   */
  Competency getEndPoint();

  /**
   * Ordered list of progression level to enable path traversal Note that not all progression may
   * have representation in db model
   */
  List<ProgressionLevel> getPath();

  /**
   * Does a path exists in order of progression (progression level of start point is lesser than end
   * point
   */
  boolean isPathInProgressionOrder();

  static CompetencyPath build(Competency startPoint, Competency endPoint) {
    return new CompetencyPathImpl(startPoint, endPoint);
  }
}
