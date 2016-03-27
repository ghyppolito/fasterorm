package br.com.ghfsoftware.faster.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to mapping relation
 * Created by gustavo on 27/03/16.
 */
public class RelationMapper <T, U> {

    private Class<T> clazz;
    private Class<U> clazzRelated;
    private List<FieldMapper> fields;

    /**
     * Constructor
     * @param clazz main class table
     * @param clazzRelated related class table
     */
    public RelationMapper(Class<T> clazz, Class<U> clazzRelated){
        this.clazz = clazz;
        this.clazzRelated = clazzRelated;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public Class<U> getClazzRelated() {
        return clazzRelated;
    }

    /**
     * Add fields
     * @param field fields mapper
     */
    public void addField(FieldMapper field){

        if (this.fields==null){
            this.fields = new ArrayList<>();
        }
        this.fields.add(field);
    }

    /**
     * Get fields mapper
     * @return fields mapper
     */
    public List<FieldMapper> getFields(){
        return this.fields;
    }
}
