package com.feicuiedu.atm.view;

import com.feicuiedu.atm.exception.BenzolampsException;
import com.feicuiedu.atm.input.ConsoleOperation;

/**
 * 控制界面操作
 * 
 * @author Benzolamps
 *
 */
public class ViewOperation extends ConsoleOperation<Class<? extends ViewInterface>> {
    
    /**
     * 一次性添加多个操作
     * 
     * @param args
     * @return
     */
    @SuppressWarnings("unchecked")
    public ViewOperation addAll(Object... args) {
        
        if (args.length % 2 != 0) { // 参数个数为奇数
            
            throw new BenzolampsException("参数个数错误");
        }
        
        int i = 0;
        try { // 测试参数类型是否匹配
            
            for (; i < args.length; i++) {
                
                super.add((String) args[i], (Class<? extends ViewInterface>) args[++i]);
            }
            
        } catch (ClassCastException e) {
            
            throw new BenzolampsException("第" + (i + 1) + "个参数类型错误!", e);
        }
        
        return this;
    } 
    
    public static void main(String[] args) {
        ViewOperation operation = new ViewOperation();
        operation.addAll("开户", null, "销户", null, "修改密码", null);
        operation.action();
    }

}
