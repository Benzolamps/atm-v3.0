package com.feicuiedu.atm.view;

import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 界面的公共接口
 * 
 * @author Benzolamps
 *
 */
public interface ViewInterface {
    
    /**
     * 初始化参数
     * 
     * @param target
     */
    void init(ViewTarget target);
    
    /**
     * 执行
     */
    void execute();
    
    /**
     * 跳转
     * 
     * @return
     */
    ViewTarget redirect();
}
