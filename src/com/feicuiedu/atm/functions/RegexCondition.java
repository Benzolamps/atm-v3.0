package com.feicuiedu.atm.functions;

import java.util.function.Predicate;

/**
 * 用于判断字符串是否与给定正则表达式匹配
 * 
 * @author Benzolamps
 *
 */
public class RegexCondition implements Predicate<String> {

    private String regex; // 正则表达式
    
    /**
     * 通过正则表达式构建对象
     * 
     * @param regex
     */
    public RegexCondition(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean test(String str) {
        return str.matches(regex);
    }

}
