package com.feicuiedu.atm.view.user;

import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 用户信息
 * 
 * @author Benzolamps
 *
 */
public class UserInfo extends AbstractView{
    
    private AtmUser user; // 要展示的用户
    
    // 初始化
    public UserInfo(AtmUser user) {
        this.user = user;
    }
    
    @Override
    public void execute(ViewOperation operation, ViewTarget response) {
        
        // 账号
        Console.write(msg("BA"), user.getCardId());
        
        // 余额
        Console.write(msg("BB"), user.getBalance());
        
        // 姓名
        Console.write(msg("BN"), user.getName());
        
        // 身份证号
        Console.write(msg("BI"), user.getIdNo());
        
        // 性别
        Console.write(msg("BG"), user.getGender());
        
        // 家庭住址
        Console.write(msg("BH"), user.getAddress());
        
        // 出生日期
        Console.write(msg("BE"), user.getBirth());
        
        // 账户状态 
        Console.write(msg("BS"), user.getStatus());
 
    }

    @Override
    public void init(ViewTarget request) { }

}
