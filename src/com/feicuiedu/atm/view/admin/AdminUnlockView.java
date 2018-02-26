package com.feicuiedu.atm.view.admin;

import com.feicuiedu.atm.dao.AtmUserDao;
import com.feicuiedu.atm.dao.AtmUserDaoImpl;
import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.input.ConsoleOperation;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

public class AdminUnlockView extends AbstractView {
    
    private AtmUser user;
    
    @Override
    public void init(ViewTarget request) {
        this.user = request.getParameter("user");
        
    }

    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        
        AtmUserDao dao = new AtmUserDaoImpl();
        
        Console.write("确实要解锁用户吗?");
        ConsoleOperation<Boolean> op = new ConsoleOperation<>();
        Boolean result = op.add("确定", true).add("返回", false).action();
        
        if (result) {
            
            // 锁定账户
            dao.unlockUser(user);
            Console.write("账号已解锁");
            operation.add(msg("UL0"), AdminMainMenuView.class);
            
        } else {
            
            response.setClazz(AdminMainMenuView.class);
        }  
    }
}
