package com.feicuiedu.atm.view.user;

import com.feicuiedu.atm.entity.AtmUser;
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
public class MainMenuView extends AbstractView {
    
    private AtmUser user; // 用户

    @Override
    public void init(ViewTarget target) {
        user = target.getParameter("user");
    }

    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        
        // 请选择您要办理的业务
        Console.write(msg("MM"));
        
        // 设置跳转
        operation.addAll(
            
            // 查询业务
            msg("MM1"), UserInfoView.class,
            
            // 取款业务
            msg("MM2"), DebitView.class,
            
            // 存款业务
            msg("MM3"), DepositView.class,
            
            // 转账业务
            msg("MM4"), TransferView.class,
            
            // 退出
            msg("MM6"), LoginView.class
        );
        
        // 设置参数
        response.setParameter("user", user);
    }
}
