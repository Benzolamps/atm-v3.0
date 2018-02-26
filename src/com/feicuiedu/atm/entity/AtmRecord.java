package com.feicuiedu.atm.entity;

import java.util.Date;

/**
 * 交易记录类
 * 
 * @author Benzolamps
 *
 */
public class AtmRecord {
    
    private String serialNo; // 流水号
    private Date time; // 交易时间
    private String type; // 交易类型
    private String target; // 交易对象
    private String targetName; // 交易对象姓名
    private Double amount; // 交易金额
    private Double balance; // 交易后余额
    private String result; // 交易结果

    private AtmRecord() { }
    
    public AtmRecord(String serialNo) {
        this.serialNo = serialNo;
    }
    
    /**
     * @return the serialNo
     */
    public String getSerialNo() {
        return serialNo;
    }
    
    /**
     * @param serialNo the serialNo to set
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    
    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }
    
    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }
    
    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.target = target;
    }
    
    /**
     * @return the targetName
     */
    public String getTargetName() {
        return targetName;
    }
    
    /**
     * @param targetName the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
    
    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }
    
    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    /**
     * @return the balance
     */
    public Double getBalance() {
        return balance;
    }
    
    /**
     * @param balance the balance to set
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    
    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }
    
    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }
   
}
