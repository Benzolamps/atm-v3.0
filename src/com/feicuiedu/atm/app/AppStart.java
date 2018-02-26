package com.feicuiedu.atm.app;

import com.feicuiedu.atm.view.IndexView;
import com.feicuiedu.atm.view.handler.ViewHandler;
import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 主程序入口
 * 
 * @author Benzolamps
 *
 */
public class AppStart {

    /***
     * 执行主程序
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        new ViewHandler(new ViewTarget(IndexView.class)).process();;
    }
    
}
