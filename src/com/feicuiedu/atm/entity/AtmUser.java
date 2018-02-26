package com.feicuiedu.atm.entity;

import java.util.Date;


/***
 * 用户类
 * 
 * @author Benzolamps
 *
 */
public class AtmUser {

    private String cardId; // 银行卡号
    private String name; // 姓名
    private String password; // 密码
    private String idNo; // 身份证号
    private String gender; // 性别
    private String address; // 家庭住址
    private Date birth; // 出生日期
    private String status; // 用户状态
    private Double balance; // 余额
    
    private AtmUser() { }

    public AtmUser(String cardId) {
        this.cardId = cardId;
    }
    public static void main(String[] args) {
        new AtmUser();
        System.out.println(new AtmUser());
    }
    
      
    /**
     * @return the cardId
     */
    public String getCardId() {
        return cardId;
    }
    
    /**
     * @param cardId the cardId to set
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * @return the idNo
     */
    public String getIdNo() {
        return idNo;
    }
    
    /**
     * @param idNo the idNo to set
     */
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
    
    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }
    
    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * @return the birth
     */
    public Date getBirth() {
        return birth;
    }
    
    /**
     * @param birth the birth to set
     */
    public void setBirth(Date birth) {
        this.birth = birth;
    }
    
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
     * 当银行卡号相等时两个账号相等
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        
        if (obj == this) { // 引用同一个对象
            return true;
        }
        
        if (!(obj instanceof AtmUser)) { // 不是AtmUser的实例
            return false;
        }
        return AtmUser.class.cast(obj).cardId.equals(this.cardId);

    }

}
