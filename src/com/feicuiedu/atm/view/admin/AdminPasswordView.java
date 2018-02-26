package com.feicuiedu.atm.view.admin;

import com.feicuiedu.atm.dao.AtmUserDao;
import com.feicuiedu.atm.dao.AtmUserDaoImpl;
import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

public class AdminPasswordView extends AbstractView {
    
    private AtmUser user;
    
    @Override
    public void init(ViewTarget request) {
        this.user = request.getParameter("user");
        
    }

    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        // 输入密码
        String password = regexValidation(msg("AP"), rgx("password")).execute();
        
        AtmUserDao dao = new AtmUserDaoImpl();
        
        dao.resetPassword(user, password);
        
        operation.add(msg("UL0"), AdminMainMenuView.class);
    }


    
}
