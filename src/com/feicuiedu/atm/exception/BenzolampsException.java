package com.feicuiedu.atm.exception;

import java.io.PrintStream;

public class BenzolampsException extends RuntimeException {

    private static final long serialVersionUID = 6827160278395112427L;

    /**
     * 构造子类时不能调用父类的构造方法???
     * 不声明此构造方法, new BenzolampsException(Throwable)报错
     * @param cause
     */
    public BenzolampsException(Throwable cause) {
        super(cause);
    }

    public BenzolampsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BenzolampsException(String message) {
        super(message);
    }

    /**
     * 重写printStackTrace()方法
     */
    @Override
    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        out.println("系统错误");
    }
}
