
package com.feicuiedu.atm.dao;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.feicuiedu.atm.entity.AtmRecord;
import com.feicuiedu.atm.entity.AtmUser;

/**
 * AtmUserDao实现类
 *
 * @author Benzolamps
 *
 */
public class AtmUserDaoImpl implements AtmUserDao {
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#addUser(com.feicuiedu.atm.entity.AtmUser)
     */
    @Override
    public Integer addUser(AtmUser user) {
        
        return Daos.invokeObject(Integer.class,
            user.getIdNo(),
            user.getName(),
            Byte.valueOf(user.getGender()),
            user.getBirth(),
            user.getAddress(),
            user.getPassword()
        );
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#deleteUser(com.feicuiedu.atm.entity.AtmUser)
     */
    @Override
    public Integer deleteUser(AtmUser user) {
        return Daos.invokeObject(Integer.class, user.getCardId());
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#lockUser(com.feicuiedu.atm.entity.AtmUser)
     */
    @Override
    public Integer lockUser(AtmUser user) {
        return Daos.invokeObject(Integer.class, user.getCardId());
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#unlockUser(com.feicuiedu.atm.entity.AtmUser)
     */
    @Override
    public Integer unlockUser(AtmUser user) {
        return Daos.invokeObject(Integer.class, user.getCardId());
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#changePassword(com.feicuiedu.atm.entity.AtmUser, java.lang.String)
     */
    @Override
    public Integer resetPassword(AtmUser user, String newPassword) {
        return Daos.invokeObject(Integer.class, user.getCardId(), newPassword);
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#transfer(com.feicuiedu.atm.entity.AtmUser, com.feicuiedu.atm.entity.AtmUser, java.lang.Double)
     */
    @Override
    public synchronized Integer transfer(AtmUser user1, AtmUser user2, Double amount) {
        return Daos.invokeObject(Integer.class,
            user1.getCardId(),
            user2.getCardId(),
            amount
        );
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#debit(com.feicuiedu.atm.entity.AtmUser, java.lang.Double)
     */
    @Override
    public synchronized Integer debit(AtmUser user, Double amount) {
        return Daos.invokeObject(Integer.class, user.getCardId(), amount);
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#deposit(com.feicuiedu.atm.entity.AtmUser, java.lang.Double)
     */
    @Override
    public synchronized Integer deposit(AtmUser user, Double amount) {
        return Daos.invokeObject(Integer.class, user.getCardId(), amount);
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#getUserByCardId(java.lang.String)
     */
    @Override
    public AtmUser getUserByCardId(String cardId) {
        // TODO Auto-generated method stub
        return Daos.invokeObject(AtmUser.class, cardId);
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#getUserByIdNo(java.lang.String)
     */
    @Override
    public AtmUser getUserByIdNo(String IdNo) {
        // TODO Auto-generated method stub
        return Daos.invokeObject(AtmUser.class, IdNo);
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#getAdmins()
     */
    @Override
    public List<AtmUser> getAdmins() {
        return Daos.invokeList(AtmUser.class);
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#getNormalUsers()
     */
    @Override
    public List<AtmUser> getNormalUsers() {
        return Daos.invokeList(AtmUser.class);
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#getLockedUsers()
     */
    @Override
    public List<AtmUser> getLockedUsers() {
        return Daos.invokeList(AtmUser.class);
    }
    
    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#getDeletedUsers()
     */
    @Override
    public List<AtmUser> getDeletedUsers() {
        // TODO Auto-generated method stub
        return Daos.invokeList(AtmUser.class);
    }

    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#getRecordByUser()
     */
    @Override
    public List<AtmRecord> getUserRecord(AtmUser user) {
        // TODO Auto-generated method stub

        return Daos.invokeList(AtmRecord.class, user.getCardId());
    }

    /* (non-Javadoc)
     * @see com.feicuiedu.atm.dao.AtmUserDao#getRecordByUser()
     */
    @Override
    public Predicate<String> cardIdContains() {
        
        Predicate<String> con = new Predicate<String>() {

            @Override
            public boolean test(String str) {
                return Objects.nonNull(getUserByCardId(str));
            }
        };
        
        return con;
    }

    @Override
    public Predicate<String> idNoContains() {
        
        Predicate<String> con = new Predicate<String>() {

            @Override
            public boolean test(String str) {
                return Objects.nonNull(getUserByIdNo(str));
            }
        };
        
        return con;
    }

    @Override
    public Predicate<String> accountContains() {
        
        return cardIdContains().or(idNoContains());
    }

    @Override
    public Predicate<String> getSelfCondition(AtmUser user) {

        return Predicate.isEqual(user.getCardId());
    }

    @Override
    public Predicate<String> normalUserConditon() {
        
        Predicate<String> con = new Predicate<String>() {

            @Override
            public boolean test(String str) {

                return Objects.equals(getUserByCardId(str).getStatus(), "正常");
            }
        };
        
        return con;
    }
 
}
