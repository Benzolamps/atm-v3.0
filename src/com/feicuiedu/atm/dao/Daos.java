package com.feicuiedu.atm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.feicuiedu.atm.database.DatabaseSentence;
import com.feicuiedu.atm.database.DatabaseUtils;
import com.feicuiedu.atm.exception.BenzolampsException;
import com.feicuiedu.atm.util.CommonUtils;

/**
 * Daos的公用类
 */
public final class Daos {
    
    private static DatabaseSentence getDatabaseSentence(String methodName) {

        try {
            return DatabaseUtils.getProperties().getSentenceByMethod(methodName);
            
        } catch (ClassNotFoundException e) {
            
            throw new BenzolampsException(e);
        }
    }

    /**
     * 获取调用该方法的方法的方法对应的Sql语句执行的结果
     * @param clazz 返回结果的类型
     * @param params 参数
     * @param <T> 返回结果的类型
     * @return 结果
     * @throws ReflectiveOperationException 
     * @throws SQLException 
     */
    public static <T> T invokeObject(Class<T> clazz, Object... params) {

        // 获取调用该方法的方法名
        String method = CommonUtils.getCallerMethodName();
        
        DatabaseSentence sentence = getDatabaseSentence(method);

        // 如果sentence对象不为空, 返回执行结果
        try {
            
            return sentence == null ? null : sentence.invokeObject(clazz, params);
            
        } catch (SQLException | ReflectiveOperationException e) {
            
            throw new BenzolampsException(e);
        }
        
    }
    
    /**
     * 获取调用该方法的方法的方法对应的Sql语句执行的结果
     * @param clazz 返回结果的类型
     * @param params 参数
     * @param <T> 返回结果的类型
     * @return 结果
     * @throws ReflectiveOperationException 
     * @throws SQLException 
     */
    public static <T> List<T> invokeList(Class<T> clazz, Object... params) {

        String method = CommonUtils.getCallerMethodName();
        
        List<T> list = new ArrayList<>();
        
        DatabaseSentence sentence = getDatabaseSentence(method);
        try {
 
            return sentence == null ? list : sentence.invokeList(clazz, params);
            
        } catch (SQLException | ReflectiveOperationException e) {
            
            throw new BenzolampsException(e);
        }
    }

}
