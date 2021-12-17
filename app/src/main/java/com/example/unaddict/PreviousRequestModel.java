package com.example.unaddict;

public class PreviousRequestModel {
    private int orderId;
    private String status , order , action;
    public PreviousRequestModel(){}
    public PreviousRequestModel(int orderId, String status, String order, String action) {
        this.orderId = orderId;
        this.status = status;
        this.order = order;
        this.action = action;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    @Override
    public String toString() {
        return "User{" +
                "orderId='" + Integer.toString(orderId) + '\'' +
                ", status=" + status +
                ", action=" + action +
                '}';
    }
}
