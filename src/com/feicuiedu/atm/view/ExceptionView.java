package com.feicuiedu.atm.view;

import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 发生错误时显示的界面
 * 
 * @author Benzolamps
 *
 */
public class ExceptionView implements ViewInterface {

    Throwable throwable; // 发生错误的原因
    
    @Override
    public void init(ViewTarget target) {
        // TODO Auto-generated method stub
        throwable = target.getParameter("throwable");   
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        System.out.println("遇到错误啦!!!");
        throwable.printStackTrace();  
    }

    @Override
    public ViewTarget redirect() {
        // TODO Auto-generated method stub
        System.exit(0);
        return null;
    }
    
    
}
