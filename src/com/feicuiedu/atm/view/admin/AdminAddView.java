package com.feicuiedu.atm.view.admin;

import java.util.Date;

import com.feicuiedu.atm.dao.AtmUserDao;
import com.feicuiedu.atm.dao.AtmUserDaoImpl;
import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.util.IdCardExplain;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

public class AdminAddView extends AbstractView {

    @Override
    public void init(ViewTarget request) { }
    
    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        
        AtmUserDao dao = new AtmUserDaoImpl();
        
        // 输出标题
        Console.write(msg("AT"));
        
        // 输入姓名
        String name = regexValidation(msg("AN"), ".+", msg("UE0")).execute();
        
        // 输入身份证号
        String idNo = validation(msg("AI"))
            
            // 验证身份证号是否合法
            .addCondition(IdCardExplain.validCondition())
            
            // 验证身份证号是否存在
            .addCondition(dao.idNoContains().negate())
            .execute();
        
        IdCardExplain exp = new IdCardExplain(idNo);
        
        Integer gender = exp.getGender();
        
        String address = exp.getAddress();
        
        Date birth = exp.getBirth();
        
        // 输入密码
        String password = regexValidation(msg("AP"), rgx("password")).execute();
        
        AtmUser user = new AtmUser(null);
        
        user.setIdNo(idNo);
        user.setBirth(birth);
        user.setAddress(address);
        user.setName(name);
        user.setGender(gender + "");
        user.setPassword(password);
        
        dao.addUser(user);
        
        user = dao.getUserByIdNo(idNo);
        
        // 输出开户成功
        Console.write(msg("AS"), user.getCardId(), user.getPassword());
        
        // 显示操作
        operation.addAll(msg("UL0"), AdminMainMenuView.class);
        
        
    }
}