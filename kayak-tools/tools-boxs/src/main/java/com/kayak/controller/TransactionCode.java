package com.kayak.controller;

/**
 * 先放着然后存数据库里面去
 * @author beacon
 *
 */
public enum TransactionCode {
	
	CODE0000("交易成功","0000"),CODE9999("服务器停机","9999"),ALIYUN9999("通用异常","A999"),K8S9999("通用异常","B999"),K8S9998("RC文件格式不正确","B998"),K8S9997("RC文件删除失败","B9997");;
	
	
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
