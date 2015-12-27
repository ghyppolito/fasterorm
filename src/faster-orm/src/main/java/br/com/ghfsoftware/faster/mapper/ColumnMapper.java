package br.com.ghfsoftware.faster.mapper;

import br.com.ghfsoftware.faster.annotation.Join;
import br.com.ghfsoftware.faster.annotation.Table;

/**
 * Class to mapping info of join
 * Created by Gustavo Ferreira on 18/10/15.
 */
public class ColumnMapper {

    private Join.Relation relation;
    private Table.Column column;
    private Object value;
    private Class<?> tableClass;

    /**
     * Constructor
     * @param relation: relation annotation
     * @param column: column annotation
     * @param tableClass: table class
     */
    public ColumnMapper(Join.Relation relation, Table.Column column, Class<?> tableClass){
        this.relation = relation;
        this.column = column;
        this.tableClass = tableClass;
    }

    /**
     * Constructor
     * @param relation: relation annotation
     * @param column: column annotation
     * @param value: value
     * @param tableClass: table class
     */
    public ColumnMapper(Join.Relation relation, Table.Column column, Object value, Class<?> tableClass){
        this.relation = relation;
        this.column = column;
        this.value = value;
        this.tableClass = tableClass;

    }

    /**
     * Get relation annotation
     * @return relation annotation
     */
    public Join.Relation getRelation() {
        return relation;
    }

    /**
     * Set relation
     * @param relation: relation
     */
    public void setRelation(Join.Relation relation){
        this.relation = relation;
    }

    /**
     * Get column annotation
     * @return column annotation
     */
    public Table.Column getColumn() {
        return column;
    }

    /**
     * Get value
     * @return value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Get the class that represents the table
     * @return table class
     */
    public Class<?> getTableClass() {
        return tableClass;
    }
}

