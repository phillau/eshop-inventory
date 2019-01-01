package com.liufei.inventory.vo;

public class Response {
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";

    private String status;
    private String message;

    public Response(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static Response failure(String message){
        return new Response(FAILURE,message);
    }

    public static Response success(String message){
        return new Response(SUCCESS,message);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
