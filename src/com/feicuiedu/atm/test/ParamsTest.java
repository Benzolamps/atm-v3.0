package com.feicuiedu.atm.test;

public class ParamsTest {
    
    public static void test(Object... params) {
        // TODO Auto-generated method stub
        for (int i = 0; i < params.length; i++) {
            System.out.println(params[i]);
        }
    }
    
    public static void main(String[] args) {
        test(1, 3, 20, "hhh");
        System.out.println();
        test(new Object[] {"1", "3"});
        System.out.println();
        test(new Object[] {"小明", "小刚", "小李"}, new Object[] {"小红", "小刘"});
    }
    
}
