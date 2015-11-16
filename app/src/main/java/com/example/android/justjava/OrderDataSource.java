package com.example.android.justjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vises_000 on 11/16/2015.
 */
public class OrderDataSource {

    private SQLiteDatabase database;
    private OrderHelper orderHelper;
    private String[] allColumns = {OrderHelper.ID, OrderHelper.ORDER_DATE, OrderHelper.CUST_NAME,
            OrderHelper.ORDER_ID, OrderHelper.TOPPINGS, OrderHelper.TOTAL};

    public OrderDataSource(Context context) {
        orderHelper = new OrderHelper(context);
    }

    public void open() {
        database = orderHelper.getWritableDatabase();
    }

    public void close() {
        orderHelper.close();
    }

    public Order createOrder(String order_date, String cust_name, int order_id,
                             int quantity, String toppings, int total) {
        ContentValues values = new ContentValues();
        values.put(OrderHelper.ORDER_DATE, order_date);
        values.put(OrderHelper.CUST_NAME, cust_name);
        values.put(OrderHelper.ORDER_ID, order_id);
        values.put(OrderHelper.QUANTITY, quantity);
        values.put(OrderHelper.TOPPINGS, toppings);
        values.put(OrderHelper.TOTAL, total);
        long insertId = database.insert(OrderHelper.TABLE_NAME, null, values);

        Cursor cursor = database.query(OrderHelper.TABLE_NAME, allColumns, OrderHelper.ID
                + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Order newOrder = cursorToOrder(cursor);
        cursor.close();
        return newOrder;
    }

    public void deleteOrder(Order order) {
        long id = order.getId();
        //Log.v(, "Comment deleted with id: " + id);
        database.delete(OrderHelper.TABLE_NAME, OrderHelper.ID + " = " + id, null);
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<Order>();
        Cursor cursor = database.query(OrderHelper.TABLE_NAME, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Order order = cursorToOrder(cursor);
            orderList.add(order);
            cursor.moveToNext();
        }
        //closing cursor
        cursor.close();

        return orderList;
    }

    private Order cursorToOrder(Cursor cursor) {
        Order order = new Order();
        order.setId(cursor.getInt(0));
        order.setOrder_date(cursor.getString(1));
        order.setCust_name(cursor.getString(2));
        order.setOrder_id(cursor.getInt(3));
        order.setQuantity(cursor.getInt(4));
        order.setToppings(cursor.getString(5));
        order.setTotal(cursor.getInt(6));
        return order;
    }
}
