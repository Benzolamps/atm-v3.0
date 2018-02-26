package com.feicuiedu.atm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.feicuiedu.atm.exception.BenzolampsException;

/**
 * 自定义Properties类
 * 
 * @author Benzolamps
 *
 */
public class BenzolampsProperties extends Properties{

    private static final long serialVersionUID = 7571171063950273417L;

    /**
     * 根据文件名创建对象
     * @param fileName
     * @throws IOException
     */
    public BenzolampsProperties(String fileName) throws IOException {
        super();
        this.load(new FileInputStream(fileName));
    }
    
    /**
     * 将所有的key获取值后, 连接起来
     * @param keys
     * @return
     */
    public String getConcated(String... keys) {
        return this.getJoined("", keys);
    }
    
    /**
     * 将所有的key获取值后, 用连接符连接
     * @param delimiter
     * @param keys
     * @return
     */
    public String getJoined(String delimiter, String... keys) {
        
        for (int i = 0; i < keys.length; i++) {
            keys[i] = this.getProperty(keys[i]);
        }
        
        return String.join(delimiter, keys);
    }
    
    /**
     * 获取不到返回原汁
     */
    @Override
    public String getProperty(String key) {
        String oldValue = super.getProperty(key);
        
        return oldValue == null ? key : oldValue;
    }
    
    private static BenzolampsProperties regex; // 存储正则表达式
    
    private static BenzolampsProperties message; // 存储信息
    
    static { // 初始化regex和message
        
        String regexPath = CommonUtils.getClassPath() + "regex.properties";
        
        String messagePath = CommonUtils.getClassPath() + "message.properties";
        
        try {
            regex = new BenzolampsProperties(regexPath);
            message = new BenzolampsProperties(messagePath);
            
        } catch (IOException e) {
            
            throw new BenzolampsException(e);
        }
        
    }
    
    /**
     * 获取正则表达式对应的对象
     * @return
     */
    public static BenzolampsProperties getRegexProperties() {
        return regex;
    }
    
    /**
     * 获取信息对应的对象
     * @return
     */
    public static BenzolampsProperties getMessageProperties() {
        return message;
    }
     
}
