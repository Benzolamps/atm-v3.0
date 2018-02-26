package com.feicuiedu.atm.dao;

import java.util.List;
import java.util.function.Predicate;

import com.feicuiedu.atm.entity.AtmRecord;
import com.feicuiedu.atm.entity.AtmUser;

/**
 * 用户
 * 
 * @author Benzolamps
 *
 */
public interface AtmUserDao {
    
    /**
     * 开户
     * 
     * @param user
     */
    Integer addUser(AtmUser user);
    
    /**
     * 销户
     * 
     * @param user
     */
    Integer deleteUser(AtmUser user);
    
    /**
     * 锁定账户
     * 
     * @param user
     */
    Integer lockUser(AtmUser user);
    
    /**
     * 解锁账户
     * 
     * @param user
     */
    Integer unlockUser(AtmUser user);
    
    /**
     * 修改密码
     * 
     * @param user
     * @param newPassword 新密码
     */
    Integer resetPassword(AtmUser user, String newPassword);
    
    /**
     * 转账
     * @param user1 转账支出
     * @param user2 转账收入
     * @param amount 转账金额
     */
    Integer transfer(AtmUser user1, AtmUser user2, Double amount);
    
    /**
     * 取款
     * 
     * @param user
     * @param amount 取款金额
     * @return 取款成功返回1, 余额不足返回2
     */
    Integer debit(AtmUser user, Double amount);
    
    /**
     * 存款
     * 
     * @param user
     * @param amount 存款金额
     */
    Integer deposit(AtmUser user, Double amount);
    
    /**
     * 根据银行卡号查询账户
     * 
     * @param cardId 银行卡号
     * @return 指定银行卡号的用户或null
     */
    AtmUser getUserByCardId(String cardId);
    
    /***
     * 根据身份证号查询账户
     * 
     * @param IdNo
     * @return 指定身份证号的用户或null
     */  
    AtmUser getUserByIdNo(String IdNo);
    
    /**
     * 获取所有管理员账户
     * 
     * @return 管理员账户的列表
     */
    List<AtmUser> getAdmins();
    
    /**
     * 获取正常账户
     * 
     * @return
     */
    List<AtmUser> getNormalUsers();
    
    /**
     * 获取已锁定账户
     * 
     * @return
     */
    List<AtmUser> getLockedUsers();
    
    /**
     * 获取已销户账户
     * 
     * @return
     */
    List<AtmUser> getDeletedUsers();
    
    /**
     * 获取用户的交易记录
     * 
     * @param user
     * @return
     */
    List<AtmRecord> getUserRecord(AtmUser user);
    
    /**
     * 获取银行卡号是否存在条件
     * 
     * @return
     */
    Predicate<String> cardIdContains();
    
    /**
     * 获取身份证号是否存在条件
     * 
     * @return
     */
    Predicate<String> idNoContains();
    
    /**
     * 获取银行卡号或身份证号是否存在的条件
     * @return
     */
    Predicate<String> accountContains();
    
    /**
     * 判断账号是不是指定用户的条件
     * 
     * @return
     */
    Predicate<String> getSelfCondition(AtmUser user);
    
    /**
     * 判断账户状态是否为指定账户的条件
     * 
     */
    Predicate<String> normalUserConditon();
    
    
    
}
