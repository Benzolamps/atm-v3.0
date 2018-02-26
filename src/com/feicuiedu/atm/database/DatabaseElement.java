package com.feicuiedu.atm.database;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.feicuiedu.atm.exception.BenzolampsException;

/**
 * 数据库元素抽象类
 * 
 * @author Benzolamps
 *
 */
public abstract class DatabaseElement {
    
    /**
     * 构造器, 通过JSONObject创建
     * 
     * @param obj JSONObject
     * @throws ClassNotFoundException
     */
    public DatabaseElement(JSONObject obj) throws ClassNotFoundException {
        super();
    }
   
    /**
     * 将JSONArray数组转换为DatabaseElement的List
     * 
     * @param clazz 类型, 必须是DatabaseElement的子类
     * @param array JSONArray对象
     * @return 对应类型的List
     */
    public static <T extends DatabaseElement> 
    List<T> parse(JSONArray array, Class<T> clazz) {
        
        List<T> list = new ArrayList<>();    
        Constructor<T> constructor = null;

        try {
            
            // 获取构造器
            constructor = clazz.getDeclaredConstructor(JSONObject.class);
        
            // 遍历JSONObject
            for (Object item : array) {         
      
                // 创建对象
                list.add(constructor.newInstance(item));          
            }
            
        } catch (ReflectiveOperationException e) {

            throw new BenzolampsException(e);
        }
        
        return list;
    }
    
}
