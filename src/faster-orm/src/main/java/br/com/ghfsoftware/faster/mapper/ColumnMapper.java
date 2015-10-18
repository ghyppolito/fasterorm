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

    /**
     * Constructor
     * @param relation: relation annotation
     * @param column: column annotation
     */
    public ColumnMapper(Join.Relation relation, Table.Column column){
        this.relation = relation;
        this.column = column;

    }

    /**
     * Constructor
     * @param relation: relation annotation
     * @param column: column annotation
     * @param value: value
     */
    public ColumnMapper(Join.Relation relation, Table.Column column, Object value){
        this.relation = relation;
        this.column = column;
        this.value = value;

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
}

