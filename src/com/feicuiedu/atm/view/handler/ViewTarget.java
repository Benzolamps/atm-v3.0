package com.feicuiedu.atm.view.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.feicuiedu.atm.exception.BenzolampsException;
import com.feicuiedu.atm.view.ViewInterface;

/**
 * 界面的跳转信息
 * 
 * @author Benzolamps
 *
 */
public class ViewTarget {
    
    private Class<? extends ViewInterface> clazz; // 界面对应的类
    
    private Map<String, Object> paramMap; // 参数

    /**
     * 用类生成ViewTarget对象
     * @param clazz
     */
    public ViewTarget(Class<? extends ViewInterface> clazz) {
        this.clazz = clazz;
        this.paramMap = new HashMap<>();
    }
    
    /**
     * 设置参数
     * @param param 要设置的参数
     * @param value 参数值
     */
    public void setParameter(String param, Object value) {
        paramMap.put(param, value);
    }
    
    public void setParameters(Object... args) {
        
        if (args.length % 2 != 0) {
            throw new BenzolampsException("参数个数错误");
        }
        
        for (int i = 0; i < args.length; i++) {
            paramMap.put((String) args[i], args[i+1]);
        }
    }
    
    /**
     * 获取参数
     * @param param 参数
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String param) {
        return (T) paramMap.get(param);
    }
    
    /**
     * 获取参数, 为null时抛异常
     * @param param
     * @return
     */
    public <T> T getParameterNonNull(String param) {
        return Objects.requireNonNull(getParameter(param));
    }
    
    /**
     * 获取参数, 为null时返回默认值
     * @param param
     * @param value
     * @return
     */
    public <T> T getParameterDefault(String param, T value) {
        T value0 = getParameter(param);
        return value0 == null ? value : value0;
    }
    
    /**
     * 设置类
     * 
     * @param clazz
     */
    public void setClazz(Class<? extends ViewInterface> clazz) {
        this.clazz = clazz;
    }
    
    /**
     * 获取类
     * 
     * @return
     */
    public Class<? extends ViewInterface> getClazz() {
        return clazz;
    }
}
