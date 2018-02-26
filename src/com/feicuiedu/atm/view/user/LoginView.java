package com.feicuiedu.atm.view.user;

import java.util.Objects;
import java.util.function.Predicate;

import com.feicuiedu.atm.dao.AtmUserDao;
import com.feicuiedu.atm.dao.AtmUserDaoImpl;
import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 登录界面
 * 
 * @author Benzolamps
 *
 */
public class LoginView extends AbstractView {
    
    private int count = 0; // 当前密码输入次数
    private static final int MAX_COUNT = 3; // 最大密码输入次数
    private AtmUserDao dao; 
    private AtmUser user; // 用户

    @Override
    public void init(ViewTarget request) { }
    
    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        
        dao = new AtmUserDaoImpl();
        
        // 打印欢迎语句
        Console.write(msg("LW0"));
        
        Predicate<String> con = dao.accountContains();
        
        // 获取账号
        String account = validation(msg("LI0")).addCondition(con, msg("LE0")).execute();
        
        // 生成账户
        user = dao.getUserByCardId(account);
        user = user == null ? dao.getUserByIdNo(account) : user;

        con = password();
        
        // 获取密码
        String password = validation(msg("LI1")).addCondition(con, msg("LE1")).execute();
  
        if (!Objects.equals("正常", user.getCardId())) { // 账号状态异常
            
            Console.write(msg("LE3"));
            response.setClazz(this.getClass());
            
        } else if (Objects.equals(password, user.getPassword())) { // 密码正确
            
            Console.write(msg("LS"));
            response.setClazz(MainMenuView.class);
            
        } else { // 密码错误已达上限
            
            dao.lockUser(user);
            Console.write(msg("LE2"));
            response.setClazz(this.getClass());
        }
        
        // 设置参数
        response.setParameter("user", user); 
    }
    
    // 验证密码
    
    private Predicate<String> password() {
        
        Predicate<String> con = new Predicate<>() {

            // 返回值标识是否退出循环
            @Override
            public boolean test(String str) {
                
                if (!Objects.equals("正常", user.getStatus())) {
                    return true;
                }
                
                // 密码输入次数
                if (count++ == LoginView.MAX_COUNT) {
                    return true;
                }
                
                // 密码正确
                return Objects.equals(str, user.getPassword());
            }
        };
        
        return con;
    }
}
