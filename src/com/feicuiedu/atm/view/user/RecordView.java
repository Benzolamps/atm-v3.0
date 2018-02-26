package com.feicuiedu.atm.view.user;

import java.util.List;

import com.feicuiedu.atm.dao.AtmUserDao;
import com.feicuiedu.atm.dao.AtmUserDaoImpl;
import com.feicuiedu.atm.entity.AtmRecord;
import com.feicuiedu.atm.entity.AtmUser;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

public class RecordView extends AbstractView {
    
    private AtmUser user; // 用户
    private int index;    // 页数
    
    @Override
    public void init(ViewTarget request) {
        user = request.getParameterNonNull("user");
        index = request.getParameterDefault("index", 0);
    }

    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        
        AtmUserDao dao = new AtmUserDaoImpl();
        
        List<AtmRecord> records = dao.getUserRecord(user);
        
        // 打印标题
        Console.write(msg("RT"));
        
        // 暂无交易记录
        if (records.isEmpty()) {
            
            // 输出暂无交易记录
            Console.write(msg("RE0"));
        } 
        else {
            
            RecordInfo info = new RecordInfo(records.get(index)); 
            info.execute();
            
            // 设置跳转
            // 如果未到尾页, 输出下一页选项
            if (index < records.size() - 1) { 
                operation.add(msg("YN"), this.getClass());
            }

            
            response.setParameter("index", ++index);
        }
        
        
        operation.add(msg("UL0"), MainMenuView.class);
        
        // 设置参数
        response.setParameter("user", user);
        

    }

    
}