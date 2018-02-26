package com.feicuiedu.atm.database;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.feicuiedu.atm.exception.BenzolampsException;

public class DatabaseSentence extends DatabaseElement {

    private String sentence; // SQL语句
    private String method; // 对应的方法名
    private Class<?>[] classes; // 参数类型列表
    
    public DatabaseSentence(JSONObject obj) throws ClassNotFoundException {
        super(obj);
        
        sentence = obj.getString("sentence");
        method = obj.getString("method");
        
        // 将JSONArray中的数据转换为Class对象数组
        JSONArray array = obj.getJSONArray("params");
        
        classes = new Class<?>[array.length()];
        
        for (int i = 0; i < array.length(); i++) {
            classes[i] = Class.forName(array.getString(i));
        }
    }
    
    /**
     * 执行SQL语句, 返回合适的结果 (查询多条记录), 封装到指定对象中
     *
     * @param params 参数列表
     * @return 结果
     * @throws ReflectiveOperationException
     * @throws SQLException
     */
    public <T> List<T> invokeList(Class<T> clazz, Object... params) 
        throws ReflectiveOperationException, 
        SQLException { // 不定参数为空时, 返回空数组, 而不是空对象
        
        checkParams(params);

        DatabaseUtils utils = new DatabaseUtils();
        return ((List<T>) utils.executeList(clazz, sentence, params));
    }
    
    /**
     * 执行SQL语句, 返回合适的结果 (查询一条记录)
     * 如果clazz是位于com.feicuiedu.entity包下, 将数据封装到对象中返回
     * 否则, 返回表格中第一行第一个字段的数据
     * 如果无数据, 返回null
     * 
     * @param clazz 类型
     * @param params 参数
     * @return 指定类型的对象
     * @throws SQLException
     * @throws ReflectiveOperationException
     */
    public <T> T invokeObject(Class<T> clazz, Object... params)
        throws SQLException,
        ReflectiveOperationException { // 不定参数为空时, 返回空数组, 而不是空对象
        
        checkParams(params);
        
        DatabaseUtils utils = new DatabaseUtils();
        return clazz.cast(utils.executeObject(clazz, sentence, params));
    }
    
    /**
     * 执行SQL语句, 返回受影响的行数
     * @param params
     * @return
     * @throws SQLException
     * @throws ReflectiveOperationException
     */
    public Integer invokeCount(Object... params)
        throws SQLException,
        ReflectiveOperationException { // 不定参数为空时, 返回空数组, 而不是空对象
        
        checkParams(params);
        
        DatabaseUtils utils = new DatabaseUtils();
        return utils.executeCount(sentence, params);
    }
    
    /**
     * 执行SQL语句, 返回执行结果
     * @param params
     * @return
     * @throws SQLException
     * @throws ReflectiveOperationException
     */
    public Boolean invoke(Object... params)
        throws SQLException,
        ReflectiveOperationException { // 不定参数为空时, 返回空数组, 而不是空对象
        
        checkParams(params);
        
        DatabaseUtils utils = new DatabaseUtils();
        return utils.execute(sentence, params);
    }
    
    /**
     * 检查参数是否兼容, 不兼容抛出异常
     *
     * @param params
     *        参数列表
     * @throws BenzolampsException
     */
    private void checkParams(Object[] params) throws BenzolampsException {
        
        if (classes.length != params.length) { // 参数个数不匹配
            
            String error = "参数个数错误, 应该为 " + classes.length + " , 得到 "
                + params.length;
            throw new BenzolampsException(error);
        }
        
        for (int i = 0; i < params.length; i++) {
            
            if (params[i] == null) { // 参数为空, 跳过检查
                continue;
            }
            
            Class<?> clazz = params[i].getClass(); // 获取元素的类型
            
            if (!classes[i].equals(clazz)) { // 检查类型是否匹配
                
                String error = "第" + (i + 1) + "个参数类型错误, ";
                error += "应该为 " + classes[i].getName() + ", 得到 " + clazz
                    .getName();
                throw new BenzolampsException(error);
            }
        }
    }
    
    /**
     * 获取SQL语句
     * 
     * @return SQL语句
     */
    public String getSentence() {
        return sentence;
    }
    
    /**
     * 获取方法名
     * 
     * @return 方法名
     */
    public String getMethod() {
        return method;
    }
    
    /**
     * 获取参数列表
     * 
     * @return
     */
    public Class<?>[] getParams() {
        return classes;
    }
}
