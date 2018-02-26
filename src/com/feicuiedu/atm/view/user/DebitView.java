package com.feicuiedu.atm.view.user;

import com.feicuiedu.atm.dao.AtmUserDao;
import com.feicuiedu.atm.dao.AtmUserDaoImpl;
import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 取款界面
 * 
 * @author Benzolamps
 *
 */
public class DebitView extends AbstractView {

    private AtmUser user; // 用户
    private Integer phase; // 阶段, 标识用户是否已完成输入
    private Double amount; // 金额

    @Override
    public void init(ViewTarget request) {
        
        user = request.getParameterNonNull("user");
        phase = request.getParameterDefault("phase", 0); 
        amount = request.getParameterDefault("amount", 0.0);
    }
    
    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        
        // 显示标题
        Console.write(msg("QT"));
        
        if (phase == 0) {
            
            // 输入取款金额
            inputAmount(operation, response);
        } else {
            
            // 取款过程
            confirmAmount(operation, response);
        }
    }
    
    private void inputAmount(ViewOperation operation, ViewTarget response) {
        
        // 获取用户输入的金额
        String amountStr = regexValidation(msg("QI"), rgx("amount"), msg("UE1")).execute();
        
        amount = Double.valueOf(amountStr);
        
        // 打印金额
        Console.write(msg("QC"), amount);
        
        // 设置跳转
        confirmReset(operation);
        
        // 设置参数
        response.setParameter("user", user);
        response.setParameter("amount", amount);
    }
    
    private void confirmAmount(ViewOperation operation, ViewTarget response) { 
        
        // 取款过程
        AtmUserDao dao = new AtmUserDaoImpl();
        Integer result = dao.debit(user, amount);
        
        user = dao.getUserByCardId(user.getCardId());
        Double balance = user.getBalance();
        
        // 打印取款结果
        Console.write(msg("QR"), result == 1 ? msg("QS") : msg("UE3"));
        
        // 打印取款金额
        Console.write(msg("QB"), amount, balance);
        
        // 设置跳转
        operation.addAll(msg("UL0"), MainMenuView.class);
        
        // 设置参数
        response.setParameter("user", user);
    }
}