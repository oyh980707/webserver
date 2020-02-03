package com.loveoyh.webserver.http.ex;

/**
 * 空请求异常
 * 当客户发送一个空请求时，HttpRequest初始化过程会抛出该异常
 * @author oyh
 */
public class EmptyRequestException extends RuntimeException{
    public EmptyRequestException() {
        super();
    }
    public EmptyRequestException(String message) {
        super(message);
    }
    public EmptyRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    public EmptyRequestException(Throwable cause) {
        super(cause);
    }
    protected EmptyRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
