package com.feicuiedu.atm.view.handler;

import java.util.Objects;

import com.feicuiedu.atm.exception.BenzolampsException;
import com.feicuiedu.atm.view.ExceptionView;
import com.feicuiedu.atm.view.ViewInterface;

/**
 * 界面显示类, 用于显示逻辑
 * @author Benzolamps
 *
 */
public class ViewHandler {
    
    private ViewTarget target; // 当前界面信息
    
    private ViewInterface view; // 当前界面对象
    
    public ViewHandler(ViewTarget target) { // 初始化界面

        Objects.requireNonNull(target);
        
        this.target = target;
    }
    
    /**
     * 界面显示过程
     */
    public void process() { 
        while (true) { // 将try-catch写在循环体内??
            try {
                
                // 创建界面对象
                view = target.getClazz().getDeclaredConstructor().newInstance();
                
                // 初始化界面
                view.init(target);
                
                // 执行界面
                view.execute();
                
                // 界面跳转  
                target = view.redirect();
                
            } catch (Throwable e) { // 出错啦
                
                target = new ViewTarget(ExceptionView.class);
                
                // 如果e不是BenzolampsException的对象, 则用e创建BenzolampsException的对象
                if (!(e instanceof BenzolampsException)) {
                    e = new BenzolampsException(e);
                }
                
                // 设置错误界面的异常参数
                target.setParameter("throwable", e);
            }
        }
    }
    
}
