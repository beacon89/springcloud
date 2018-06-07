package com.kayak.controller;

/**
 * 先放着然后存数据库里面去
 * @author beacon
 *
 */
public enum TransactionCode {
	
	CODE0000("交易成功","0000"),CODE9999("服务器停机","9999");
	
	// 成员变量  
    private String message;  
	
    private String code;  
    
 // 普通方法  
    public static String getName(String code) {  
        for (TransactionCode c : TransactionCode.values()) {  
            if (code.equals(c.getCode())) {  
                return  c.getMessage();  
            }  
        }  
        return null;
    }  
    // 构造方法  
    private TransactionCode(String message, String code) {  
        this.message = message;  
        this.code = code;  
    }
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}  

}
