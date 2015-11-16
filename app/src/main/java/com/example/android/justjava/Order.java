package com.example.android.justjava;

/**
 * Created by vises_000 on 10/30/2015.
 */
public class Order {

    private int id;
    private String order_date;
    private String cust_name;
    private int order_id;
    private int quantity;
    private String toppings;
    private int total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getToppings() {
        return toppings;
    }

    public void setToppings(String toppings) {
        this.toppings = toppings;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", order_date='" + order_date + '\'' +
                ", cust_name='" + cust_name + '\'' +
                ", order_id=" + order_id +
                ", quantity=" + quantity +
                ", toppings='" + toppings + '\'' +
                ", total=" + total +
                '}';
    }
}
