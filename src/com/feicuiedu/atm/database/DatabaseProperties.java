package com.feicuiedu.atm.database;

import java.util.List;

import org.json.JSONObject;

import com.feicuiedu.atm.exception.BenzolampsException;
import com.feicuiedu.atm.util.CommonUtils;

/**
 * 存储数据库的属性
 * 
 * @author Benzolamps
 *
 */
public class DatabaseProperties extends DatabaseElement {

    private String driver; // 连接驱动名称
    private String url; // 连接URL 
    private String username; // 用户名
    private String password; // 密码
    private List<DatabaseTable> tables; // 数据库表
    private List<DatabaseSentence> sentences; // SQL语句
    
    public DatabaseProperties(JSONObject obj) throws ClassNotFoundException {
        super(obj);
        
        this.driver = obj.getString("driver"); // 获取连接驱动名称
        this.url = obj.getString("url"); // 获取URL
        this.username = obj.getString("username"); // 获取用户名   
        this.password = obj.getString("password"); // 获取密码
        
        // 获取数据库表
        this.tables = parse(obj.getJSONArray("tables"), DatabaseTable.class);
        
        // 获取SQL语句
        this.sentences = parse(obj.getJSONArray("sentences"), DatabaseSentence.class);
    }
    
    /**
     * 通过表名获取表, 找不到返回null
     * 
     * @param name
     * @return
     */
    public DatabaseTable getTableByName(String name) {

        
        for (DatabaseTable item : tables) {
            
            if (item.getName().equals(name)) {
                return item;
            }
        }
        
        return null;
    }
    
    /**
     * 通过类型获取表, 找不到返回null
     * 
     * @param clazz
     * @return
     * @throws ClassNotFoundException
     */
    public DatabaseTable getTableByClass(Class<?> clazz) 
        throws ClassNotFoundException {
        
        for (DatabaseTable item : tables) {
            
            if (item.getClazz().equals(clazz)) {
                return item;
            }
        }
        
        throw new BenzolampsException("找不到" + clazz.getName() + "对应的table");
    }
    
    /**
     * 通过方法名获取SQL语句
     * 
     * @param methodName
     * @return
     */
    public DatabaseSentence getSentenceByMethod(String method) {
        

        for (DatabaseSentence item : sentences) { // 遍历sentences数组

            if (item.getMethod().equals(method)) { // 对比方法名

                return item;
            }
        }

        throw new BenzolampsException("找不到" + method + "对应的sentence");
    }
    
    private static DatabaseProperties defaultProperties; // 默认的属性
    
    /**
     *    
     * 获取默认的DatabaseProperties对象
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static DatabaseProperties getDefaultProperties() {
        
        if (defaultProperties == null) {
        
            String path = CommonUtils.getClassPath();
            
            JSONObject obj = CommonUtils.fileToJSON(path + "dbconfig.json");
            
            try {
                
                defaultProperties = new DatabaseProperties(obj);
            } 
            catch (ClassNotFoundException e) {
                
                throw new BenzolampsException(e);
            }
        }
        
        return defaultProperties;
    }
    
    /**
     * 获取连接驱动名称
     * 
     * @return
     */
    public String getDriver() { 
        return driver;
    }
    
    /**
     * 获取连接URL
     * 
     * @return
     */
    public String getUrl() { 
        return url;
    }
    
    /**
     * 获取用户名
     *  
     * @return
     */
    public String getUserName() { 
        return username;
    }
    
    /**
     * 获取密码
     * 
     * @return
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 获取数据库表
     * 
     * @return
     */
    public List<DatabaseTable> getTables() {
        return tables;
    }
    
    /**
     * 获取SQL语句
     * 
     * @return
     */
    public List<DatabaseSentence> getSentences() {
        return sentences;
    }
    
}
