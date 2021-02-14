package vip.testops.account.common;

import lombok.Data;

@Data
public class Response<T> {

    private Integer code;
    private String message;
    private T data;
}
