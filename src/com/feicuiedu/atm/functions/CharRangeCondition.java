package com.feicuiedu.atm.functions;

import java.util.function.Predicate;

/**
 * 获取字符串是否在指定的字符范围内, 左闭右开
 * 
 * @author Benzolamps
 *
 */
public class CharRangeCondition implements Predicate<String> {
    
    private Character min; // 下界
    private Character max; // 上界

    public CharRangeCondition(Character min, Character max) {
        this.min = min;
        this.max = max;
    }
    
    /**
     * 第二个参数是范围的长度
     * 
     * @param min
     * @param length
     */
    public CharRangeCondition(Character min, int length) {
        // TODO Auto-generated constructor stub
        this.min = min;
        this.max = (char) (min + length);
    }
    
    @Override
    public boolean test(String str) {
        if (str.length() != 1) {
            return false;
        }

        int test = str.charAt(0);

        if (test >= min && test < max) {
            return true;
        }
        
        return false;
    }
 
}
