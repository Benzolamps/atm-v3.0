package com.feicuiedu.atm.view;

import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.admin.AdminLoginView;
import com.feicuiedu.atm.view.handler.ViewTarget;
import com.feicuiedu.atm.view.user.LoginView;

public class IndexView extends AbstractView {

    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        // TODO Auto-generated method stub
        Console.write("欢迎使用自动取款机!\r\n请选择您的身份:");
        operation.add("管理员登录", AdminLoginView.class)
        .add("用户登录", LoginView.class);
    }

    @Override
    public void init(ViewTarget request) {
        // TODO Auto-generated method stub
        
    }
     
}