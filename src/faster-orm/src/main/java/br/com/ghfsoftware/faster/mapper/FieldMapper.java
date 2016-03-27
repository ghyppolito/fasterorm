package br.com.ghfsoftware.faster.mapper;

/**
 * Class to mapping the field of a table
 * and the field related in another table
 * Created by gustavo on 27/03/16.
 */
public class FieldMapper {

    private String field;
    private String fieldRelated;

    /**
     * Constructor
     * @param field field of the main table
     * @param fieldRelated field of the related table
     */
    public FieldMapper(String field, String fieldRelated){
        this.field = field;
        this.fieldRelated = fieldRelated;
    }

    /**
     * Get the field name
     * @return field name
     */
    public String getField() {
        return field;
    }

    /**
     * Get the field name related
     * @return field name related
     */
    public String getFieldRelated() {
        return fieldRelated;
    }
}
