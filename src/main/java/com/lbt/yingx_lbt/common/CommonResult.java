package com.lbt.yingx_lbt.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult {
    private String status;
    private String message;
    private Object Data;
    public CommonResult success(String status,String message,Object data){
        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(status);
        commonResult.setMessage(message);
        commonResult.setData(data);
        return commonResult;
    }
    public CommonResult fail(String status,String message){
        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(status);
        commonResult.setMessage(message);
        commonResult.setData(null);
        return commonResult;
    }
}
