package com.feicuiedu.atm.view.user;

import com.feicuiedu.atm.entity.AtmRecord;
import com.feicuiedu.atm.input.Console;
import com.feicuiedu.atm.view.AbstractView;
import com.feicuiedu.atm.view.ViewOperation;
import com.feicuiedu.atm.view.handler.ViewTarget;

/**
 * 交易记录信息
 * 
 * @author Benzolamps
 *
 */
public class RecordInfo extends AbstractView {

    private AtmRecord record;
    
    public RecordInfo(AtmRecord record) {
        this.record = record;
    }

    @Override
    protected void execute(ViewOperation operation, ViewTarget response) {
        
        // 交易流水号
        Console.write(msg("RS"), record.getSerialNo());
        
        // 交易时间
        Console.write(msg("RI"), record.getTime());
        
        // 交易类型
        Console.write(msg("RY"), record.getType());
        
        // 对方账号
        Console.write(msg("RO"), record.getTarget());
        
        // 对方姓名
        Console.write(msg("RN"), record.getTargetName());
        
        // 交易金额
        Console.write(msg("RA"), record.getAmount());
        
        // 交易后余额
        Console.write(msg("RB"), record.getBalance());
        
        // 交易结果
        Console.write(msg("RR"), record.getResult());

    }

    @Override
    public void init(ViewTarget request) { }
    
}
