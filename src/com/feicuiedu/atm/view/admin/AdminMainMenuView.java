package com.feicuiedu.atm.view.admin;

import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 用户主菜单界面
 * 
 * @author Benzolamps
 *
 */
public class AdminMainMenuView extends AbstractView {

    @Override
    public void init(ViewTarget target) { }

    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        
        // 请选择您要办理的业务
        Console.write(msg("MM"));
        
        // 设置跳转
        operation.addAll(
            
            // 开户业务
            msg("MMA0"), AdminAddView.class,
            
            // 查询所有账户业务
            msg("MMA2"), AdminInfoView.class,
            
            // 退出
            msg("MMA4"), AdminLoginView.class
        );

    }
}
