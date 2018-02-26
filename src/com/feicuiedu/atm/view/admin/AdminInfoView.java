package com.feicuiedu.atm.view.admin;

import java.util.List;
import java.util.Objects;

import com.feicuiedu.atm.dao.AtmUserDao;
import com.feicuiedu.atm.dao.AtmUserDaoImpl;
import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;
import com.feicuiedu.atm.view.user.UserInfo;


public class AdminInfoView extends AbstractView {
    
    Integer index;
    
    
    @Override
    public void init(ViewTarget request) {
        index = request.getParameterDefault("index", 0);
    }
    
    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        AtmUserDao dao = new AtmUserDaoImpl();
        
        List<AtmUser> users = dao.getNormalUsers();
        users.addAll(dao.getDeletedUsers());
        users.addAll(dao.getLockedUsers());
        
        // 打印标题
        Console.write(msg("YT"));
        

        // 暂无交易记录
        if (users.isEmpty()) {
            
            // 输出暂无交易记录
            Console.write(msg("YE"));
        } 
        else {
            
            UserInfo info = new UserInfo(users.get(index)); 
            info.execute();
            
            // 设置跳转
            // 如果未到尾页, 输出下一页选项
            if (index < users.size() - 1) { 
                operation.add(msg("YN"), this.getClass());
            }
            
            // 账户正常时, 输出销户选项
            if (Objects.equals("正常", users.get(index).getStatus())) {
                operation.add(msg("销户"), AdminRemoveView.class);
            }
            
            // 账户锁定时, 输出解锁用户选项
            if (Objects.equals("已锁定", users.get(index).getStatus())) {
                operation.add(msg("解锁账户"), AdminUnlockView.class);
            }
            
            operation.add(msg("修改账户密码"), AdminPasswordView.class);
 
            operation.add(msg("UL0"), AdminMainMenuView.class);
            
            response.setParameter("user", users.get(index));
            
            // 设置参数
            response.setParameter("index", ++index);
            
        }
        
    }

    
}