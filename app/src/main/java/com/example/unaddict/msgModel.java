package com.example.unaddict;

public class msgModel {
    private String msg;
    private String btn1_text, btn2_text, btn3_text,btn4_text, btn5_text;
    private boolean isUser;

    public msgModel(){}
    public msgModel(String msg,boolean isUser){
        this.msg = msg;
        this.btn1_text = "";
        this.btn2_text = "";
        this.btn3_text = "";
        this.btn4_text = "";
        this.btn5_text = "";
        this.isUser=isUser;
    }
    public msgModel(String msg, String btn1_text, String btn2_text, String btn3_text,String btn4_text,String btn5_text,boolean isUser) {
        this.msg = msg;
        this.btn1_text = btn1_text;
        this.btn2_text = btn2_text;
        this.btn3_text = btn3_text;
        this.btn4_text = btn4_text;
        this.btn5_text = btn5_text;
        this.isUser=isUser;
    }

    public msgModel(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBtn1_text() {
        return btn1_text;
    }

    public String getBtn4_text() {
        return btn4_text;
    }

    public void setBtn4_text(String btn4_text) {
        this.btn4_text = btn4_text;
    }

    public String getBtn5_text() {
        return btn5_text;
    }

    public void setBtn5_text(String btn5_text) {
        this.btn5_text = btn5_text;
    }

    public void setBtn1_text(String btn1_text) {
        this.btn1_text = btn1_text;
    }

    public String getBtn2_text() {
        return btn2_text;
    }

    public void setBtn2_text(String btn2_text) {
        this.btn2_text = btn2_text;
    }

    public String getBtn3_text() {
        return btn3_text;
    }

    public void setBtn3_text(String btn3_text) {
        this.btn3_text = btn3_text;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
}

