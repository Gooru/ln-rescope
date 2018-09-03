package org.gooru.rescope.infra.services.itemfinder;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 21/5/18.
 */
class ItemModel {

    private String courseId;
    private String unitId;
    private String lessonId;
    private String collectionId;
    private String format;
    private List<String> gutCodes;

    public ItemModel(String courseId, String unitId, String lessonId, String collectionId, String format,
        List<String> gutCodes) {
        this.courseId = courseId;
        this.unitId = unitId;
        this.lessonId = lessonId;
        this.collectionId = collectionId;
        this.format = format;
        this.gutCodes = gutCodes;
    }

    public boolean isItemCollection() {
        return "collection".equals(format);
    }

    public boolean isItemAssessment() {
        return "assessment".equals(format);
    }

    public boolean isItemCollectionExternal() {
        return "collection-external".equals(format);
    }

    public boolean isItemAssessmentExternal() {
        return "assessment-external".equals(format);
    }

    public String getCourseId() {
        return courseId;
    }

    public String getUnitId() {
        return unitId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public String getFormat() {
        return format;
    }

    public List<String> getGutCodes() {
        return gutCodes;
    }

    public static class ItemModelMapper implements ResultSetMapper<ItemModel> {

        @Override
        public ItemModel map(final int index, final ResultSet resultSet, final StatementContext statementContext)
            throws SQLException {
            String courseId = resultSet.getString("course_id");
            String unitId = resultSet.getString("unit_id");
            String lessonId = resultSet.getString("lesson_id");
            String collectionId = resultSet.getString("id");
            String format = resultSet.getString("format");
            List<String> gutCodes;

            Array gutCodesArray = resultSet.getArray("gut_codes");
            if (gutCodesArray != null) {
                List<String> originalList = Arrays.asList((String[]) gutCodesArray.getArray());
                gutCodes = new ArrayList<>(originalList);
            } else {
                gutCodes = Collections.emptyList();
            }

            return new ItemModel(courseId, unitId, lessonId, collectionId, format, gutCodes);
        }

    }

}
