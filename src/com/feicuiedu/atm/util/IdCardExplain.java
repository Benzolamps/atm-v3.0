package com.feicuiedu.atm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;

import org.json.JSONException;
import org.json.JSONObject;

import com.feicuiedu.atm.input.Console;
import com.show.api.ShowApiRequest;

/**
 * 身份证号解析
 * 写的比较乱
 * 
 * @author Benzolamps
 *
 */
public class IdCardExplain {
    
    private static String url;
    private static String appid;
    private static String secret;
    
    {
        JSONObject obj = CommonUtils.fileToJSON(CommonUtils.getClassPath() + "showapi.json");
        url = obj.getString("url");
        appid = obj.getString("appid");
        secret = obj.getString("secret");
    }

    private Integer gender;
    private Date birth;
    private String address;
    private boolean result;
    private String error;
    
    /**
     * 利用身份证号构建解析器
     * @param idCard
     * @throws JSONException
     * @throws ParseException
     */
    public IdCardExplain(String idCard) {
        
        if (idCard == null || idCard.isEmpty()) {
            idCard = "00";
        }
        

        JSONObject obj = getJSONObject(idCard);
        
        result = obj.getJSONObject("showapi_res_body").getInt("ret_code") >= 0;
        
        try {
            error = obj.getJSONObject("showapi_res_body").getString("remark");
        } catch (JSONException e) {}
        
        try {
            error = obj.getJSONObject("showapi_res_body").getString("retMsg");
        } catch (JSONException e) {}
        
        if (result) {
            JSONObject retData = obj.getJSONObject("showapi_res_body").getJSONObject("retData");
            gender = "M".equals(retData.getString("sex")) ? 1 : 2;
            try {
                birth = new SimpleDateFormat("yyyy-MM-dd").parse(retData.getString("birthday"));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  
            address = retData.getString("address");
        }
    }
    
    
    private JSONObject getJSONObject(String idCard) {
        ShowApiRequest request = new ShowApiRequest(url, appid, secret);
        request.addTextPara("id", idCard);
        return new JSONObject(request.post());
    }
    
    public static void main(String[] args) throws JSONException, ParseException {
        IdCardExplain explain = new IdCardExplain("370902199603055411");
        
        System.out.println(explain.getResult());
        System.out.println(explain.getError());
        System.out.println(explain.getBirth());
        System.out.println(explain.getGender());
        System.out.println(explain.getAddress());
    }
    
    /**
     * 获取性别
     * 
     * @return the gender
     */
    public Integer getGender() {
        return gender;
    }


    /**
     * 获取出生日期
     * 
     * @return the birth
     */
    public Date getBirth() {
        return birth;
    }


    /**
     * 获取家庭住址
     * 
     * @return the address
     */
    public String getAddress() {
        return address;
    }


    /**
     * 获取解析结果
     * 
     * @return the result
     */
    public boolean getResult() {
        return result;
    }


    /**
     * 获取错误信息
     * 
     * @return the error
     */
    public String getError() {
        return error;
    }
    
    public static Predicate<String> validCondition() {
        Predicate<String> con = new Predicate<>() {

            @Override
            public boolean test(String str) {
                IdCardExplain exp = new IdCardExplain(str);
                
                if (!exp.getResult()) {
                    Console.write(exp.getError());
                    return false;
                }
                
                return true;
            }
        };
        
        return con;
    }
}
