package com.feicuiedu.atm.test;

import java.util.List;

import com.feicuiedu.atm.dao.AtmUserDao;
import com.feicuiedu.atm.dao.AtmUserDaoImpl;
import com.feicuiedu.atm.entity.AtmRecord;
import com.feicuiedu.atm.entity.AtmUser;

public class Test {
    
    public Test() {
        // TODO Auto-generated constructor stub
    }
    
    public static void main(String[] args) {
        AtmUserDao dao = new AtmUserDaoImpl();
        
        AtmUser user = dao.getUserByCardId("BC1802200604283049");
        
        Double balance = user.getBalance();
        System.out.println(balance);
        
        user = dao.getUserByCardId("BC1802200604283049");
        
        balance = user.getBalance();
        System.out.println(balance);
        
        List<AtmRecord> record = dao.getUserRecord(user);
        
        for (AtmRecord item : record) {
           System.out.print(item.getAmount() + "~~~" + item.getBalance());
           System.out.println();
        }
    }
    
}
