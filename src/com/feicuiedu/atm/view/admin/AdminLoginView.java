package com.feicuiedu.atm.view.admin;

import java.util.function.Predicate;

import com.feicuiedu.atm.dao.AtmUserDao;
import com.feicuiedu.atm.dao.AtmUserDaoImpl;
import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 管理员登录界面
 * 
 * @author Benzolamps
 *
 */
public class AdminLoginView extends AbstractView {
    
    @Override
    public void init(ViewTarget request) { }
    
    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        
        // 显示欢迎语句
        Console.write(msg("LW1"));
        
        // 获取管理员用户
        AtmUserDao dao = new AtmUserDaoImpl();
        AtmUser user = dao.getAdmins().get(0);
        
        // 验证用户名
        validation(msg("LI0")).addCondition(Predicate.isEqual(user.getCardId()), msg("UE5")).execute();
        
        // 验证密码
        validation(msg("LI1")).addCondition(Predicate.isEqual(user.getPassword()), msg("UE6")).execute();
        
        // 设置跳转
        response.setClazz(AdminMainMenuView.class);
    }

    
}