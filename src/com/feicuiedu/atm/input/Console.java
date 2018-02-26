package com.feicuiedu.atm.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.feicuiedu.atm.exception.BenzolampsException;

/**
 * 用于从控制台读写数据
 * 
 * @author Benzolamps
 *
 */
public class Console {
    
    private static PrintStream out; // 输出流
    private static BufferedReader in; 

    private Console() { }
    
    /**
     * 初始化输入输出流
     */
    static {
        
        out = System.out;
        
        in = new BufferedReader(new InputStreamReader(System.in));
    }
    
    /**
     * 向控制台写一行字符串
     * @param str
     * @param args
     */
    public static void write(String str, Object... args) {
        
        out.println(String.format(str, args));
        
    }
    
    /**
     * 先输出一行提示信息, 再从控制台读取一行
     * @param message
     * @return
     */
    public static String read(String message) {
        
        if (message != null) { // 若message==null则不输出提示信息
            Console.write(message);
        }
        
        try {
            
            return in.readLine().trim();
            
        } catch (IOException e) {
            
            throw new BenzolampsException(e);
        }
    }
    
    /**
     * 不输出提示信息, 直接读取一行
     * 
     * @return
     */
    public static String read() {
        
        return Console.read(null);
    }

}
