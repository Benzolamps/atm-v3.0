package com.feicuiedu.atm.input;

public class ConsoleValidateInput extends ValidateInput {

    public ConsoleValidateInput(String message) {
        super(message);
    }

    @Override
    protected void error(String input, String error) {
        Console.write(error, input);
    }

    @Override
    protected String test(String message) {
        return Console.read(message);
    }
}
