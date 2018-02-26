package com.feicuiedu.atm.view.user;

import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 用户信息界面
 * 
 * @author Benzolamps
 *
 */
public class UserInfoView extends AbstractView {
    
    private AtmUser user;

    @Override
    public void init(ViewTarget request) {
        this.user = request.getParameterNonNull("user");   
    }
    
    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        // 显示标题
        Console.write(msg("BT"));
        
        // 显示信息
        new UserInfo(user).execute();

        // 显示操作
        operation.addAll(msg("BR"), RecordView.class, msg("UL0"), null);
        
        // 设置参数
        response.setParameter("user", user);
    }
}
