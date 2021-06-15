package com.septem1997.api.model;

import lombok.Data;

@Data
public class Result {

    private Integer code = 0;

    private String message = "OK";

    private Object result;

    public static Result success(){
        return new Result(0,"OK");
    }

    public static Result success(Object data){
        return new Result(data);
    }

    public Result(Object result) {
        this.result = result;
    }
    public Result(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
