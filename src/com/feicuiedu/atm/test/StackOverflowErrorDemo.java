package com.feicuiedu.atm.test;

public class StackOverflowErrorDemo {
    public static int count = 0;
    
    public static void circle() {
        count++;
        circle();
    }
    
    public static void main(String[] args) {
        try {
            circle();
        } catch (StackOverflowError e) {
            System.err.println("经过" + count + "次调用circle方法, 遇到了" + e.getClass().getName());
        }
    }
}
