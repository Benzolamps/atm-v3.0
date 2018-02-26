package com.feicuiedu.atm.input;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

import com.feicuiedu.atm.functions.RegexCondition;

/**
 * 这个类用于验证用户输入, 使用while循环直到用户输入正确
 * 
 * @author Benzolamps
 *
 */
public abstract class ValidateInput {
    
    private String message; // 提示用户输入的信息
    
    private Map<Predicate<String>, String> conditions; // 用户输入正确的条件
    
    /**
     * 默认构造器
     */
    public ValidateInput() {
        this(null);
    }
    
    /**
     * 使用提示信息创建ValidateInput对象
     * 
     * @param message
     */
    public ValidateInput(String message) {
        
        if (message == null) { // message可为空
            message = new String();
        }
        
        this.message = message;
        
        conditions = new LinkedHashMap<>(); // 创建conditon的Map
    }
    

    /**
     * 添加一个条件, 并返回当前对象
     * 
     * @param condition 条件
     * @param error     错误提示信息
     * @return
     */
    public ValidateInput addCondition(Predicate<String> condition, String error) {
        
        conditions.put(condition, error);
        return this;
    }
    
    /**
     * 添加一个条件
     * 
     * @param condition 条件
     * @return
     */
    public ValidateInput addCondition(Predicate<String> condition) {
        return addCondition(condition, null);
    }
    
    /**
     * 获取用户输入的值
     * 
     * @param message
     * @return
     */
    protected abstract String test(String message);
    
    /**
     * 当用户输入有误时调用
     * 
     * @param input
     * @param error
     */
    protected abstract void error(String input, String error);
    
    /**
     * 执行输入并验证
     * @return
     */
    public String execute() {
        
        /*String input = test(message);

        for (Entry<Predicate<String>, String> item : conditions.entrySet()) {
            if (!item.getKey().test(input)) {
                error(input, item.getValue());
                input = execute();
                break;
            }
        }
        
        return input; */   
        /**
         * 之前采用递归, 现更正
         */
        
        String input = null;
        
        // 执行循环, 直到验证通过
        do {
            
            input = test(message);
            
        } while (!validate(input));
        
        return input;
    }
    
    /**
     * 验证字符串是否正确
     * @param message
     * @return
     */
    private boolean validate(String message) {

        // 遍历条件Map
        for (Map.Entry<Predicate<String>, String> item : conditions.entrySet()) {
            
            if (!item.getKey().test(message)) { // 逐个条件检查
                
                if (item.getValue() != null) {
                    
                    // 打印错误信息
                    error(message, item.getValue());
                }
                return false;
            }
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        
        ValidateInput input = new ValidateInput() {
            
            @Override
            protected String test(String message) {
                System.out.println("请输入");
                @SuppressWarnings("resource")
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
            
            @Override
            protected void error(String input, String error) {
                // TODO Auto-generated method stub
                System.out.println(error);
            }
        };
        
        input.addCondition(new RegexCondition("[A-Z]{4}"), "a-z");
        input.addCondition(new RegexCondition("[A-G]{4}"), "a-g");
        input.addCondition(new RegexCondition("[B-E]{4}"), "b-e");
        System.out.println(input.execute());;
    }

}
