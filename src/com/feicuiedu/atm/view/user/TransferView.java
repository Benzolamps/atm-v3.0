package com.feicuiedu.atm.view.user;

import com.feicuiedu.atm.dao.AtmUserDao;
import com.feicuiedu.atm.dao.AtmUserDaoImpl;
import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 转账界面
 * 
 * @author Benzolamps
 *
 */
public class TransferView extends AbstractView{

    private AtmUser user;
    private AtmUser target; // 转账账户
    private Integer phase;
    private Double amount; // 转账金额
    
    @Override
    public void init(ViewTarget request) {
        user = request.getParameterNonNull("user");
        target = request.getParameter("target");
        amount = request.getParameterDefault("amount", 0.0);
        phase = request.getParameterDefault("phase", 0);
    }
    
    
    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        
        // 显示标题
        Console.write(msg("TT"));
        
        if (phase == 0) {
            
            inputAmount(operation, response);
            
        } else {
            
            confirmAmount(operation, response);
        }
    }
    
    private void inputAmount(ViewOperation operation, ViewTarget response) {
        System.out.println(user);
        AtmUserDao dao = new AtmUserDaoImpl();
        
        // 判断账号是否符合条件
        String account = validation(msg("TI0"))
            
            // 判断是否是自己的账号
            .addCondition(dao.getSelfCondition(user).negate(), msg("TE"))
            
            // 判断账号是否存在
            .addCondition(dao.cardIdContains(), msg("UE5"))
            
            // 判断账号状态
            .addCondition(dao.normalUserConditon(), msg("TE0"))
            .execute();
        
        // 生成账户
        target = dao.getUserByCardId(account);
        
            
        // 获取用户输入的金额
        String amountStr = regexValidation(msg("TI1"), rgx("amount"), msg("UE1")).execute();
        
        amount = Double.valueOf(amountStr);
        
        // 打印对方账号
        Console.write(msg("TZ0"), target.getCardId());
        
        // 打印对方姓名
        Console.write(msg("TZ1"), target.getName());
        
        // 打印金额
        Console.write(msg("TZ2"), amount);
        
        // 设置跳转
        confirmReset(operation);
        
        // 设置参数
        response.setParameter("user", user);
        response.setParameter("target", target);
        response.setParameter("amount", amount);
    }
    
    private void confirmAmount(ViewOperation operation, ViewTarget response) {
        
        // 转账过程
        AtmUserDao dao = new AtmUserDaoImpl();
        
        Integer result = dao.transfer(user, target, amount);
        
        user = dao.getUserByCardId(user.getCardId());
        Double balance = user.getBalance();
        
        // 打印转账结果
        Console.write(msg("TR"), result == 1 ? msg("TS") : msg("UE3"));
        
        // 打印转账金额
        Console.write(msg("TB"), amount, balance);
        
        // 设置跳转
        operation.addAll(msg("UL0"), MainMenuView.class);
        
        // 设置参数
        response.setParameter("user", user);
    }

}