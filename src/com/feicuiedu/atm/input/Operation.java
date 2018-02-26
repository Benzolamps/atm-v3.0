package com.feicuiedu.atm.input;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.feicuiedu.atm.exception.BenzolampsException;

/**
 * 给一个选项, 返回一个值, 取代switch case case T return R
 * 
 * @author Benzolamps
 *
 * @param <T>
 *        选项数据类型
 * @param <R>
 *        返回数据类型
 */
public abstract class Operation<T, R> {
    
    private Map<T, R> map; // 存储选项与返回值
    
    private Map<T, Runnable> actions;
    
    public Operation() {
        super();
        map = new LinkedHashMap<T, R>();
        actions = new LinkedHashMap<>();
    }
    
    // 添加一个选项, 并返回当前操作集
    // 这样设计受StringBuffer的append()方法的启发
    public Operation<T, R> add(T key, R value) {
        
        map.put(key, value);
        return this;
    }
    
    public Operation<T, R> addAction(T key, Runnable action) {
        if (!map.containsKey(key)) {
            throw new BenzolampsException(key + "不存在");
        }
        
        actions.put(key, action);
        
        return this;
    }
    
    /**
     * 删除一个选项
     * 
     * @param key
     * @return
     */
    public Operation<T, R> remove(T key) {
        map.remove(key);
        return this;
    }
    
    /*
     * 删除所有选项
     */
    public Operation<T, R> clear() {
        map.clear();
        return this;
    }
    
    /**
     * 获取选项个数
     * @return
     */
    public int size() {
        return map.size();
    }
    
    /**
     * 返回key的list
     * 
     * @return
     */
    protected List<T> keyList() {
        return new ArrayList<>(map.keySet());
    }
    
    protected abstract T getInput(); // 获取用户的输入
    
    public final R action() {
        
        if (map.size() == 0) { // Map为空
            throw new BenzolampsException("map不能为空");
        }
        
        T input = getInput(); // 获取用户的输入
        
        if (!map.containsKey(input)) {
            
            throw new BenzolampsException(input + "不存在");
        }
        
        if (actions.containsKey(input)) {
            actions.get(input).run();
        }
        
        return map.get(input); // 返回对应的值
    }
}
