package com.example.android.justjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    /*
        private TextView orderIdView;
        private TextView orderDateView;
        private TextView orderNameView;
        private TextView orderQuantityView;
        private TextView orderToppingsView;
        private TextView orderTotalView;
    */
    private LinearLayout linearLayout;

    private OrderDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //find and attach views to references
        linearLayout = (LinearLayout) findViewById(R.id.card_layout);

        dataSource = new OrderDataSource(this);
        dataSource.open();

        List<Order> orderList = dataSource.getAllOrders();

        if (!orderList.isEmpty()) {
            for (int i = 0; i < orderList.size(); i++) {
                ViewStub viewStub = new ViewStub(this);
                linearLayout.addView(viewStub);
                viewStub.setLayoutResource(R.layout.orderhistory_card_layout);
                viewStub.inflate();

                CardView cardView = (CardView) linearLayout.getChildAt(i);
                LinearLayout linearLayout1 = (LinearLayout) cardView.getChildAt(0);

                LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(0);
                TextView orderIdView = (TextView) linearLayout2.getChildAt(1);

                LinearLayout linearLayout3 = (LinearLayout) linearLayout1.getChildAt(1);
                TextView orderDateView = (TextView) linearLayout3.getChildAt(1);

                LinearLayout linearLayout4 = (LinearLayout) linearLayout1.getChildAt(2);
                TextView orderNameView = (TextView) linearLayout4.getChildAt(1);

                LinearLayout linearLayout5 = (LinearLayout) linearLayout1.getChildAt(3);
                TextView orderQuantityView = (TextView) linearLayout5.getChildAt(1);

                LinearLayout linearLayout6 = (LinearLayout) linearLayout1.getChildAt(4);
                TextView orderToppingsView = (TextView) linearLayout6.getChildAt(1);

                LinearLayout linearLayout7 = (LinearLayout) linearLayout1.getChildAt(5);
                TextView orderTotalView = (TextView) linearLayout7.getChildAt(1);

                orderIdView.setText(String.valueOf(orderList.get(i).getOrder_id()));
                orderDateView.setText(orderList.get(i).getOrder_date());
                orderNameView.setText(orderList.get(i).getCust_name());
                orderQuantityView.setText(String.valueOf(orderList.get(i).getQuantity()));

                String toppings = orderList.get(i).getToppings();
                orderToppingsView.setText(toppings);

                orderTotalView.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(orderList.get(i).getTotal())));
            }
        } else {
            setContentView(R.layout.no_orders);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
