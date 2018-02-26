package com.feicuiedu.atm.database;

import java.util.List;

import org.json.JSONObject;

public class DatabaseTable extends DatabaseElement {
    
    private String name; // 表名
    private Class<?> clazz; // 类
    private List<DatabaseColumn> columns; // 列
    
    public DatabaseTable(JSONObject obj) throws ClassNotFoundException {
        super(obj);
        
        // 获取表名
        this.name = obj.getString("name");
        
        // 获取类
        this.clazz = Class.forName(obj.getString("class"));
        
        // 获取列名
        this.columns = parse(obj.getJSONArray("columns"), DatabaseColumn.class);
        
    }
    
    /**
     * 获取表名
     * 
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取类
     * 
     * @return
     */
    public Class<?> getClazz() {    
        return clazz;
    }
    
    /**
     * 获取列
     * 
     * @return
     */
    public List<DatabaseColumn> getColumns() {  
        return columns;
    }
    
    /**
     * 通过列名寻找列
     * @param name
     * @return
     */
    public DatabaseColumn getColumnByName(String name) {
        
        for (DatabaseColumn item : columns) {
            
            if (item.getName().equals(name)) {
                
                return item;
            }
        }
        
        return null;
        
        // throw new BenzolampsException("找不到" + name + "对应的column");
    }

}
