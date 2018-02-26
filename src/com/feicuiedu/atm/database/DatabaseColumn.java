package com.feicuiedu.atm.database;

import org.json.JSONObject;

/**
 * 
 * 数据库表的列
 * 
 * @author Benzolamps
 *
 */
public class DatabaseColumn extends DatabaseElement {
    
    private String name; // 列名
    private String field; // 类中对应的变量
    
    public DatabaseColumn(JSONObject obj) throws ClassNotFoundException {
        super(obj);
        
        this.name = obj.getString("name"); // 获取列名
        this.field = obj.getString("field");  // 获取变量名    
    }
    
    /**
     * 获取列名
     * 
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取变量名
     * 
     * @return
     */
    public String getField() {
        return field;
    }
}
