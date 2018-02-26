package com.feicuiedu.atm.input;

import java.util.Properties;

import com.feicuiedu.atm.functions.CharRangeCondition;
import com.feicuiedu.atm.util.BenzolampsProperties;

public class ConsoleOperation<R> extends Operation<String, R> {
    
    public ConsoleOperation() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String getInput() {
        
        String message = generateOptionMessage();
        
        ValidateInput input = new ConsoleValidateInput(message);
        
        Properties prop = BenzolampsProperties.getMessageProperties();
        
        String error = prop.getProperty("UE0");

        input.addCondition(new CharRangeCondition('1', keyList().size()), error);
        
        return keyList().get(input.execute().charAt(0) - '1');

    }
   
    
    private String generateOptionMessage() {
        
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < keyList().size(); i++) {
            sb.append((char) ('1' + i));
            sb.append(": ");
            sb.append(keyList().get(i));
            sb.append("\r\n");  
        }
        
        sb.delete(sb.length() - 2, sb.length());
        
        return sb.toString();
    }
}
