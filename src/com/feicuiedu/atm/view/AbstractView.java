package com.feicuiedu.atm.view;

import com.feicuiedu.atm.functions.RegexCondition;
import com.feicuiedu.atm.input.ConsoleValidateInput;
import com.feicuiedu.atm.input.ValidateInput;
import com.feicuiedu.atm.util.BenzolampsProperties;
import com.feicuiedu.atm.view.handler.ViewTarget;
import com.feicuiedu.atm.view.user.MainMenuView;

public abstract class AbstractView implements ViewInterface {
    
    private static BenzolampsProperties regex;
    private static BenzolampsProperties message;
    
    static {
        regex = BenzolampsProperties.getRegexProperties();
        message = BenzolampsProperties.getMessageProperties();
    }
    
    protected String rgx(String... keys) {
        return regex.getConcated(keys);
    }
    
    protected String msg(String... keys) {
        return message.getConcated(keys);
    }
    
    protected String rgxj(String delimiter, String... keys) {
        return regex.getJoined(delimiter, keys);
    }
    
    protected String msgj(String delimiter, String... keys) {
        return message.getJoined(delimiter, keys);
    }
    
    protected ValidateInput regexValidation(String message, String regex) {
        return regexValidation(message, regex, null);
    }
    
    protected ValidateInput regexValidation(String message, String regex, String error) {
        return validation(message).addCondition(new RegexCondition(regex), error);
    }
    
    protected ValidateInput validation(String message) {
        return new ConsoleValidateInput(message);
    }
    
    protected void confirmReset(ViewOperation operation) {
        operation.addAll(msg("UL0"), MainMenuView.class, 
            msg("UL1"), this.getClass(), 
            msg("UL2"), this.getClass());
        operation.addAction(msg("UL1"), new PhaseProcess(response, 0)); // 重新输入, 阶段0
        operation.addAction(msg("UL2"), new PhaseProcess(response, 1)); // 确认输入, 阶段1  
    }
    
    private ViewOperation operation;
    private ViewTarget response;
    
    public AbstractView() {
        operation = new ViewOperation();
        response = new ViewTarget(this.getClass());
    }
    
    protected abstract void execute(ViewOperation operation, ViewTarget response);
    
    @Override
    public abstract void init(ViewTarget request);
    
    @Override
    public final void execute() {
        execute(operation, response);
    }
  
    @Override
    public final ViewTarget redirect() {  
        
        if (operation.size() != 0) {
            
            response.setClazz(operation.action());
        }
        return response;
    }
}
