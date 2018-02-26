package com.feicuiedu.atm.util;

import java.io.IOException;

import org.json.JSONObject;

import com.feicuiedu.atm.exception.BenzolampsException;

/**
 * 公用工具类
 *
 * @author Benzolamps
 */
public class CommonUtils {

    /**
     * 获取路径名
     * @return 路径名
     */
    public static String getClassPath() {

        return CommonUtils.class.getClassLoader().getResource("").getPath();
    }

    /**
     * 获取当前方法名
     * 比如在AtmUser.toString()方法中调用, 返回com.feicuiedu.atm.entity.AtmUser.toString
     * @return
     */
    public static String getCurrentMethodName() {

        return CommonUtils.getCallerMethodName();
    }

    /**
     * 获取调用者方法名
     *
     * <pre>
     *
     * void methodA() {
     *     methodB();
     * }
     *
     * void methodB() {
     *     System.out.println(CommonUtils.getCallerMethodName());
     * }
     *
     * </pre>
     *
     * 上述程序, 执行methodB()返回包名.类名.methodA
     *
     *
     * @return 方法名
     */
    public static String getCallerMethodName() {
        // 利用Throwable方法的getStackTrace()可以获取方法名
        Throwable t = new Throwable();
        StackTraceElement[] stes = t.getStackTrace();

        /**
         * <pre>
         *
         * void methodA() {
         *     methodB();
         * }
         *
         * void methodB() {
         *     System.out.println(CommonUtils.getCallerMethodName());
         * }
         *
         * </pre>
         *
         * stes数组第一项为getCallerMethodName
         * stes数组第二项为methodB
         * stes数组第三项为methodA
         */

        if (stes.length < 3) { // 表示当前无调用者
            return "null";
        }

        String className = stes[2].getClassName(); // 获取类名
        String methodName = stes[2].getMethodName(); // 获取方法名, 不能直接获取Method对象

        return className + "." + methodName; // 返回字符串
    }


    /**
     * 通过文件名获取JSONObject对象
     * @param fileName 文件名
     * @return
     */
    public static JSONObject fileToJSON(String fileName) {
        
        String json = null;
        
        try {

            json = new BenzolampsTextFile(fileName).read(); // 读取文本

            //System.out.println(json);

        } catch (IOException e) {

            throw new BenzolampsException(e);
        }

        if (json != null) {

            return new JSONObject(json); // 建立JSONObject
        }
        
        return null;
    }

}
